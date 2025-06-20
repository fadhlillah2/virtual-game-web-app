# Database Connection Pooling Optimization

This document describes the database connection pooling optimizations implemented for the Virtual Game Web App.

## Overview

Connection pooling is a technique used to improve performance by maintaining a pool of database connections that can be reused, rather than creating a new connection each time one is needed. The Virtual Game Web App uses HikariCP, a high-performance JDBC connection pool, for database connection management.

## Optimizations Implemented

The following optimizations have been implemented for each environment:

### Development Environment (`application-dev.properties`)

```properties
# HikariCP Connection Pool Optimization
spring.datasource.hikari.maximum-pool-size=10
spring.datasource.hikari.minimum-idle=5
spring.datasource.hikari.idle-timeout=300000
spring.datasource.hikari.connection-timeout=10000
spring.datasource.hikari.max-lifetime=1200000
spring.datasource.hikari.auto-commit=true
spring.datasource.hikari.validation-timeout=5000
spring.datasource.hikari.leak-detection-threshold=60000
```

### Test Environment (`application-test.properties`)

```properties
# HikariCP Connection Pool Optimization for Tests
spring.datasource.hikari.maximum-pool-size=5
spring.datasource.hikari.minimum-idle=2
spring.datasource.hikari.idle-timeout=60000
spring.datasource.hikari.connection-timeout=5000
spring.datasource.hikari.max-lifetime=600000
spring.datasource.hikari.auto-commit=true
spring.datasource.hikari.validation-timeout=3000
```

### Production Environment (`application-prod.properties`)

```properties
# HikariCP Connection Pool Optimization for Production
spring.datasource.hikari.maximum-pool-size=50
spring.datasource.hikari.minimum-idle=15
spring.datasource.hikari.idle-timeout=300000
spring.datasource.hikari.connection-timeout=30000
spring.datasource.hikari.max-lifetime=1800000
spring.datasource.hikari.auto-commit=true
spring.datasource.hikari.validation-timeout=5000
spring.datasource.hikari.leak-detection-threshold=60000
spring.datasource.hikari.register-mbeans=true
spring.datasource.hikari.pool-name=VirtualCasinoHikariCP-Prod
```

## Configuration Parameters Explained

| Parameter | Description | Development | Test | Production |
|-----------|-------------|-------------|------|------------|
| `maximum-pool-size` | Maximum number of connections in the pool | 10 | 5 | 50 |
| `minimum-idle` | Minimum number of idle connections maintained in the pool | 5 | 2 | 15 |
| `idle-timeout` | Maximum time (ms) a connection can sit idle in the pool | 300000 (5 min) | 60000 (1 min) | 300000 (5 min) |
| `connection-timeout` | Maximum time (ms) to wait for a connection from the pool | 10000 (10 sec) | 5000 (5 sec) | 30000 (30 sec) |
| `max-lifetime` | Maximum lifetime (ms) of a connection in the pool | 1200000 (20 min) | 600000 (10 min) | 1800000 (30 min) |
| `auto-commit` | Whether connections are auto-commit by default | true | true | true |
| `validation-timeout` | Maximum time (ms) to validate a connection | 5000 (5 sec) | 3000 (3 sec) | 5000 (5 sec) |
| `leak-detection-threshold` | Time (ms) after which a connection is considered leaked | 60000 (1 min) | - | 60000 (1 min) |
| `register-mbeans` | Whether to register JMX MBeans for monitoring | - | - | true |
| `pool-name` | Name of the connection pool | - | - | VirtualCasinoHikariCP-Prod |

## Rationale for Environment-Specific Settings

### Development Environment
- Moderate pool size (10) to balance resource usage and development performance
- Shorter connection lifetime (20 minutes) to detect connection issues during development
- Leak detection enabled to identify connection leaks during development

### Test Environment
- Smaller pool size (5) to minimize resource usage during tests
- Very short connection lifetime (10 minutes) as tests are typically short-lived
- Faster timeouts to speed up test execution

### Production Environment
- Large pool size (50) to handle high concurrency
- Longer connection lifetime (30 minutes) for better performance under sustained load
- JMX monitoring enabled for operational visibility
- Custom pool name for easier identification in monitoring tools

## Monitoring and Maintenance

To monitor the connection pool performance:

1. In production, use JMX monitoring tools to track pool metrics
2. Watch for connection timeouts in logs
3. Monitor the `active-connections` metric to ensure the pool size is appropriate
4. Adjust settings based on observed usage patterns and performance metrics

## Further Optimization Opportunities

1. Implement connection pool sizing based on CPU cores: `connectionPool.maxSize = (cores * 2) + effective_spindle_count`
2. Add health checks to verify database connectivity
3. Implement circuit breaker pattern for database operations
4. Consider read/write connection separation for high-load scenarios
