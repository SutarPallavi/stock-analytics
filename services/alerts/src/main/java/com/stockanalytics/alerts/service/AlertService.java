package com.stockanalytics.alerts.service;

import com.stockanalytics.shared.model.AlertNotification;
import com.stockanalytics.alerts.model.AlertRule;
import com.stockanalytics.alerts.repository.AlertNotificationRepository;
import com.stockanalytics.alerts.repository.AlertRuleRepository;
import com.stockanalytics.shared.model.StockTick;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AlertService {

    private static final Logger logger = LoggerFactory.getLogger(AlertService.class);

    @Value("${kafka.topic.alerts.notifications:alerts.notifications}")
    private String alertsNotificationsTopic;

    @Value("${mail.from:alerts@stockanalytics.com}")
    private String mailFrom;

    private final AlertRuleRepository alertRuleRepository;
    private final AlertNotificationRepository alertNotificationRepository;
    private final KafkaTemplate<String, AlertNotification> kafkaTemplate;
    private final JavaMailSender mailSender;

    // In-memory deduplication cache
    private final Map<String, Instant> lastAlertTime = new ConcurrentHashMap<>();

    @Autowired
    public AlertService(AlertRuleRepository alertRuleRepository,
                        AlertNotificationRepository alertNotificationRepository,
                        KafkaTemplate<String, AlertNotification> kafkaTemplate,
                        JavaMailSender mailSender) {
        this.alertRuleRepository = alertRuleRepository;
        this.alertNotificationRepository = alertNotificationRepository;
        this.kafkaTemplate = kafkaTemplate;
        this.mailSender = mailSender;
    }

    @KafkaListener(topics = "${kafka.topic.stocks.ticks:stocks.ticks}")
    public void processTick(StockTick tick) {
        try {
            // Get all enabled alert rules for this symbol
            List<AlertRule> rules = alertRuleRepository.findBySymbolAndEnabledTrue(tick.getSymbol());
            
            for (AlertRule rule : rules) {
                if (shouldFireAlert(rule, tick)) {
                    fireAlert(rule, tick);
                }
            }
        } catch (Exception e) {
            logger.error("Failed to process tick for alerts: {}", tick.getSymbol(), e);
        }
    }

    private boolean shouldFireAlert(AlertRule rule, StockTick tick) {
        // Check deduplication
        String dedupKey = rule.getId() + "_" + rule.getSymbol();
        Instant lastFired = lastAlertTime.get(dedupKey);
        if (lastFired != null) {
            long secondsSinceLastAlert = Instant.now().getEpochSecond() - lastFired.getEpochSecond();
            if (secondsSinceLastAlert < rule.getDedupSeconds()) {
                return false;
            }
        }

        // Evaluate rule based on type
        switch (rule.getRuleType()) {
            case "PRICE_THRESHOLD":
                return evaluatePriceThreshold(rule, tick);
            case "MA_CROSS":
                return evaluateMACross(rule, tick);
            case "PCT_CHANGE_WINDOW":
                return evaluatePctChange(rule, tick);
            default:
                logger.warn("Unknown rule type: {}", rule.getRuleType());
                return false;
        }
    }

    private boolean evaluatePriceThreshold(AlertRule rule, StockTick tick) {
        double currentPrice = tick.getLastPrice().doubleValue();
        double threshold = rule.getThreshold();
        
        switch (rule.getOperator()) {
            case ">=":
                return currentPrice >= threshold;
            case "<=":
                return currentPrice <= threshold;
            case ">":
                return currentPrice > threshold;
            case "<":
                return currentPrice < threshold;
            case "==":
                return Math.abs(currentPrice - threshold) < 0.01;
            default:
                return false;
        }
    }

    private boolean evaluateMACross(AlertRule rule, StockTick tick) {
        // Simplified MA cross evaluation
        // In a real implementation, you'd get the actual MA values from analytics
        return false; // Placeholder
    }

    private boolean evaluatePctChange(AlertRule rule, StockTick tick) {
        // Simplified percentage change evaluation
        // In a real implementation, you'd calculate actual percentage change
        return false; // Placeholder
    }

    private void fireAlert(AlertRule rule, StockTick tick) {
        try {
            // Create notification
            Map<String, Object> payload = new HashMap<>();
            payload.put("symbol", tick.getSymbol());
            payload.put("last", tick.getLastPrice());
            payload.put("timestamp", tick.getTimestamp());
            
            AlertNotification notification = new AlertNotification(
                    java.util.UUID.randomUUID().toString(), // id
                    rule.getAccountId(),
                    rule.getId(),
                    Instant.now(),
                    payload,
                    "EMAIL"
            );
            
            // Save notification
            alertNotificationRepository.save(notification);
            
            // Send email
            sendAlertEmail(rule, tick);
            
            // Publish to Kafka
            kafkaTemplate.send(alertsNotificationsTopic, rule.getAccountId(), notification);
            
            // Update deduplication cache
            String dedupKey = rule.getId() + "_" + rule.getSymbol();
            lastAlertTime.put(dedupKey, Instant.now());
            
            logger.info("Alert fired for rule: {} symbol: {} price: {}", 
                       rule.getId(), tick.getSymbol(), tick.getLastPrice());
            
        } catch (Exception e) {
            logger.error("Failed to fire alert for rule: {}", rule.getId(), e);
        }
    }

    private void sendAlertEmail(AlertRule rule, StockTick tick) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(mailFrom);
            message.setTo("user@example.com"); // In real app, get from user profile
            message.setSubject("Stock Alert: " + rule.getSymbol());
            message.setText(String.format(
                    "Alert triggered for %s\n" +
                    "Current Price: %s\n" +
                    "Rule: %s %s %.2f\n" +
                    "Time: %s",
                    rule.getSymbol(),
                    tick.getLastPrice(),
                    rule.getRuleType(),
                    rule.getOperator(),
                    rule.getThreshold(),
                    tick.getTimestamp()
            ));
            
            mailSender.send(message);
            logger.debug("Alert email sent for rule: {}", rule.getId());
            
        } catch (Exception e) {
            logger.error("Failed to send alert email for rule: {}", rule.getId(), e);
        }
    }

    public AlertRule createAlertRule(AlertRule rule) {
        return alertRuleRepository.save(rule);
    }

    public List<AlertRule> getAlertRules(String accountId) {
        return alertRuleRepository.findByAccountId(accountId);
    }

    public void deleteAlertRule(String ruleId) {
        alertRuleRepository.deleteById(ruleId);
    }

    public void toggleAlertRule(String ruleId, boolean enabled) {
        alertRuleRepository.findById(ruleId).ifPresent(rule -> {
            rule.setEnabled(enabled);
            alertRuleRepository.save(rule);
        });
    }
}
