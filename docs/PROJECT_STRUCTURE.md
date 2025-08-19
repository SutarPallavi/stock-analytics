# Stock Analytics Platform - Project Structure

## Overview
This document describes the complete structure of the Stock Market Streaming Analytics platform, including all services, their responsibilities, and how they interact.

## Directory Structure
```
stock-analytics/
├── .github/workflows/          # CI/CD workflows
├── .mvn/wrapper/              # Maven wrapper files
├── docs/                      # Documentation and configurations
├── scripts/                   # Utility and demo scripts
├── services/                  # Backend microservices
│   ├── market-data/          # Stock tick generator
│   ├── analytics/            # Kafka Streams analytics
│   ├── portfolio/            # Portfolio management
│   ├── alerts/               # Alert processing
│   └── api-gateway/          # API gateway and WebSocket
├── ui/                       # React frontend
├── docker-compose.yml        # Main infrastructure
├── docker-compose.monitoring.yml # Monitoring stack
├── .env.example              # Environment configuration
├── Makefile                  # Build and run commands
├── mvnw                      # Maven wrapper (Unix)
├── mvnw.cmd                  # Maven wrapper (Windows)
├── pom.xml                   # Root Maven POM
└── README.md                 # Project overview
```

## Service Architecture

### 1. Market Data Service (`services/market-data/`)
**Port:** 8081
**Purpose:** Generates mock stock ticks and publishes them to Kafka

**Key Components:**
- `TickGeneratorService`: Generates ticks at configurable rates
- `StockTickRepository`: MongoDB repository for tick history
- `TickGeneratorController`: REST API for controlling generation

**Kafka Topics:**
- **Produces:** `stocks.ticks` (key: symbol, partitions: 8)

**Data Flow:**
1. Generates ticks for configured symbols (INFY, TCS, RELIANCE)
2. Publishes to Kafka with JSON SerDes
3. Periodically persists sample to MongoDB `ticks_hist` collection
4. Provides REST endpoints to start/stop generation

### 2. Analytics Service (`services/analytics/`)
**Port:** 8082
**Purpose:** Real-time analytics computation using Kafka Streams

**Key Components:**
- `AnalyticsTopology`: Kafka Streams topology configuration
- `EMACalculator`: Exponential Moving Average computation
- `VWAPCalculator`: Volume Weighted Average Price calculation

**Kafka Topics:**
- **Consumes:** `stocks.ticks`
- **Produces:** `analytics.metrics`

**Analytics Computed:**
- SMA(20): 20-minute Simple Moving Average (hopping window)
- EMA(12): 12-period Exponential Moving Average
- VWAP(5m): 5-minute Volume Weighted Average Price (tumbling window)

**Processing Guarantees:**
- `exactly_once_v2`
- Grace period: 5 seconds
- Suppression enabled for VWAP windows

### 3. Portfolio Service (`services/portfolio/`)
**Port:** 8083
**Purpose:** Portfolio management and P&L calculation

**Key Components:**
- `PortfolioService`: Core portfolio logic
- `AccountRepository`: MongoDB repository for accounts
- `PositionRepository`: MongoDB repository for positions
- `OrderExecutedRepository`: MongoDB repository for executed orders

**Kafka Topics:**
- **Consumes:** `orders.executed`
- **Produces:** `portfolio.updates`

**Data Models:**
- `Account`: User account with cash balance
- `Position`: Stock positions with quantity and average price
- `OrderExecuted`: Executed trade orders
- `PortfolioUpdate`: Real-time portfolio snapshot

**Features:**
- Maintains positions from executed orders
- Calculates unrealized P&L using latest prices
- Persists portfolio snapshots to MongoDB
- Optional Redis caching for performance

### 4. Alerts Service (`services/alerts/`)
**Port:** 8084
**Purpose:** Real-time alert evaluation and notification

**Key Components:**
- `AlertService`: Alert evaluation logic
- `AlertRuleRepository`: MongoDB repository for alert rules
- `AlertNotificationRepository`: MongoDB repository for alert history

**Kafka Topics:**
- **Consumes:** `stocks.ticks`
- **Produces:** `alerts.notifications`

**Alert Types:**
- `PRICE_THRESHOLD`: Price above/below threshold
- `MA_CROSS`: Moving average crossover (EMA12)
- `PCT_CHANGE_WINDOW`: Percentage change over time window

**Features:**
- Rule persistence in MongoDB
- Deduplication with configurable time windows
- Email delivery via MailHog
- WebSocket notifications through API Gateway

### 5. API Gateway (`services/api-gateway/`)
**Port:** 8080
**Purpose:** Central API gateway with WebSocket support

**Key Components:**
- `ApiController`: REST API forwarding
- `StreamingService`: Real-time data streaming
- `WebSocketConfig`: STOMP over WebSocket configuration

**Kafka Topics:**
- **Consumes:** All topics for real-time streaming
- **WebSocket:** Broadcasts to subscribed clients

**Features:**
- REST API aggregation from all services
- WebSocket streaming for real-time data
- Mock JWT authentication
- CORS configuration for local development
- Simple RBAC (USER, SUPPORT, ADMIN)

## Frontend Architecture (`ui/`)

**Technology Stack:**
- React 18 with TypeScript
- Vite for build tooling
- Tailwind CSS for styling
- React Router for navigation
- Recharts for data visualization
- Socket.IO Client for WebSocket

**Pages:**
1. **Dashboard:** Live stock ticks, charts, technical indicators
2. **Portfolio:** Positions, P&L, allocation charts
3. **Alerts:** Alert rule management
4. **Settings:** Application configuration

**Real-time Features:**
- WebSocket subscriptions for live data
- Toast notifications for alerts
- Real-time portfolio updates
- Live price charts with overlays

## Infrastructure

### Docker Compose Services
- **Redpanda:** Kafka-compatible streaming platform
- **Redpanda Console:** Web UI for Redpanda
- **Kafka UI:** Alternative Kafka management interface
- **Redis:** Caching and session storage
- **MailHog:** Local SMTP server for testing
- **MongoDB:** Commented out by default (use local)

### Monitoring Stack
- **Prometheus:** Metrics collection
- **Grafana:** Visualization and dashboards

## Data Flow

```
Market Data → Kafka → Analytics → Kafka → API Gateway → WebSocket → UI
     ↓              ↓         ↓         ↓
  MongoDB       Portfolio   Alerts   Portfolio
     ↓              ↓         ↓         ↓
  History       Updates   Notifications  Updates
```

## Configuration

### Environment Variables
- `MONGODB_URI`: MongoDB connection string
- `KAFKA_BOOTSTRAP_SERVERS`: Kafka/Redpanda servers
- `REDIS_HOST`: Redis server host
- `MAIL_HOST`: SMTP server host
- Service-specific ports and settings

### Profiles
- **Default:** Local development with host MongoDB
- **Docker:** Containerized environment

## Development Workflow

1. **Setup:** Ensure local MongoDB running
2. **Start:** `docker compose up -d --build`
3. **Generate Data:** `./scripts/start-ticks.sh`
4. **Seed Data:** `./scripts/seed-orders.sh`
5. **Create Alerts:** `./scripts/create-alert.sh`
6. **Access UI:** http://localhost:5173
7. **Monitor:** Redpanda Console (8085), Kafka UI (8086), MailHog (8025)

## Testing Strategy

- **Unit Tests:** Service logic, calculators, repositories
- **Integration Tests:** Testcontainers for Kafka + MongoDB
- **End-to-End:** Complete workflow testing
- **CI/CD:** GitHub Actions with automated testing

## Security Considerations

- Mock JWT for local development
- CORS configured for localhost
- WebSocket subscription rate limiting
- Input validation and sanitization
- MongoDB injection protection via Spring Data

## Performance Optimizations

- Kafka Streams with RocksDB state stores
- MongoDB indexes on frequently queried fields
- Optional Redis caching layer
- Async processing for non-blocking operations
- Efficient JSON serialization with Jackson

## Monitoring and Observability

- Micrometer metrics exposed via `/actuator/prometheus`
- Structured JSON logging with correlation IDs
- Health checks for all services
- Prometheus scraping configuration
- Sample Grafana dashboards
- DLQ handling for failed messages
