package com.stockanalytics.marketdata.controller;

import com.stockanalytics.shared.model.StockTick;
import com.stockanalytics.marketdata.repository.StockTickRepository;
import com.stockanalytics.marketdata.service.TickGeneratorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/generator")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
public class TickGeneratorController {

    private static final Logger logger = LoggerFactory.getLogger(TickGeneratorController.class);

    private final TickGeneratorService tickGeneratorService;
    private final StockTickRepository stockTickRepository;

    @Autowired
    public TickGeneratorController(TickGeneratorService tickGeneratorService,
                                 StockTickRepository stockTickRepository) {
        this.tickGeneratorService = tickGeneratorService;
        this.stockTickRepository = stockTickRepository;
    }

    @PostMapping("/start")
    public ResponseEntity<String> startTickGeneration(
            @RequestParam(defaultValue = "INFY,TCS,RELIANCE") String symbols,
            @RequestParam(defaultValue = "50") int rate) {
        
        logger.info("Received request to start tick generation for symbols: {} at rate: {}/s", symbols, rate);
        
        try {
            tickGeneratorService.startTickGeneration(symbols, rate);
            return ResponseEntity.ok("Tick generation started successfully");
        } catch (Exception e) {
            logger.error("Failed to start tick generation", e);
            return ResponseEntity.internalServerError()
                    .body("Failed to start tick generation: " + e.getMessage());
        }
    }

    @PostMapping("/stop")
    public ResponseEntity<String> stopTickGeneration() {
        logger.info("Received request to stop tick generation");
        
        try {
            tickGeneratorService.stopTickGeneration();
            return ResponseEntity.ok("Tick generation stopped successfully");
        } catch (Exception e) {
            logger.error("Failed to stop tick generation", e);
            return ResponseEntity.internalServerError()
                    .body("Failed to stop tick generation: " + e.getMessage());
        }
    }

    @GetMapping("/status")
    public ResponseEntity<TickGeneratorStatus> getStatus() {
        boolean isRunning = tickGeneratorService.isRunning();
        List<String> availableSymbols = tickGeneratorService.getAvailableSymbols();
        
        TickGeneratorStatus status = new TickGeneratorStatus(isRunning, availableSymbols);
        return ResponseEntity.ok(status);
    }

    @GetMapping("/ticks/{symbol}")
    public ResponseEntity<List<StockTick>> getTicksBySymbol(
            @PathVariable String symbol,
            @RequestParam(defaultValue = "100") int limit) {
        
        try {
            List<StockTick> ticks = stockTickRepository.findBySymbolOrderByTimestampDesc(symbol);
            if (ticks.size() > limit) {
                ticks = ticks.subList(0, limit);
            }
            return ResponseEntity.ok(ticks);
        } catch (Exception e) {
            logger.error("Failed to retrieve ticks for symbol: {}", symbol, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/ticks/latest/{symbol}")
    public ResponseEntity<StockTick> getLatestTick(@PathVariable String symbol) {
        try {
            StockTick latestTick = stockTickRepository.findLatestBySymbol(symbol);
            if (latestTick != null) {
                return ResponseEntity.ok(latestTick);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            logger.error("Failed to retrieve latest tick for symbol: {}", symbol, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    // Status response class
    public static class TickGeneratorStatus {
        private final boolean running;
        private final List<String> availableSymbols;

        public TickGeneratorStatus(boolean running, List<String> availableSymbols) {
            this.running = running;
            this.availableSymbols = availableSymbols;
        }

        public boolean isRunning() {
            return running;
        }

        public List<String> getAvailableSymbols() {
            return availableSymbols;
        }
    }
}
