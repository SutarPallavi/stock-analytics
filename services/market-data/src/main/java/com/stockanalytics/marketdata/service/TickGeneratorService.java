package com.stockanalytics.marketdata.service;

import com.stockanalytics.shared.model.StockTick;
import com.stockanalytics.marketdata.repository.StockTickRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class TickGeneratorService {

    private static final Logger logger = LoggerFactory.getLogger(TickGeneratorService.class);
    private static final Random random = new Random();

    @Value("${tick.generator.default.symbols:INFY,TCS,RELIANCE}")
    private String defaultSymbols;

    @Value("${tick.generator.default.rate:50}")
    private int defaultRate;

    private final StockTickRepository stockTickRepository;
    private final AtomicBoolean isRunning = new AtomicBoolean(false);
    private final List<String> symbols = List.of("INFY", "TCS", "RELIANCE", "HDFC", "ICICIBANK");

    // Base prices for each symbol (simplified)
    private final BigDecimal[] basePrices = {
        BigDecimal.valueOf(1800), // INFY
        BigDecimal.valueOf(4000), // TCS
        BigDecimal.valueOf(2500), // RELIANCE
        BigDecimal.valueOf(1600), // HDFC
        BigDecimal.valueOf(950)   // ICICIBANK
    };

    @Autowired
    public TickGeneratorService(StockTickRepository stockTickRepository) {
        this.stockTickRepository = stockTickRepository;
    }

    public void startTickGeneration(String symbols, int ratePerSecond) {
        if (isRunning.compareAndSet(false, true)) {
            logger.info("Starting tick generation for symbols: {} at rate: {}/s", symbols, ratePerSecond);
            // The actual generation is handled by the scheduled method
        } else {
            logger.warn("Tick generation is already running");
        }
    }

    public void stopTickGeneration() {
        if (isRunning.compareAndSet(true, false)) {
            logger.info("Stopped tick generation");
        } else {
            logger.warn("Tick generation is not running");
        }
    }

    public boolean isRunning() {
        return isRunning.get();
    }

    @Scheduled(fixedRate = 2000) // Run every 2 seconds
    public void generateTicks() {
        if (!isRunning.get()) {
            return;
        }

        for (int i = 0; i < symbols.size(); i++) {
            String symbol = symbols.get(i);
            StockTick tick = generateTick(symbol, basePrices[i]);
            
            // Log the tick instead of sending to Kafka for now
            logger.info("Generated tick: {} -> {}", symbol, tick.getLastPrice());

            // Persist to MongoDB (every 5th tick to avoid overwhelming the database)
            if (random.nextInt(5) == 0) {
                try {
                    stockTickRepository.save(tick);
                    logger.debug("Saved tick to MongoDB: {}", tick.getId());
                } catch (Exception e) {
                    logger.error("Failed to save tick to MongoDB: {}", tick.getSymbol(), e);
                }
            }
        }
    }

    private StockTick generateTick(String symbol, BigDecimal basePrice) {
        // Generate random price variation (±5%)
        double variation = (random.nextDouble() - 0.5) * 0.1; // ±5%
        BigDecimal priceChange = basePrice.multiply(BigDecimal.valueOf(variation));
        BigDecimal newPrice = basePrice.add(priceChange).setScale(2, RoundingMode.HALF_UP);
        
        // Generate random volume
        int volume = random.nextInt(100000) + 50000; // 50k to 150k
        
        // Generate bid/ask spread
        BigDecimal spread = newPrice.multiply(BigDecimal.valueOf(0.001)); // 0.1% spread
        BigDecimal bidPrice = newPrice.subtract(spread).setScale(2, RoundingMode.HALF_UP);
        BigDecimal askPrice = newPrice.add(spread).setScale(2, RoundingMode.HALF_UP);
        
        // Generate OHLC (simplified - using same price for now)
        BigDecimal openPrice = basePrice;
        BigDecimal highPrice = newPrice.max(openPrice);
        BigDecimal lowPrice = newPrice.min(openPrice);

        return new StockTick(
                Instant.now(),
                symbol,
                newPrice,
                openPrice,
                highPrice,
                lowPrice,
                (long) volume,
                bidPrice,
                askPrice,
                "MOCK"
        );
    }

    public List<String> getAvailableSymbols() {
        return symbols;
    }
}
