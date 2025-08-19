package com.stockanalytics.alerts.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;

@Document(collection = "alerts")
public class AlertRule {

    @Id
    private String id;

    @JsonProperty("accountId")
    @Field("accountId")
    private String accountId;

    @JsonProperty("ruleType")
    @Field("ruleType")
    private String ruleType; // PRICE_THRESHOLD, MA_CROSS, PCT_CHANGE_WINDOW

    @JsonProperty("symbol")
    @Field("symbol")
    private String symbol;

    @JsonProperty("op")
    @Field("op")
    private String operator; // >=, <=, >, <, ==

    @JsonProperty("threshold")
    @Field("threshold")
    private Double threshold;

    @JsonProperty("window")
    @Field("window")
    private Integer window; // For moving average crossovers

    @JsonProperty("dedupSec")
    @Field("dedupSec")
    private Integer dedupSeconds;

    @JsonProperty("enabled")
    @Field("enabled")
    private Boolean enabled;

    @JsonProperty("createdAt")
    @Field("createdAt")
    private Instant createdAt;

    // Constructors
    public AlertRule() {}

    public AlertRule(String accountId, String ruleType, String symbol, String operator,
                     Double threshold, Integer window, Integer dedupSeconds) {
        this.accountId = accountId;
        this.ruleType = ruleType;
        this.symbol = symbol;
        this.operator = operator;
        this.threshold = threshold;
        this.window = window;
        this.dedupSeconds = dedupSeconds;
        this.enabled = true;
        this.createdAt = Instant.now();
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getRuleType() {
        return ruleType;
    }

    public void setRuleType(String ruleType) {
        this.ruleType = ruleType;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Double getThreshold() {
        return threshold;
    }

    public void setThreshold(Double threshold) {
        this.threshold = threshold;
    }

    public Integer getWindow() {
        return window;
    }

    public void setWindow(Integer window) {
        this.window = window;
    }

    public Integer getDedupSeconds() {
        return dedupSeconds;
    }

    public void setDedupSeconds(Integer dedupSeconds) {
        this.dedupSeconds = dedupSeconds;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "AlertRule{" +
                "id='" + id + '\'' +
                ", accountId='" + accountId + '\'' +
                ", ruleType='" + ruleType + '\'' +
                ", symbol='" + symbol + '\'' +
                ", operator='" + operator + '\'' +
                ", threshold=" + threshold +
                ", window=" + window +
                ", dedupSeconds=" + dedupSeconds +
                ", enabled=" + enabled +
                ", createdAt=" + createdAt +
                '}';
    }
}
