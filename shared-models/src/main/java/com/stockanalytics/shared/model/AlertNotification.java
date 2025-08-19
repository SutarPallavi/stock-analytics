package com.stockanalytics.shared.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.util.Map;

public class AlertNotification {

    @JsonProperty("id")
    private String id;

    @JsonProperty("accountId")
    private String accountId;

    @JsonProperty("ruleId")
    private String ruleId;

    @JsonProperty("firedTs")
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    private Instant firedTs;

    @JsonProperty("payload")
    private Map<String, Object> payload;

    @JsonProperty("channel")
    private String channel;

    // Constructors
    public AlertNotification() {}

    public AlertNotification(String id, String accountId, String ruleId, 
                           Instant firedTs, Map<String, Object> payload, String channel) {
        this.id = id;
        this.accountId = accountId;
        this.ruleId = ruleId;
        this.firedTs = firedTs;
        this.payload = payload;
        this.channel = channel;
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

    public String getRuleId() {
        return ruleId;
    }

    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }

    public Instant getFiredTs() {
        return firedTs;
    }

    public void setFiredTs(Instant firedTs) {
        this.firedTs = firedTs;
    }

    public Map<String, Object> getPayload() {
        return payload;
    }

    public void setPayload(Map<String, Object> payload) {
        this.payload = payload;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    @Override
    public String toString() {
        return "AlertNotification{" +
                "id='" + id + '\'' +
                ", accountId='" + accountId + '\'' +
                ", ruleId='" + ruleId + '\'' +
                ", firedTs=" + firedTs +
                ", payload=" + payload +
                ", channel='" + channel + '\'' +
                '}';
    }
}
