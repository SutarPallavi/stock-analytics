package com.stockanalytics.shared.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.Instant;

public class AnalyticsMetric {

    @JsonProperty("ts")
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    private Instant timestamp;

    @JsonProperty("symbol")
    private String symbol;

    @JsonProperty("sma20")
    private BigDecimal sma20;

    @JsonProperty("ema12")
    private BigDecimal ema12;

    @JsonProperty("vwap_5m")
    private BigDecimal vwap5m;

    // Constructors
    public AnalyticsMetric() {}

    public AnalyticsMetric(Instant timestamp, String symbol, BigDecimal sma20, 
                          BigDecimal ema12, BigDecimal vwap5m) {
        this.timestamp = timestamp;
        this.symbol = symbol;
        this.sma20 = sma20;
        this.ema12 = ema12;
        this.vwap5m = vwap5m;
    }

    // Getters and Setters
    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public BigDecimal getSma20() {
        return sma20;
    }

    public void setSma20(BigDecimal sma20) {
        this.sma20 = sma20;
    }

    public BigDecimal getEma12() {
        return ema12;
    }

    public void setEma12(BigDecimal ema12) {
        this.ema12 = ema12;
    }

    public BigDecimal getVwap5m() {
        return vwap5m;
    }

    public void setVwap5m(BigDecimal vwap5m) {
        this.vwap5m = vwap5m;
    }

    @Override
    public String toString() {
        return "AnalyticsMetric{" +
                "timestamp=" + timestamp +
                ", symbol='" + symbol + '\'' +
                ", sma20=" + sma20 +
                ", ema12=" + ema12 +
                ", vwap5m=" + vwap5m +
                '}';
    }
}
