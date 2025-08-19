#!/bin/bash

echo "Starting tick generation..."

# Start tick generation for default symbols
curl -X POST "http://localhost:8080/api/v1/generator/start?symbols=INFY,TCS,RELIANCE&rate=50" \
  -H "Content-Type: application/json"

echo ""
echo "Tick generation started. Check the dashboard at http://localhost:5173"
echo "To stop tick generation, run: ./scripts/stop-ticks.sh"
