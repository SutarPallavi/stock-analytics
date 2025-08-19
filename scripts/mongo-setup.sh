#!/bin/bash

echo "Setting up MongoDB collections and indexes..."

# Connect to MongoDB and create collections with proper indexes
mongosh "mongodb://localhost:27017/stock-analytics" --eval '
// Create collections with proper indexes

// Users collection
db.createCollection("users");
db.users.createIndex({ "email": 1 }, { unique: true });

// Accounts collection
db.createCollection("accounts");
db.accounts.createIndex({ "userId": 1 });

// Positions collection
db.createCollection("positions");
db.positions.createIndex({ "accountId": 1, "symbol": 1 });

// Orders executed collection
db.createCollection("orders_executed");
db.orders_executed.createIndex({ "accountId": 1, "execTs": 1 });

// Ticks history collection (time series)
db.createCollection("ticks_hist", {
  timeseries: {
    timeField: "ts",
    metaField: "symbol",
    granularity: "seconds"
  }
});
db.ticks_hist.createIndex({ "symbol": 1, "ts": -1 });

// Analytics snapshots collection
db.createCollection("analytics_snap");
db.analytics_snap.createIndex({ "ts": 1 }, { expireAfterSeconds: 604800 }); // 7 days TTL

// Alerts collection
db.createCollection("alerts");
db.alerts.createIndex({ "accountId": 1, "symbol": 1, "ruleType": 1 });

// Alerts history collection
db.createCollection("alerts_history");
db.alerts_history.createIndex({ "alertId": 1, "firedTs": -1 });

print("MongoDB setup completed successfully!");
print("Collections and indexes created:");
print("- users (email unique index)");
print("- accounts (userId index)");
print("- positions (accountId + symbol compound index)");
print("- orders_executed (accountId + execTs compound index)");
print("- ticks_hist (time series with symbol + ts index)");
print("- analytics_snap (with 7-day TTL)");
print("- alerts (accountId + symbol + ruleType compound index)");
print("- alerts_history (alertId + firedTs compound index)");
'
