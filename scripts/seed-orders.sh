#!/bin/bash

echo "Seeding sample orders..."

# Create sample orders for demo account
curl -X POST "http://localhost:8080/api/v1/orders" \
  -H "Content-Type: application/json" \
  -d '{
    "accountId": "acc_demo",
    "symbol": "INFY",
    "side": "BUY",
    "quantity": 100,
    "price": 1750.0,
    "orderType": "MARKET"
  }'

echo ""
echo "Sample orders created. Check portfolio at http://localhost:5173/portfolio"
