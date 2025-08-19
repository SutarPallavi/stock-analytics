#!/bin/bash

echo "Creating sample alert..."

# Create a price threshold alert for INFY
curl -X POST "http://localhost:8080/api/v1/alerts" \
  -H "Content-Type: application/json" \
  -d '{
    "accountId": "acc_demo",
    "ruleType": "PRICE_THRESHOLD",
    "symbol": "INFY",
    "operator": ">=",
    "threshold": 1800.0,
    "dedupSeconds": 60,
    "enabled": true
  }'

echo ""
echo "Sample alert created. Check alerts at http://localhost:5173/alerts"
echo "Alert will fire when INFY price >= ₹1800"
