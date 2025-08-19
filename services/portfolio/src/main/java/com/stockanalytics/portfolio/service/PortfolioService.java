package com.stockanalytics.portfolio.service;

import com.stockanalytics.portfolio.model.*;
import com.stockanalytics.portfolio.repository.AccountRepository;
import com.stockanalytics.portfolio.repository.OrderExecutedRepository;
import com.stockanalytics.portfolio.repository.PositionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PortfolioService {

    private static final Logger logger = LoggerFactory.getLogger(PortfolioService.class);

    @Value("${kafka.topic.portfolio.updates:portfolio.updates}")
    private String portfolioUpdatesTopic;

    private final AccountRepository accountRepository;
    private final PositionRepository positionRepository;
    private final OrderExecutedRepository orderExecutedRepository;
    private final KafkaTemplate<String, PortfolioUpdate> kafkaTemplate;

    @Autowired
    public PortfolioService(AccountRepository accountRepository,
                           PositionRepository positionRepository,
                           OrderExecutedRepository orderExecutedRepository,
                           KafkaTemplate<String, PortfolioUpdate> kafkaTemplate) {
        this.accountRepository = accountRepository;
        this.positionRepository = positionRepository;
        this.orderExecutedRepository = orderExecutedRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = "${kafka.topic.orders.executed:orders.executed}")
    public void processExecutedOrder(OrderExecuted order) {
        logger.info("Processing executed order: {} for account: {}", order.getOrderId(), order.getAccountId());
        
        try {
            // Process the order and update portfolio
            processOrder(order);
            
            // Calculate and publish portfolio update
            publishPortfolioUpdate(order.getAccountId());
            
        } catch (Exception e) {
            logger.error("Failed to process executed order: {}", order.getOrderId(), e);
        }
    }

    @Transactional
    public void processOrder(OrderExecuted order) {
        String accountId = order.getAccountId();
        String symbol = order.getSymbol();
        
        // Get or create account
        Account account = getOrCreateAccount(accountId);
        
        // Get or create position
        Position position = getOrCreatePosition(accountId, symbol);
        
        if ("BUY".equals(order.getSide())) {
            processBuyOrder(account, position, order);
        } else if ("SELL".equals(order.getSide())) {
            processSellOrder(account, position, order);
        }
        
        // Save order
        orderExecutedRepository.save(order);
        
        // Update account and position
        accountRepository.save(account);
        positionRepository.save(position);
    }

    private void processBuyOrder(Account account, Position position, OrderExecuted order) {
        BigDecimal totalCost = order.getPrice().multiply(BigDecimal.valueOf(order.getQuantity()));
        BigDecimal totalFees = order.getFees();
        
        // Update cash
        account.setCash(account.getCash().subtract(totalCost).subtract(totalFees));
        
        // Update position
        if (position.getQuantity() == 0) {
            position.setQuantity(order.getQuantity());
            position.setAveragePrice(order.getPrice());
        } else {
            // Calculate new average price
            BigDecimal currentValue = position.getAveragePrice().multiply(BigDecimal.valueOf(position.getQuantity()));
            BigDecimal newValue = order.getPrice().multiply(BigDecimal.valueOf(order.getQuantity()));
            BigDecimal totalValue = currentValue.add(newValue);
            int totalQuantity = position.getQuantity() + order.getQuantity();
            
            position.setQuantity(totalQuantity);
            position.setAveragePrice(totalValue.divide(BigDecimal.valueOf(totalQuantity), 2, RoundingMode.HALF_UP));
        }
        
        position.setUpdatedAt(Instant.now());
    }

    private void processSellOrder(Account account, Position position, OrderExecuted order) {
        BigDecimal totalProceeds = order.getPrice().multiply(BigDecimal.valueOf(order.getQuantity()));
        BigDecimal totalFees = order.getFees();
        
        // Update cash
        account.setCash(account.getCash().add(totalProceeds).subtract(totalFees));
        
        // Update position
        if (position.getQuantity() < order.getQuantity()) {
            throw new IllegalStateException("Insufficient position for sell order");
        }
        
        position.setQuantity(position.getQuantity() - order.getQuantity());
        if (position.getQuantity() == 0) {
            position.setAveragePrice(BigDecimal.ZERO);
        }
        
        position.setUpdatedAt(Instant.now());
    }

    private Account getOrCreateAccount(String accountId) {
        return accountRepository.findByUserId(accountId)
                .orElseGet(() -> {
                    Account newAccount = new Account(accountId, BigDecimal.valueOf(100000), "INR");
                    return accountRepository.save(newAccount);
                });
    }

    private Position getOrCreatePosition(String accountId, String symbol) {
        return positionRepository.findByAccountIdAndSymbol(accountId, symbol)
                .orElseGet(() -> {
                    Position newPosition = new Position(accountId, symbol, 0, BigDecimal.ZERO);
                    return positionRepository.save(newPosition);
                });
    }

    public void publishPortfolioUpdate(String accountId) {
        try {
            PortfolioUpdate update = calculatePortfolioUpdate(accountId);
            kafkaTemplate.send(portfolioUpdatesTopic, accountId, update);
            logger.debug("Published portfolio update for account: {}", accountId);
        } catch (Exception e) {
            logger.error("Failed to publish portfolio update for account: {}", accountId, e);
        }
    }

    private PortfolioUpdate calculatePortfolioUpdate(String accountId) {
        Account account = accountRepository.findByUserId(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found: " + accountId));
        
        List<Position> positions = positionRepository.findByAccountId(accountId);
        
        List<PortfolioUpdate.PositionSummary> positionSummaries = positions.stream()
                .filter(p -> p.getQuantity() > 0)
                .map(p -> new PortfolioUpdate.PositionSummary(
                        p.getSymbol(), p.getQuantity(), p.getAveragePrice()))
                .collect(Collectors.toList());
        
        // Calculate NAV (simplified - in real scenario, you'd get current prices)
        BigDecimal positionsValue = positionSummaries.stream()
                .map(p -> p.getAveragePrice().multiply(BigDecimal.valueOf(p.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        BigDecimal nav = account.getCash().add(positionsValue);
        
        // Calculate P&L (simplified - in real scenario, you'd get current prices)
        PortfolioUpdate.PnL pnl = new PortfolioUpdate.PnL(BigDecimal.ZERO, BigDecimal.ZERO);
        
        return new PortfolioUpdate(Instant.now(), accountId, positionSummaries, account.getCash(), pnl, nav);
    }

    public PortfolioUpdate getPortfolio(String accountId) {
        return calculatePortfolioUpdate(accountId);
    }
}
