package com.stockanalytics.gateway.service;

import com.stockanalytics.shared.model.StockTick;
import com.stockanalytics.shared.model.AnalyticsMetric;
import com.stockanalytics.shared.model.PortfolioUpdate;
import com.stockanalytics.shared.model.AlertNotification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class StreamingService {

    private static final Logger logger = LoggerFactory.getLogger(StreamingService.class);

    private final SimpMessagingTemplate messagingTemplate;
    
    // Track active subscriptions
    private final Map<String, String> symbolSubscriptions = new ConcurrentHashMap<>();
    private final Map<String, String> accountSubscriptions = new ConcurrentHashMap<>();

    @Autowired
    public StreamingService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @KafkaListener(topics = "${kafka.topic.stocks.ticks:stocks.ticks}")
    public void handleStockTick(StockTick tick) {
        try {
            // Broadcast to all subscribers of this symbol
            messagingTemplate.convertAndSend("/topic/ticks/" + tick.getSymbol(), tick);
            logger.debug("Broadcasted tick for symbol: {}", tick.getSymbol());
        } catch (Exception e) {
            logger.error("Failed to broadcast tick for symbol: {}", tick.getSymbol(), e);
        }
    }

    @KafkaListener(topics = "${kafka.topic.analytics.metrics:analytics.metrics}")
    public void handleAnalyticsMetric(AnalyticsMetric metric) {
        try {
            // Broadcast to all subscribers of this symbol
            messagingTemplate.convertAndSend("/topic/metrics/" + metric.getSymbol(), metric);
            logger.debug("Broadcasted metrics for symbol: {}", metric.getSymbol());
        } catch (Exception e) {
            logger.error("Failed to broadcast metrics for symbol: {}", metric.getSymbol(), e);
        }
    }

    @KafkaListener(topics = "${kafka.topic.portfolio.updates:portfolio.updates}")
    public void handlePortfolioUpdate(PortfolioUpdate update) {
        try {
            // Send to specific account
            messagingTemplate.convertAndSendToUser(
                update.getAccountId(), 
                "/queue/portfolio", 
                update
            );
            logger.debug("Sent portfolio update to account: {}", update.getAccountId());
        } catch (Exception e) {
            logger.error("Failed to send portfolio update to account: {}", update.getAccountId(), e);
        }
    }

    @KafkaListener(topics = "${kafka.topic.alerts.notifications:alerts.notifications}")
    public void handleAlertNotification(AlertNotification notification) {
        try {
            // Send to specific account
            messagingTemplate.convertAndSendToUser(
                notification.getAccountId(), 
                "/queue/alerts", 
                notification
            );
            logger.debug("Sent alert notification to account: {}", notification.getAccountId());
        } catch (Exception e) {
            logger.error("Failed to send alert notification to account: {}", notification.getAccountId(), e);
        }
    }

    public void subscribeToSymbol(String sessionId, String symbol) {
        symbolSubscriptions.put(sessionId, symbol);
        logger.info("Session {} subscribed to symbol: {}", sessionId, symbol);
    }

    public void subscribeToAccount(String sessionId, String accountId) {
        accountSubscriptions.put(sessionId, accountId);
        logger.info("Session {} subscribed to account: {}", sessionId, accountId);
    }

    public void unsubscribe(String sessionId) {
        symbolSubscriptions.remove(sessionId);
        accountSubscriptions.remove(sessionId);
        logger.info("Session {} unsubscribed from all topics", sessionId);
    }
}
