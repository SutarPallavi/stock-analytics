package com.stockanalytics.portfolio.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.time.Instant;

@Document(collection = "orders_executed")
public class OrderExecuted {

    @Id
    private String id;

    @JsonProperty("execTs")
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    @Field("execTs")
    private Instant executionTimestamp;

    @JsonProperty("accountId")
    @Field("accountId")
    private String accountId;

    @JsonProperty("orderId")
    @Field("orderId")
    private String orderId;

    @JsonProperty("symbol")
    @Field("symbol")
    private String symbol;

    @JsonProperty("side")
    @Field("side")
    private String side; // BUY or SELL

    @JsonProperty("qty")
    @Field("qty")
    private Integer quantity;

    @JsonProperty("price")
    @Field("price")
    private BigDecimal price;

    @JsonProperty("fees")
    @Field("fees")
    private BigDecimal fees;

    // Constructors
    public OrderExecuted() {}

    public OrderExecuted(Instant executionTimestamp, String accountId, String orderId,
                        String symbol, String side, Integer quantity, BigDecimal price, BigDecimal fees) {
        this.executionTimestamp = executionTimestamp;
        this.accountId = accountId;
        this.orderId = orderId;
        this.symbol = symbol;
        this.side = side;
        this.quantity = quantity;
        this.price = price;
        this.fees = fees;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Instant getExecutionTimestamp() {
        return executionTimestamp;
    }

    public void setExecutionTimestamp(Instant executionTimestamp) {
        this.executionTimestamp = executionTimestamp;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getFees() {
        return fees;
    }

    public void setFees(BigDecimal fees) {
        this.fees = fees;
    }

    @Override
    public String toString() {
        return "OrderExecuted{" +
                "id='" + id + '\'' +
                ", executionTimestamp=" + executionTimestamp +
                ", accountId='" + accountId + '\'' +
                ", orderId='" + orderId + '\'' +
                ", symbol='" + symbol + '\'' +
                ", side='" + side + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                ", fees=" + fees +
                '}';
    }
}
