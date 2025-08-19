.PHONY: help build up down logs clean test spotless

# Default target
help:
	@echo "Stock Market Streaming Analytics - Available Commands:"
	@echo ""
	@echo "Infrastructure:"
	@echo "  up          - Start all services with docker-compose"
	@echo "  down        - Stop all services"
	@echo "  logs        - Show logs for all services"
	@echo "  logs-<svc>  - Show logs for specific service (e.g., logs-redpanda)"
	@echo ""
	@echo "Development:"
	@echo "  build       - Build all services"
	@echo "  build-<svc> - Build specific service (e.g., build-market-data)"
	@echo "  test        - Run all tests"
	@echo "  spotless    - Format code with Spotless"
	@echo ""
	@echo "Data & Demo:"
	@echo "  start-ticks - Start tick generation"
	@echo "  seed-orders - Seed sample orders"
	@echo "  create-alert - Create sample alert"
	@echo "  demo        - Run complete demo sequence"
	@echo ""
	@echo "Maintenance:"
	@echo "  clean       - Clean all build artifacts and containers"
	@echo "  reset       - Reset everything (clean + down + up)"

# Infrastructure commands
up:
	@echo "Starting Stock Analytics infrastructure..."
	docker-compose up -d --build
	@echo "Waiting for services to be ready..."
	@echo "Services will be available at:"
	@echo "  API Gateway: http://localhost:8080"
	@echo "  UI: http://localhost:5173"
	@echo "  Redpanda Console: http://localhost:8085"
	@echo "  Kafka UI: http://localhost:8086"
	@echo "  MailHog: http://localhost:8025"

down:
	@echo "Stopping Stock Analytics infrastructure..."
	docker-compose down

logs:
	@echo "Showing logs for all services..."
	docker-compose logs -f

logs-%:
	@echo "Showing logs for $* service..."
	docker-compose logs -f $*

# Build commands
build:
	@echo "Building all services..."
	mvn clean install -DskipTests

build-%:
	@echo "Building $* service..."
	mvn clean install -pl services/$* -am -DskipTests

# Test commands
test:
	@echo "Running all tests..."
	mvn test

# Code quality
spotless:
	@echo "Formatting code with Spotless..."
	mvn spotless:apply

# Demo commands
start-ticks:
	@echo "Starting tick generation..."
	@./scripts/start-ticks.sh

seed-orders:
	@echo "Seeding sample orders..."
	@./scripts/seed-orders.sh

create-alert:
	@echo "Creating sample alert..."
	@./scripts/create-alert.sh

demo: up
	@echo "Running complete demo sequence..."
	@echo "Waiting for services to be ready..."
	@sleep 30
	@./scripts/demo.sh

# Maintenance commands
clean:
	@echo "Cleaning build artifacts..."
	mvn clean
	@echo "Cleaning Docker containers and images..."
	docker-compose down --rmi all --volumes --remove-orphans
	@echo "Cleaning completed"

reset: clean down up
	@echo "Reset completed"

# Health checks
health:
	@echo "Checking service health..."
	@curl -s http://localhost:8080/actuator/health || echo "API Gateway not ready"
	@curl -s http://localhost:8081/actuator/health || echo "Market Data not ready"
	@curl -s http://localhost:8082/actuator/health || echo "Analytics not ready"
	@curl -s http://localhost:8083/actuator/health || echo "Portfolio not ready"
	@curl -s http://localhost:8084/actuator/health || echo "Alerts not ready"
	@curl -s http://localhost:5173 || echo "UI not ready"

# MongoDB setup (if using local MongoDB)
mongo-setup:
	@echo "Setting up MongoDB collections and indexes..."
	@./scripts/mongo-setup.sh

# Quick start for development
dev: up
	@echo "Development environment ready!"
	@echo "Run 'make start-ticks' to begin tick generation"
	@echo "Run 'make seed-orders' to create sample portfolio"
	@echo "Run 'make create-alert' to set up price alerts"
