# Virtual Game Web App Development Guidelines

## Build & Configuration

### Prerequisites
- Java 21 JDK
- Maven 3.9+
- PostgreSQL 15.13
- Redis 8.0
- Kafka 3.6

### Build Commands
```bash
# Clean and compile
mvn clean compile

# Run tests
mvn test

# Build WAR package
mvn clean package

# Run application
mvn spring-boot:run
```

### Project Structure
- **Java Version**: 21 with Spring Boot 3.2.0
- **Packaging**: WAR deployment
- **Architecture**: Layered architecture with Spring MVC, JPA, Security
- **Database**: PostgreSQL with Flyway migrations
- **Caching**: Redis with Redisson
- **Messaging**: Kafka for event processing
- **Monitoring**: Micrometer with Prometheus integration

## Testing Framework & Guidelines

### Testing Stack
- **JUnit 5** for unit testing
- **Mockito** for mocking
- **Spring Boot Test** for integration testing
- **Testcontainers** for external dependencies (PostgreSQL, Kafka)

### Running Tests
```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=ChipServiceTest

# Run with coverage (when configured)
mvn test jacoco:report
```

### Test Example (Working)
Unit test pattern for service layer:
```java
@ExtendWith(MockitoExtension.class)
public class ChipServiceTest {
    @Mock
    private UserRepository userRepository;
    
    @Mock
    private TransactionRepository transactionRepository;
    
    @InjectMocks
    private ChipServiceImpl chipService;
    
    @BeforeEach
    void setUp() {
        // Use lenient stubbing to avoid strict stubbing issues
        Mockito.lenient().when(userRepository.findById(any())).thenReturn(Optional.of(testUser));
        // Mock all repository methods called in service
    }
    
    @Test
    void getUserBalance_ShouldReturnCorrectBalance() {
        // Act
        ChipBalanceDto result = chipService.getUserBalance(userId);
        
        // Assert
        assertNotNull(result);
        assertEquals(1000L, result.getChipsBalance());
    }
}
```

## Code Style & Development Patterns

### Code Conventions
- **Lombok**: Extensive use of `@Data`, `@Builder`, `@Slf4j` annotations
- **Package Structure**: Feature-based organization (controller, service, repository per domain)
- **Naming**: CamelCase for classes, camelCase for methods/variables
- **DTOs**: Separate DTOs for API requests/responses with validation

### Architecture Patterns
- **Service Layer**: Interface-based services with implementation classes
- **Repository Pattern**: Spring Data JPA repositories with custom queries
- **Event-Driven**: Kafka events for cross-cutting concerns (chip transactions, game events)
- **Caching**: Redis for session management and game state caching
- **Security**: JWT-based authentication with Spring Security

### Key Configuration Classes
- `SecurityConfig.java`: Security configuration with JWT filter
- `WebSocketConfig.java`: WebSocket configuration for real-time features
- `KafkaConfig.java`: Kafka producer/consumer configuration
- `RedissonConfig.java`: Redis client configuration

### Entity Design
- UUID primary keys for all entities
- Audit fields with `@CreatedDate` and `@LastModifiedDate`
- Builder pattern with Lombok for entity creation
- JPA validation annotations for data integrity

### Exception Handling
- Global exception handler with `@ControllerAdvice`
- Custom business exceptions (e.g., `InsufficientChipsException`)
- Consistent API response format via `ApiResponse<T>` wrapper

### Observability
- Micrometer tracing with Zipkin integration
- Prometheus metrics for monitoring
- Structured logging with SLF4J

## Development Commands

### Database Management
```bash
# Run Flyway migrations
mvn flyway:migrate

# Validate migrations
mvn flyway:validate

# Clean database (dev only)
mvn flyway:clean
```

### Docker Development
```bash
# Build and run with Docker Compose
docker-compose up -d

# View logs
docker-compose logs -f virtual-game-web-app
```

### Common Issues

1. **Compilation Warnings**: Lombok `@Builder` warnings about initializing expressions can be ignored or fixed by adding `@Builder.Default`

2. **Test Context Loading**: Integration tests may fail if external dependencies (Redis, PostgreSQL) are not running

3. **JWT Configuration**: Ensure JWT secret is properly configured in application properties for different environments

### Security Considerations
- Rate limiting configured via bucket4j
- CSRF protection enabled
- JWT token blacklisting for logout
- Input validation on all API endpoints
- SQL injection prevention via JPA parameterized queries
