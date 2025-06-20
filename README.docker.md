# Docker Setup for Virtual Game Web App

This document provides instructions for setting up and running the Virtual Game Web App using Docker and Docker Compose.

## Prerequisites

- [Docker](https://docs.docker.com/get-docker/) (version 20.10.0 or higher)
- [Docker Compose](https://docs.docker.com/compose/install/) (version 2.0.0 or higher)

## Services

The Docker Compose configuration includes the following services:

1. **app** - The Spring Boot application
2. **postgres** - PostgreSQL database
3. **redis** - Redis for caching and session management
4. **zookeeper** - Required for Kafka
5. **kafka** - Kafka for event messaging
6. **kafka-ui** - Web UI for monitoring Kafka (optional)

## Getting Started

### Building and Starting the Services

To build and start all services:

```bash
# Build and start all services in detached mode
docker-compose up -d

# View logs
docker-compose logs -f

# View logs for a specific service
docker-compose logs -f app
```

### Stopping the Services

To stop all services:

```bash
# Stop all services but keep containers
docker-compose stop

# Stop and remove containers, networks, and volumes
docker-compose down

# Stop and remove containers, networks, volumes, and images
docker-compose down --rmi all
```

## Accessing the Services

- **Application**: http://localhost:8080
- **Kafka UI**: http://localhost:8090
- **PostgreSQL**: localhost:5432 (Use a database client like pgAdmin or DBeaver)
- **Redis**: localhost:6379 (Use a Redis client like RedisInsight)

## Development Workflow

### Hot Reloading

The application service is configured with a volume mount for the `src` directory, which enables hot reloading during development. When you make changes to the source code, the application will automatically reload.

### Running Tests

To run tests inside the container:

```bash
docker-compose exec app ./mvnw test
```

### Accessing Logs

```bash
# View logs for all services
docker-compose logs -f

# View logs for a specific service
docker-compose logs -f app
docker-compose logs -f postgres
```

## Troubleshooting

### Database Connection Issues

If the application cannot connect to the database:

1. Check if the PostgreSQL container is running:
   ```bash
   docker-compose ps postgres
   ```

2. Check PostgreSQL logs:
   ```bash
   docker-compose logs postgres
   ```

3. Ensure the database is initialized:
   ```bash
   docker-compose exec postgres psql -U postgres -c "\l"
   ```

### Redis Connection Issues

If the application cannot connect to Redis:

1. Check if the Redis container is running:
   ```bash
   docker-compose ps redis
   ```

2. Check Redis logs:
   ```bash
   docker-compose logs redis
   ```

3. Test Redis connection:
   ```bash
   docker-compose exec redis redis-cli ping
   ```

### Kafka Connection Issues

If the application cannot connect to Kafka:

1. Check if Kafka and Zookeeper containers are running:
   ```bash
   docker-compose ps kafka zookeeper
   ```

2. Check Kafka logs:
   ```bash
   docker-compose logs kafka
   ```

3. List Kafka topics:
   ```bash
   docker-compose exec kafka kafka-topics --list --bootstrap-server kafka:9092
   ```

## Environment Variables

You can customize the Docker environment by setting environment variables in the `.env` file or by passing them to the `docker-compose up` command:

```bash
POSTGRES_PASSWORD=mysecretpassword docker-compose up -d
```

## Data Persistence

The Docker Compose configuration uses named volumes to persist data:

- **postgres-data**: PostgreSQL data
- **redis-data**: Redis data
- **zookeeper-data** and **zookeeper-logs**: Zookeeper data
- **kafka-data**: Kafka data

These volumes persist even when containers are removed, ensuring that your data is not lost between restarts.

To remove all volumes and start fresh:

```bash
docker-compose down -v
```
