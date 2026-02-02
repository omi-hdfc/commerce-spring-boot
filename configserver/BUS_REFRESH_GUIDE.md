# Spring Cloud Bus Refresh Guide

## Issue Resolution

The 400 Bad Request error on `/actuator/busrefresh` has been resolved by:

1. Adding explicit Spring Cloud Bus configuration in `application.yml`
2. Enabling the bus refresh endpoint in management configuration

## Updated Configuration

The following configuration has been added to `application.yml`:

```yaml
spring:
  cloud:
    bus:
      enabled: true
      refresh:
        enabled: true

management:
  endpoint:
    busrefresh:
      enabled: true
```

## How to Use Bus Refresh

### Option 1: Using POST request (Recommended)

The `/actuator/busrefresh` endpoint requires a **POST** request, not GET:

```bash
curl -X POST http://localhost:8888/actuator/busrefresh
```

### Option 2: Using the hyphenated endpoint

In Spring Boot 3.x/4.x, try the hyphenated version:

```bash
curl -X POST http://localhost:8888/actuator/bus-refresh
```

### Option 3: Target specific service

To refresh a specific service:

```bash
curl -X POST http://localhost:8888/actuator/busrefresh/user-service
```

## Prerequisites

1. **RabbitMQ must be running** on localhost:5672
2. All microservices must be connected to the same RabbitMQ instance
3. Services must have Spring Cloud Bus dependency

## Verification

After starting the config server, verify available actuator endpoints:

```bash
curl http://localhost:8888/actuator
```

This will list all available endpoints including `busrefresh` or `bus-refresh`.

## Next Steps

1. Restart the config server for changes to take effect
2. Ensure RabbitMQ is running: `docker ps` or check your RabbitMQ service
3. Use POST method (not GET) to trigger the refresh
4. Check if endpoint is available: `curl http://localhost:8888/actuator`

## Common Issues

- **400 Bad Request**: Using GET instead of POST
- **404 Not Found**: Endpoint not enabled or wrong path (try `/bus-refresh` with hyphen)
- **Connection Refused**: RabbitMQ is not running
- **503 Service Unavailable**: RabbitMQ connection failed

## Testing the Configuration

1. Start RabbitMQ (if using Docker):
   ```bash
   docker run -d --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:management
   ```

2. Start the config server:
   ```bash
   cd /Users/OMKAR_S/ecommerce-course/configserver
   ./mvnw spring-boot:run
   ```

3. Test the refresh endpoint:
   ```bash
   curl -X POST http://localhost:8888/actuator/busrefresh
   ```

Expected response: `204 No Content` (success) or similar success status.
