package com.stockanalytics.analytics.stream;

import com.stockanalytics.shared.model.AnalyticsMetric;
import com.stockanalytics.shared.model.StockTick;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.support.serializer.JsonSerde;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;

@Component
public class AnalyticsTopology {

    @Value("${kafka.topic.stocks.ticks:stocks.ticks}")
    private String inputTopic;

    @Value("${kafka.topic.analytics.metrics:analytics.metrics}")
    private String outputTopic;

    @Autowired
    public void buildTopology(StreamsBuilder streamsBuilder) {
        // Create Serdes
        Serde<String> stringSerde = Serdes.String();
        JsonSerde<StockTick> stockTickSerde = new JsonSerde<>(StockTick.class);
        JsonSerde<AnalyticsMetric> analyticsMetricSerde = new JsonSerde<>(AnalyticsMetric.class);

        // Create the main stream from input topic
        KStream<String, StockTick> tickStream = streamsBuilder.stream(inputTopic, 
            Consumed.with(stringSerde, stockTickSerde));

        // Simple transformation: convert StockTick to AnalyticsMetric
        KStream<String, AnalyticsMetric> metricsStream = tickStream
            .mapValues(tick -> {
                // For now, just create a simple metric with basic calculations
                BigDecimal price = tick.getLastPrice();
                BigDecimal sma20 = price; // Placeholder - would be calculated in real implementation
                BigDecimal ema12 = price; // Placeholder - would be calculated in real implementation
                BigDecimal vwap5m = price; // Placeholder - would be calculated in real implementation
                
                return new AnalyticsMetric(
                    Instant.now(),
                    tick.getSymbol(),
                    sma20,
                    ema12,
                    vwap5m
                );
            });

        // Send to output topic
        metricsStream.to(outputTopic, Produced.with(stringSerde, analyticsMetricSerde));
    }
}
