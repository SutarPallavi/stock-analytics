package com.stockanalytics.shared.model;

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
    private List<Position> positions;

    @JsonProperty("cash")
    private BigDecimal cash;

    @JsonProperty("pnl")
    private PnL pnl;

    @JsonProperty("nav")
    private BigDecimal nav;

    // Constructors
    public PortfolioUpdate() {}

    public PortfolioUpdate(Instant timestamp, String accountId, List<Position> positions, 
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

    public List<Position> getPositions() {
        return positions;
    }

    public void setPositions(List<Position> positions) {
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

    // Inner classes
    public static class Position {
        private String symbol;
        private Long qty;
        private BigDecimal avgPrice;

        public Position() {}

        public Position(String symbol, Long qty, BigDecimal avgPrice) {
            this.symbol = symbol;
            this.qty = qty;
            this.avgPrice = avgPrice;
        }

        public String getSymbol() { return symbol; }
        public void setSymbol(String symbol) { this.symbol = symbol; }

        public Long getQty() { return qty; }
        public void setQty(Long qty) { this.qty = qty; }

        public BigDecimal getAvgPrice() { return avgPrice; }
        public void setAvgPrice(BigDecimal avgPrice) { this.avgPrice = avgPrice; }
    }

    public static class PnL {
        private BigDecimal unrealized;
        private BigDecimal realized;

        public PnL() {}

        public PnL(BigDecimal unrealized, BigDecimal realized) {
            this.unrealized = unrealized;
            this.realized = realized;
        }

        public BigDecimal getUnrealized() { return unrealized; }
        public void setUnrealized(BigDecimal unrealized) { this.unrealized = unrealized; }

        public BigDecimal getRealized() { return realized; }
        public void setRealized(BigDecimal realized) { this.realized = realized; }
    }
}
