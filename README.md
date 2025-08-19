# Stock Market Streaming Analytics

A real-time stock market analytics platform built with Spring Boot, Kafka Streams, and React. This project demonstrates a complete streaming analytics pipeline for stock market data with live portfolio monitoring and alerting capabilities.

## 🚀 Features

- **Real-time Market Data**: Live stock tick generation and streaming
- **Technical Indicators**: SMA, EMA, VWAP calculations using Kafka Streams
- **Portfolio Management**: Real-time P&L calculation and position tracking
- **Smart Alerts**: Configurable price and technical indicator alerts
- **Modern UI**: React-based dashboard with real-time charts
- **Scalable Architecture**: Microservices with event-driven design

## 🏗️ Architecture

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Market Data   │───▶│    Analytics    │───▶│   Portfolio     │
│   Service       │    │   Service       │    │   Service       │
└─────────────────┘    └─────────────────┘    └─────────────────┘
         │                       │                       │
         ▼                       ▼                       ▼
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Kafka/        │    │   MongoDB       │    │   Redis Cache   │
│   Redpanda      │    │   Collections   │    │   (Optional)    │
└─────────────────┘    └─────────────────┘    └─────────────────┘
         │                       │                       │
         ▼                       ▼                       ▼
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   API Gateway   │    │   Alerts        │    │   React UI      │
│   + WebSocket   │    │   Service       │    │   Dashboard     │
└─────────────────┘    └─────────────────┘    └─────────────────┘
```

## 🛠️ Technology Stack

### Backend
- **Java 17** with Spring Boot 3.x
- **Kafka Streams** for real-time analytics
- **MongoDB** for data persistence
- **Redis** for caching (optional)
- **Redpanda** (Kafka-compatible) for messaging

### Frontend
- **React 18** with TypeScript
- **Vite** for fast development
- **Tailwind CSS** for styling
- **Recharts** for data visualization
- **WebSocket** for real-time updates

### Infrastructure
- **Docker Compose** for local development
- **Health checks** and service dependencies
- **Prometheus metrics** and monitoring

## 📋 Prerequisites

- **Java 17** (OpenJDK or Oracle JDK)
- **Maven 3.8+**
- **Docker & Docker Compose**
- **MongoDB** (local installation or Docker)
- **Node.js 18+** (for UI development)

## 🚀 Quick Start

### 1. Clone and Setup

```bash
git clone <repository-url>
cd stock-analytics
```

### 2. MongoDB Setup

**Option A: Local MongoDB (Recommended for development)**
```bash
# Install MongoDB locally or use MongoDB Atlas
# Ensure MongoDB is running on port 27017
# Run the setup script
chmod +x scripts/mongo-setup.sh
./scripts/mongo-setup.sh
```

**Option B: Containerized MongoDB**
```bash
# Uncomment the mongo service in docker-compose.yml
# Update MONGODB_URI in .env to: mongodb://mongo:27017/stock-analytics
```

### 3. Start Infrastructure

```bash
# Start all services
make up
# or
docker-compose up -d --build
```

### 4. Run Demo

```bash
# Wait for services to be ready (about 30 seconds)
# Then run the complete demo
./scripts/demo.sh
```

### 5. Access the Application

- **Dashboard**: http://localhost:5173
- **API Gateway**: http://localhost:8080
- **Redpanda Console**: http://localhost:8085
- **Kafka UI**: http://localhost:8086
- **MailHog**: http://localhost:8025

## 📊 Service Ports

| Service | Port | Description |
|---------|------|-------------|
| API Gateway | 8080 | Main REST API and WebSocket |
| Market Data | 8081 | Tick generation service |
| Analytics | 8082 | Technical indicators service |
| Portfolio | 8083 | Portfolio management service |
| Alerts | 8084 | Alert management service |
| UI | 5173 | React dashboard |
| Redpanda | 9092 | Kafka-compatible messaging |
| Redpanda Console | 8085 | Kafka topic browser |
| Kafka UI | 8086 | Alternative Kafka UI |
| MailHog | 8025 | Email testing interface |

## 🔧 Development

### Building Services

```bash
# Build all services
make build

# Build specific service
make build-market-data
make build-analytics
make build-portfolio
make build-alerts
make build-api-gateway
```

### Running Tests

```bash
# Run all tests
make test

# Format code
make spotless
```

### Service Management

```bash
# Start services
make up

# Stop services
make down

# View logs
make logs
make logs-redpanda
make logs-market-data

# Health check
make health
```

## 📈 Demo Scenarios

### 1. Live Market Data
- Start tick generation for INFY, TCS, RELIANCE
- Watch real-time price updates in the dashboard
- Monitor Kafka topics via Redpanda Console

### 2. Portfolio Management
- Create sample buy orders for demo account
- View real-time portfolio P&L calculations
- Track position changes and cash balance

### 3. Technical Analysis
- Real-time SMA(20), EMA(12), VWAP(5m) calculations
- Chart overlays with technical indicators
- Historical data persistence in MongoDB

### 4. Alert System
- Set price threshold alerts (e.g., INFY >= ₹1800)
- Receive email notifications via MailHog
- View alert history and rule management

## 🗄️ Data Models

### Stock Tick
```json
{
  "ts": 1734500000000,
  "symbol": "INFY",
  "last": 1799.5,
  "open": 1760,
  "high": 1805,
  "low": 1755,
  "volume": 245000,
  "bid": 1799.45,
  "ask": 1799.55,
  "source": "MOCK"
}
```

### Analytics Metric
```json
{
  "ts": 1734500001000,
  "symbol": "INFY",
  "sma20": 1788.12,
  "ema12": 1790.45,
  "vwap_5m": 1786.01
}
```

### Portfolio Update
```json
{
  "ts": 1734500005200,
  "accountId": "acc_demo",
  "positions": [{"symbol":"INFY","qty":10,"avgPrice":1780.0}],
  "cash": 100000.0,
  "pnl": {"unrealized": 195.0, "realized": 0.0},
  "nav": 101195.0
}
```

## 🔍 Monitoring & Debugging

### Health Checks
```bash
# Check all services
curl http://localhost:8080/actuator/health
curl http://localhost:8081/actuator/health
curl http://localhost:8082/actuator/health
curl http://localhost:8083/actuator/health
curl http://localhost:8084/actuator/health
```

### Kafka Topics
- `stocks.ticks` - Raw stock tick data
- `analytics.metrics` - Calculated technical indicators
- `orders.executed` - Executed order events
- `portfolio.updates` - Portfolio state changes
- `alerts.notifications` - Alert events

### MongoDB Collections
- `ticks_hist` - Time series stock data
- `analytics_snap` - Technical indicator snapshots
- `accounts` - User account information
- `positions` - Stock positions
- `orders_executed` - Order history
- `alerts` - Alert rules
- `alerts_history` - Alert notifications

## 🚨 Troubleshooting

### Common Issues

**Services not starting**
```bash
# Check Docker logs
docker-compose logs -f

# Verify MongoDB connection
# Ensure MongoDB is running and accessible
# Check MONGODB_URI in .env file
```

**Kafka connection issues**
```bash
# Wait for Redpanda to be healthy
docker-compose logs redpanda

# Check topic creation
# Verify bootstrap servers configuration
```

**UI not loading**
```bash
# Check if UI container is running
docker ps | grep ui

# Verify API Gateway connectivity
curl http://localhost:8080/api/v1/health
```

### Port Conflicts
If you encounter port conflicts, update the ports in `docker-compose.yml` or stop conflicting services.

### MongoDB Connection
- **Windows/Mac**: Use `host.docker.internal` in MONGODB_URI
- **Linux**: Use `172.17.0.1` or your host IP
- **Containerized**: Use `mongo` service name

## 📚 API Documentation

### Market Data
- `POST /api/v1/generator/start` - Start tick generation
- `POST /api/v1/generator/stop` - Stop tick generation
- `GET /api/v1/generator/status` - Get generator status
- `GET /api/v1/generator/ticks/{symbol}` - Get tick history

### Portfolio
- `GET /api/v1/portfolio/{accountId}` - Get portfolio
- `POST /api/v1/portfolio/{accountId}/refresh` - Refresh portfolio

### Alerts
- `GET /api/v1/alerts/{accountId}` - Get alert rules
- `POST /api/v1/alerts` - Create alert rule
- `PUT /api/v1/alerts/{ruleId}/toggle` - Toggle alert
- `DELETE /api/v1/alerts/{ruleId}` - Delete alert

### WebSocket
- Connect to `/ws` for real-time updates
- Subscribe to `/topic/ticks/{symbol}` for tick data
- Subscribe to `/topic/metrics/{symbol}` for analytics
- Subscribe to `/queue/portfolio` for portfolio updates
- Subscribe to `/queue/alerts` for alert notifications

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests if applicable
5. Submit a pull request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgments

- Spring Boot team for the excellent framework
- Apache Kafka for streaming capabilities
- Redpanda for Kafka-compatible messaging
- React team for the UI framework
- Tailwind CSS for the utility-first CSS framework

---

**Happy Trading! 📈**
