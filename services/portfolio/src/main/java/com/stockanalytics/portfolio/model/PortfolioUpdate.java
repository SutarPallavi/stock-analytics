package com.stockanalytics.portfolio.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public class PortfolioUpdate {

    @JsonProperty("ts")
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    private Instant timestamp;

    @JsonProperty("accountId")
    private String accountId;

    @JsonProperty("positions")
    private List<PositionSummary> positions;

    @JsonProperty("cash")
    private BigDecimal cash;

    @JsonProperty("pnl")
    private PnL pnl;

    @JsonProperty("nav")
    private BigDecimal nav;

    // Constructors
    public PortfolioUpdate() {}

    public PortfolioUpdate(Instant timestamp, String accountId, List<PositionSummary> positions,
                          BigDecimal cash, PnL pnl, BigDecimal nav) {
        this.timestamp = timestamp;
        this.accountId = accountId;
        this.positions = positions;
        this.cash = cash;
        this.pnl = pnl;
        this.nav = nav;
    }

    // Getters and Setters
    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public List<PositionSummary> getPositions() {
        return positions;
    }

    public void setPositions(List<PositionSummary> positions) {
        this.positions = positions;
    }

    public BigDecimal getCash() {
        return cash;
    }

    public void setCash(BigDecimal cash) {
        this.cash = cash;
    }

    public PnL getPnl() {
        return pnl;
    }

    public void setPnl(PnL pnl) {
        this.pnl = pnl;
    }

    public BigDecimal getNav() {
        return nav;
    }

    public void setNav(BigDecimal nav) {
        this.nav = nav;
    }

    // Position Summary inner class
    public static class PositionSummary {
        @JsonProperty("symbol")
        private String symbol;

        @JsonProperty("qty")
        private Integer quantity;

        @JsonProperty("avgPrice")
        private BigDecimal averagePrice;

        public PositionSummary() {}

        public PositionSummary(String symbol, Integer quantity, BigDecimal averagePrice) {
            this.symbol = symbol;
            this.quantity = quantity;
            this.averagePrice = averagePrice;
        }

        public String getSymbol() {
            return symbol;
        }

        public void setSymbol(String symbol) {
            this.symbol = symbol;
        }

        public Integer getQuantity() {
            return quantity;
        }

        public void setQuantity(Integer quantity) {
            this.quantity = quantity;
        }

        public BigDecimal getAveragePrice() {
            return averagePrice;
        }

        public void setAveragePrice(BigDecimal averagePrice) {
            this.averagePrice = averagePrice;
        }
    }

    // P&L inner class
    public static class PnL {
        @JsonProperty("unrealized")
        private BigDecimal unrealized;

        @JsonProperty("realized")
        private BigDecimal realized;

        public PnL() {}

        public PnL(BigDecimal unrealized, BigDecimal realized) {
            this.unrealized = unrealized;
            this.realized = realized;
        }

        public BigDecimal getUnrealized() {
            return unrealized;
        }

        public void setUnrealized(BigDecimal unrealized) {
            this.unrealized = unrealized;
        }

        public BigDecimal getRealized() {
            return realized;
        }

        public void setRealized(BigDecimal realized) {
            this.realized = realized;
        }
    }

    @Override
    public String toString() {
        return "PortfolioUpdate{" +
                "timestamp=" + timestamp +
                ", accountId='" + accountId + '\'' +
                ", positions=" + positions +
                ", cash=" + cash +
                ", pnl=" + pnl +
                ", nav=" + nav +
                '}';
    }
}
