#!/bin/bash

echo "=========================================="
echo "Stock Analytics Demo - Complete Setup"
echo "=========================================="

echo ""
echo "Waiting for services to be ready..."
sleep 10

echo ""
echo "1. Starting tick generation..."
./scripts/start-ticks.sh

echo ""
echo "2. Creating sample portfolio..."
./scripts/seed-orders.sh

echo ""
echo "3. Setting up price alerts..."
./scripts/create-alert.sh

echo ""
echo "=========================================="
echo "Demo Setup Complete!"
echo "=========================================="
echo ""
echo "Access the application:"
echo "  Dashboard: http://localhost:5173"
echo "  Portfolio: http://localhost:5173/portfolio"
echo "  Alerts: http://localhost:5173/alerts"
echo ""
echo "Monitor services:"
echo "  Redpanda Console: http://localhost:8085"
echo "  Kafka UI: http://localhost:8086"
echo "  MailHog: http://localhost:8025"
echo ""
echo "The system is now generating live stock data!"
echo "Watch for alerts when INFY price crosses ₹1800"
echo ""
echo "To stop the demo:"
echo "  ./scripts/stop-ticks.sh"
echo "  docker-compose down"
