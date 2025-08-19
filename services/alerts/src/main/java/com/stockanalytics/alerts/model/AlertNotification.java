package com.stockanalytics.alerts.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.util.Map;

@Document(collection = "alerts_history")
public class AlertNotification {

    @Id
    private String id;

    @JsonProperty("accountId")
    @Field("accountId")
    private String accountId;

    @JsonProperty("ruleId")
    @Field("ruleId")
    private String ruleId;

    @JsonProperty("firedTs")
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    @Field("firedTs")
    private Instant firedTimestamp;

    @JsonProperty("payload")
    @Field("payload")
    private Map<String, Object> payload;

    @JsonProperty("channel")
    @Field("channel")
    private String channel; // EMAIL, WS, SMS

    @JsonProperty("status")
    @Field("status")
    private String status; // SENT, FAILED, PENDING

    // Constructors
    public AlertNotification() {}

    public AlertNotification(String accountId, String ruleId, Instant firedTimestamp,
                           Map<String, Object> payload, String channel) {
        this.accountId = accountId;
        this.ruleId = ruleId;
        this.firedTimestamp = firedTimestamp;
        this.payload = payload;
        this.channel = channel;
        this.status = "PENDING";
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

    public Instant getFiredTimestamp() {
        return firedTimestamp;
    }

    public void setFiredTimestamp(Instant firedTimestamp) {
        this.firedTimestamp = firedTimestamp;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "AlertNotification{" +
                "id='" + id + '\'' +
                ", accountId='" + accountId + '\'' +
                ", ruleId='" + ruleId + '\'' +
                ", firedTimestamp=" + firedTimestamp +
                ", payload=" + payload +
                ", channel='" + channel + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
