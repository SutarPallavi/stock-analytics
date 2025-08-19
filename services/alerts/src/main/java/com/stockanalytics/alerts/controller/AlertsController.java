package com.stockanalytics.alerts.controller;

import com.stockanalytics.alerts.model.AlertRule;
import com.stockanalytics.alerts.service.AlertService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/alerts")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
public class AlertsController {

    private static final Logger logger = LoggerFactory.getLogger(AlertsController.class);

    private final AlertService alertService;

    @Autowired
    public AlertsController(AlertService alertService) {
        this.alertService = alertService;
    }

    @PostMapping
    public ResponseEntity<AlertRule> createAlertRule(@RequestBody AlertRule alertRule) {
        try {
            logger.info("Creating alert rule for account: {} symbol: {}", 
                       alertRule.getAccountId(), alertRule.getSymbol());
            AlertRule created = alertService.createAlertRule(alertRule);
            return ResponseEntity.ok(created);
        } catch (Exception e) {
            logger.error("Failed to create alert rule", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<List<AlertRule>> getAlertRules(@PathVariable String accountId) {
        try {
            logger.info("Retrieving alert rules for account: {}", accountId);
            List<AlertRule> rules = alertService.getAlertRules(accountId);
            return ResponseEntity.ok(rules);
        } catch (Exception e) {
            logger.error("Failed to retrieve alert rules for account: {}", accountId, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/{ruleId}")
    public ResponseEntity<String> deleteAlertRule(@PathVariable String ruleId) {
        try {
            logger.info("Deleting alert rule: {}", ruleId);
            alertService.deleteAlertRule(ruleId);
            return ResponseEntity.ok("Alert rule deleted successfully");
        } catch (Exception e) {
            logger.error("Failed to delete alert rule: {}", ruleId, e);
            return ResponseEntity.internalServerError()
                    .body("Failed to delete alert rule: " + e.getMessage());
        }
    }

    @PutMapping("/{ruleId}/toggle")
    public ResponseEntity<String> toggleAlertRule(@PathVariable String ruleId,
                                                @RequestParam boolean enabled) {
        try {
            logger.info("Toggling alert rule: {} to enabled: {}", ruleId, enabled);
            alertService.toggleAlertRule(ruleId, enabled);
            return ResponseEntity.ok("Alert rule toggled successfully");
        } catch (Exception e) {
            logger.error("Failed to toggle alert rule: {}", ruleId, e);
            return ResponseEntity.internalServerError()
                    .body("Failed to toggle alert rule: " + e.getMessage());
        }
    }
}
