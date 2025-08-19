# Troubleshooting Guide

## Common Issues and Solutions

### 1. MongoDB Connection Issues

#### Problem: Cannot connect to MongoDB
```
Error: Failed to connect to MongoDB: mongodb://host.docker.internal:27017/stock-analytics
```

**Solutions:**
1. **Ensure MongoDB is running locally:**
   ```bash
   # Windows
   net start MongoDB
   
   # macOS/Linux
   sudo systemctl start mongod
   # or
   brew services start mongodb-community
   ```

2. **Check MongoDB port:**
   ```bash
   netstat -an | findstr 27017  # Windows
   netstat -an | grep 27017     # macOS/Linux
   ```

3. **Use containerized MongoDB instead:**
   - Uncomment the `mongo` service in `docker-compose.yml`
   - Update `.env` file: `MONGODB_URI=mongodb://mongo:27017/stock-analytics`
   - Restart: `docker compose down && docker compose up -d`

4. **Verify MongoDB Compass connection:**
   - Use connection string: `mongodb://localhost:27017`
   - Database: `stock-analytics`

#### Problem: MongoDB collections not created
```
Error: Collection 'ticks_hist' not found
```

**Solution:**
```bash
# Run the MongoDB setup script
./scripts/mongo-setup.sh

# Or manually create collections in MongoDB Compass
```

### 2. Kafka/Redpanda Issues

#### Problem: Redpanda not starting
```
Error: Failed to start Redpanda service
```

**Solutions:**
1. **Check port conflicts:**
   ```bash
   netstat -an | findstr 9092  # Windows
   netstat -an | grep 9092     # macOS/Linux
   ```

2. **Clear Redpanda data:**
   ```bash
   docker compose down
   rm -rf ./data/redpanda
   docker compose up -d
   ```

3. **Check Docker resources:**
   - Ensure Docker has enough memory (at least 4GB)
   - Increase Docker Desktop memory allocation

#### Problem: Kafka topics not created
```
Error: Topic 'stocks.ticks' not found
```

**Solutions:**
1. **Topics are auto-created by default, but you can manually create them:**
   ```bash
   # Via Redpanda Console (http://localhost:8085)
   # Go to Topics → Create Topic
   # Name: stocks.ticks, Partitions: 8
   ```

2. **Check Redpanda Console:**
   - Open http://localhost:8085
   - Verify topics exist under Topics tab

### 3. Service Startup Issues

#### Problem: Services fail to start
```
Error: Application failed to start
```

**Solutions:**
1. **Check service logs:**
   ```bash
   docker compose logs market-data
   docker compose logs analytics
   docker compose logs portfolio
   docker compose logs alerts
   docker compose logs api-gateway
   ```

2. **Verify dependencies:**
   ```bash
   # Check if all services are healthy
   docker compose ps
   
   # Wait for infrastructure to be ready
   docker compose up -d
   sleep 30
   ```

3. **Check environment variables:**
   ```bash
   # Verify .env file exists and has correct values
   cat .env
   
   # Copy from example if missing
   cp .env.example .env
   ```

#### Problem: Port already in use
```
Error: Port 8080 is already in use
```

**Solutions:**
1. **Find process using the port:**
   ```bash
   # Windows
   netstat -ano | findstr 8080
   
   # macOS/Linux
   lsof -i :8080
   ```

2. **Kill the process or change port:**
   ```bash
   # Kill process (replace PID with actual process ID)
   taskkill /PID <PID> /F  # Windows
   kill -9 <PID>            # macOS/Linux
   
   # Or change port in docker-compose.yml
   ```

### 4. Frontend Issues

#### Problem: UI not loading
```
Error: Cannot connect to API Gateway
```

**Solutions:**
1. **Check API Gateway status:**
   ```bash
   curl http://localhost:8080/actuator/health
   ```

2. **Verify CORS configuration:**
   - Check browser console for CORS errors
   - Ensure API Gateway is running on port 8080

3. **Check UI build:**
   ```bash
   cd ui
   npm install
   npm run dev
   ```

#### Problem: WebSocket connection failed
```
Error: WebSocket connection to 'ws://localhost:8080/ws' failed
```

**Solutions:**
1. **Verify WebSocket endpoint:**
   ```bash
   curl http://localhost:8080/ws
   ```

2. **Check WebSocket configuration in API Gateway**
3. **Ensure STOMP client is properly configured**

### 5. Data Generation Issues

#### Problem: No stock ticks being generated
```
Error: No data in Kafka topics
```

**Solutions:**
1. **Start tick generation:**
   ```bash
   ./scripts/start-ticks.sh
   ```

2. **Check tick generator status:**
   ```bash
   curl http://localhost:8080/api/v1/generator/status
   ```

3. **Verify market-data service:**
   ```bash
   docker compose logs market-data
   curl http://localhost:8081/actuator/health
   ```

#### Problem: Analytics not computing
```
Error: No analytics metrics generated
```

**Solutions:**
1. **Check analytics service logs:**
   ```bash
   docker compose logs analytics
   ```

2. **Verify Kafka Streams topology:**
   ```bash
   curl http://localhost:8082/actuator/health
   ```

3. **Check RocksDB state stores:**
   - Verify analytics service has enough disk space
   - Check RocksDB configuration

### 6. Performance Issues

#### Problem: High latency in data processing
```
Error: Slow response times
```

**Solutions:**
1. **Check resource usage:**
   ```bash
   docker stats
   ```

2. **Optimize Kafka Streams:**
   - Increase RocksDB memory allocation
   - Adjust window sizes and grace periods
   - Check for backpressure

3. **Monitor MongoDB performance:**
   - Check indexes are properly created
   - Monitor query performance in Compass

#### Problem: Memory issues
```
Error: OutOfMemoryError
```

**Solutions:**
1. **Increase JVM heap size:**
   ```bash
   # In docker-compose.yml, add environment variable
   - JAVA_OPTS=-Xmx2g -Xms1g
   ```

2. **Check RocksDB memory usage:**
   - Monitor RocksDB state store sizes
   - Adjust RocksDB configuration

### 7. Monitoring Issues

#### Problem: Prometheus not scraping metrics
```
Error: No metrics visible in Grafana
```

**Solutions:**
1. **Check Prometheus configuration:**
   ```bash
   # Verify prometheus.yml is mounted correctly
   docker exec stock-analytics-prometheus cat /etc/prometheus/prometheus.yml
   ```

2. **Verify targets are up:**
   - Open http://localhost:9090/targets
   - Check target status

3. **Check service endpoints:**
   ```bash
   curl http://localhost:8080/actuator/prometheus
   curl http://localhost:8081/actuator/prometheus
   ```

#### Problem: Grafana dashboard not loading
```
Error: Dashboard panels show "No data"
```

**Solutions:**
1. **Check data source configuration:**
   - Verify Prometheus data source is configured
   - Check data source URL: `http://prometheus:9090`

2. **Verify time range:**
   - Ensure dashboard time range includes data
   - Check if metrics are being generated

### 8. Network and Connectivity

#### Problem: Services can't communicate
```
Error: Connection refused between services
```

**Solutions:**
1. **Check Docker network:**
   ```bash
   docker network ls
   docker network inspect stock-analytics_stock-analytics
   ```

2. **Verify service discovery:**
   - Ensure all services are on the same network
   - Check service names in docker-compose.yml

3. **Test connectivity:**
   ```bash
   # From one service container to another
   docker exec stock-analytics-market-data-1 ping api-gateway
   ```

### 9. Build and Compilation Issues

#### Problem: Maven build fails
```
Error: Compilation failed
```

**Solutions:**
1. **Check Java version:**
   ```bash
   java -version  # Should be Java 17
   ```

2. **Clean and rebuild:**
   ```bash
   ./mvnw clean install
   ```

3. **Check dependencies:**
   ```bash
   ./mvnw dependency:tree
   ```

4. **Verify Maven wrapper:**
   ```bash
   # Download Maven wrapper if missing
   mvn wrapper:wrapper
   ```

### 10. Script Execution Issues

#### Problem: Scripts not executable
```
Error: Permission denied
```

**Solutions:**
1. **Make scripts executable:**
   ```bash
   chmod +x scripts/*.sh
   ```

2. **Run with bash explicitly:**
   ```bash
   bash scripts/start-ticks.sh
   ```

3. **Check line endings (Windows):**
   - Ensure scripts use Unix line endings (LF, not CRLF)
   - Use Git Bash or WSL for better compatibility

## Getting Help

### Logs and Debugging
1. **Service logs:**
   ```bash
   docker compose logs -f [service-name]
   ```

2. **Application logs:**
   - Check `/logs` directory in each service
   - Look for JSON structured logs

3. **Health checks:**
   ```bash
   curl http://localhost:[port]/actuator/health
   ```

### Useful Commands
```bash
# Check all services status
docker compose ps

# View all logs
docker compose logs

# Restart specific service
docker compose restart [service-name]

# Rebuild and restart
docker compose down
docker compose up -d --build

# Check resource usage
docker stats

# Access service shell
docker exec -it [container-name] /bin/bash
```

### Common Ports
- **API Gateway:** 8080
- **Market Data:** 8081
- **Analytics:** 8082
- **Portfolio:** 8083
- **Alerts:** 8084
- **UI:** 5173
- **Redpanda Console:** 8085
- **Kafka UI:** 8086
- **MailHog:** 8025
- **Prometheus:** 9090
- **Grafana:** 3000

### Environment Variables
- **MONGODB_URI:** MongoDB connection string
- **KAFKA_BOOTSTRAP_SERVERS:** Kafka servers
- **REDIS_HOST:** Redis server
- **MAIL_HOST:** SMTP server
- **JWT_SECRET:** JWT signing secret
