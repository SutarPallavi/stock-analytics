package com.stockanalytics.gateway.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
public class ApiController {

    private static final Logger logger = LoggerFactory.getLogger(ApiController.class);

    private final RestTemplate restTemplate;

    @Autowired
    public ApiController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("timestamp", System.currentTimeMillis());
        health.put("service", "api-gateway");
        return ResponseEntity.ok(health);
    }

    @GetMapping("/symbols")
    public ResponseEntity<Object> getSymbols() {
        try {
            // Forward to market-data service
            String url = "http://localhost:8080/api/v1/generator/status";
            Object response = restTemplate.getForObject(url, Object.class);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Failed to get symbols", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/quotes/{symbol}")
    public ResponseEntity<Object> getQuote(@PathVariable String symbol) {
        try {
            // Forward to market-data service
            String url = "http://localhost:8080/api/v1/generator/ticks/latest/" + symbol;
            Object response = restTemplate.getForObject(url, Object.class);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Failed to get quote for symbol: {}", symbol, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/metrics/{symbol}")
    public ResponseEntity<Object> getMetrics(@PathVariable String symbol) {
        try {
            // Forward to analytics service
            String url = "http://localhost:8081/api/v1/metrics/" + symbol;
            Object response = restTemplate.getForObject(url, Object.class);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Failed to get metrics for symbol: {}", symbol, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/portfolio/{accountId}")
    public ResponseEntity<Object> getPortfolio(@PathVariable String accountId) {
        try {
            // Forward to portfolio service
            String url = "http://localhost:8082/api/v1/portfolio/" + accountId;
            Object response = restTemplate.getForObject(url, Object.class);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Failed to get portfolio for account: {}", accountId, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/alerts/{accountId}")
    public ResponseEntity<Object> getAlerts(@PathVariable String accountId) {
        try {
            // Forward to alerts service
            String url = "http://localhost:8083/api/v1/alerts/" + accountId;
            Object response = restTemplate.getForObject(url, Object.class);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Failed to get alerts for account: {}", accountId, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/alerts")
    public ResponseEntity<Object> createAlert(@RequestBody Object alertRule) {
        try {
            // Forward to alerts service
            String url = "http://localhost:8083/api/v1/alerts";
            Object response = restTemplate.postForObject(url, alertRule, Object.class);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Failed to create alert", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/generator/start")
    public ResponseEntity<String> startTickGeneration(
            @RequestParam(defaultValue = "INFY,TCS,RELIANCE") String symbols,
            @RequestParam(defaultValue = "50") int rate) {
        try {
            // Forward to market-data service
            String url = String.format("http://localhost:8080/api/v1/generator/start?symbols=%s&rate=%d", symbols, rate);
            String response = restTemplate.postForObject(url, null, String.class);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Failed to start tick generation", e);
            return ResponseEntity.internalServerError()
                    .body("Failed to start tick generation: " + e.getMessage());
        }
    }

    @PostMapping("/generator/stop")
    public ResponseEntity<String> stopTickGeneration() {
        try {
            // Forward to market-data service
            String url = "http://localhost:8080/api/v1/generator/stop";
            String response = restTemplate.postForObject(url, null, String.class);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Failed to stop tick generation", e);
            return ResponseEntity.internalServerError()
                    .body("Failed to stop tick generation: " + e.getMessage());
        }
    }
}
