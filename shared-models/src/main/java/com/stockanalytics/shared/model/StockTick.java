package com.stockanalytics.shared.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.time.Instant;

@Document(collection = "ticks_hist")
public class StockTick {

    @Id
    private String id;

    @Field("ts")
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    private Instant timestamp;

    @Field("symbol")
    private String symbol;

    @Field("last")
    private BigDecimal lastPrice;

    @Field("open")
    private BigDecimal openPrice;

    @Field("high")
    private BigDecimal highPrice;

    @Field("low")
    private BigDecimal lowPrice;

    @Field("volume")
    private Long volume;

    @Field("bid")
    private BigDecimal bidPrice;

    @Field("ask")
    private BigDecimal askPrice;

    @Field("source")
    private String source;

    // Constructors
    public StockTick() {}

    public StockTick(Instant timestamp, String symbol, BigDecimal lastPrice, BigDecimal openPrice,
                     BigDecimal highPrice, BigDecimal lowPrice, Long volume, BigDecimal bidPrice,
                     BigDecimal askPrice, String source) {
        this.timestamp = timestamp;
        this.symbol = symbol;
        this.lastPrice = lastPrice;
        this.openPrice = openPrice;
        this.highPrice = highPrice;
        this.lowPrice = lowPrice;
        this.volume = volume;
        this.bidPrice = bidPrice;
        this.askPrice = askPrice;
        this.source = source;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public BigDecimal getLastPrice() {
        return lastPrice;
    }

    public void setLastPrice(BigDecimal lastPrice) {
        this.lastPrice = lastPrice;
    }

    public BigDecimal getOpenPrice() {
        return openPrice;
    }

    public void setOpenPrice(BigDecimal openPrice) {
        this.openPrice = openPrice;
    }

    public BigDecimal getHighPrice() {
        return highPrice;
    }

    public void setHighPrice(BigDecimal highPrice) {
        this.highPrice = highPrice;
    }

    public BigDecimal getLowPrice() {
        return lowPrice;
    }

    public void setLowPrice(BigDecimal lowPrice) {
        this.lowPrice = lowPrice;
    }

    public Long getVolume() {
        return volume;
    }

    public void setVolume(Long volume) {
        this.volume = volume;
    }

    public BigDecimal getBidPrice() {
        return bidPrice;
    }

    public void setBidPrice(BigDecimal bidPrice) {
        this.bidPrice = bidPrice;
    }

    public BigDecimal getAskPrice() {
        return askPrice;
    }

    public void setAskPrice(BigDecimal askPrice) {
        this.askPrice = askPrice;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    @Override
    public String toString() {
        return "StockTick{" +
                "id='" + id + '\'' +
                ", timestamp=" + timestamp +
                ", symbol='" + symbol + '\'' +
                ", lastPrice=" + lastPrice +
                ", volume=" + volume +
                ", source='" + source + '\'' +
                '}';
    }
}
