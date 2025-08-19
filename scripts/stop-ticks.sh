#!/bin/bash

echo "Stopping tick generation..."

# Stop tick generation
curl -X POST "http://localhost:8080/api/v1/generator/stop" \
  -H "Content-Type: application/json"

echo ""
echo "Tick generation stopped."
