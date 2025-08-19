package com.stockanalytics.portfolio.controller;

import com.stockanalytics.portfolio.model.PortfolioUpdate;
import com.stockanalytics.portfolio.service.PortfolioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/portfolio")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
public class PortfolioController {

    private static final Logger logger = LoggerFactory.getLogger(PortfolioController.class);

    private final PortfolioService portfolioService;

    @Autowired
    public PortfolioController(PortfolioService portfolioService) {
        this.portfolioService = portfolioService;
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<PortfolioUpdate> getPortfolio(@PathVariable String accountId) {
        try {
            logger.info("Retrieving portfolio for account: {}", accountId);
            PortfolioUpdate portfolio = portfolioService.getPortfolio(accountId);
            return ResponseEntity.ok(portfolio);
        } catch (Exception e) {
            logger.error("Failed to retrieve portfolio for account: {}", accountId, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/{accountId}/refresh")
    public ResponseEntity<String> refreshPortfolio(@PathVariable String accountId) {
        try {
            logger.info("Refreshing portfolio for account: {}", accountId);
            portfolioService.publishPortfolioUpdate(accountId);
            return ResponseEntity.ok("Portfolio refresh initiated");
        } catch (Exception e) {
            logger.error("Failed to refresh portfolio for account: {}", accountId, e);
            return ResponseEntity.internalServerError()
                    .body("Failed to refresh portfolio: " + e.getMessage());
        }
    }
}
