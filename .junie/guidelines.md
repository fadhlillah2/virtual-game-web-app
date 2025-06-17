# Virtual Game Web App - Developer Guidelines

This document provides essential information for developers working on the Virtual Game Web App project. It covers build configuration, testing, and development guidelines specific to this project.

## Build and Configuration

### Prerequisites

- Java 21 (required as specified in pom.xml)
- Maven 3.8+ (for build management)
- PostgreSQL 14+ (for database)
- Redis (for caching and session management)
- Kafka (for event messaging)

### Database Setup

1. Create a PostgreSQL database named `virtual_casino`:
   ```sql
   CREATE DATABASE virtual_casino;
   ```

2. Configure database credentials in `application.properties` or use environment variables:
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/virtual_casino
   spring.datasource.username=postgres
   spring.datasource.password=postgres
   ```

3. Database migrations are handled by Flyway and will run automatically on application startup.

### Redis Configuration

Redis is used for caching and real-time game session management:

```properties
spring.data.redis.host=localhost
spring.data.redis.port=6379
```

### Kafka Configuration

Kafka is used for event messaging between components:

```properties
spring.kafka.bootstrap-servers=localhost:9092
spring.kafka.consumer.group-id=virtual-casino-group
```

### Building the Application

```bash
# Clean and build the application
mvn clean install

# Skip tests during build
mvn clean install -DskipTests

# Run the application
mvn spring-boot:run
```

## Testing

### Test Structure

The project uses JUnit 5 with Mockito for unit testing. Tests are organized by component type:

- `src/test/java/co/id/virtual/game/web/app/service/` - Service layer tests
- `src/test/java/co/id/virtual/game/web/app/controller/` - Controller layer tests
- `src/test/java/co/id/virtual/game/web/app/repository/` - Repository layer tests

### Unit Testing

Unit tests focus on testing individual components in isolation using mocks for dependencies.

Example of a service test using Mockito:

```java
@ExtendWith(MockitoExtension.class)
public class ChipServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private ChipServiceImpl chipService;

    private User testUser;
    private UUID userId;

    @BeforeEach
    void setUp() {
        // Create test data and configure mocks
        userId = UUID.randomUUID();
        testUser = User.builder()
                .id(userId)
                .username("testuser")
                .chipsBalance(1000L)
                .build();
                
        // Use lenient stubbing to avoid strict stubbing issues
        Mockito.lenient().when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));
        
        // Mock repository methods as needed
    }

    @Test
    void testServiceMethod() {
        // Arrange
        // Additional test-specific setup
        
        // Act
        ChipBalanceDto result = chipService.getUserBalance(userId);
        
        // Assert
        assertNotNull(result);
        assertEquals(1000L, result.getChipsBalance());
    }
}
```

### Integration Testing

For integration tests, the project uses TestContainers to provide real database, Redis, and Kafka instances:

```java
@SpringBootTest
@Testcontainers
public class ChipServiceIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:14")
            .withDatabaseName("test_db")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void postgresProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }
    
    @Autowired
    private ChipService chipService;
    
    @Autowired
    private UserRepository userRepository;
    
    @Test
    void testServiceWithRealDatabase() {
        // Test using actual database
    }
}
```

### Running Tests

```bash
# Run all tests
mvn test

# Run a specific test class
mvn test -Dtest=ChipServiceTest

# Run a specific test method
mvn test -Dtest=ChipServiceTest#getUserBalance_ShouldReturnCorrectBalance
```

### Adding New Tests

When adding new tests:

1. Follow the existing package structure
2. Use descriptive test method names (e.g., `methodName_scenario_expectedResult`)
3. Structure tests with Arrange-Act-Assert pattern
4. For service tests, mock external dependencies
5. For integration tests, use TestContainers for real dependencies

## Development Guidelines

### Code Style

- Use Java 21 features where appropriate
- Follow standard Java naming conventions
- Use Lombok annotations to reduce boilerplate code
- Document public APIs with Javadoc

### Architecture

The application follows a layered architecture:

- **Controller Layer**: Handles HTTP requests and WebSocket messages
- **Service Layer**: Contains business logic
- **Repository Layer**: Handles data access
- **Model Layer**: Defines domain entities
- **DTO Layer**: Defines data transfer objects for API communication

### Key Components

- **Authentication**: JWT-based authentication with token refresh
- **WebSockets**: Used for real-time game updates and chat
- **Chip Management**: Virtual economy system with transactions
- **Game Sessions**: Real-time game state management using Redis

### Transaction Management

When implementing features that modify chip balances:

1. Always use the ChipService for chip operations
2. Handle InsufficientChipsException appropriately
3. Ensure transactions are properly recorded
4. Use proper transaction types from TransactionType enum

### WebSocket Communication

For real-time features:

1. Extend existing message types in the dto.websocket package
2. Use the UserInterceptor for authentication
3. Follow the existing pattern in GameWebSocketController

### Error Handling

- Use specific exception types for different error scenarios
- Handle exceptions at the controller level with @ExceptionHandler
- Return appropriate HTTP status codes and error messages

## Debugging

- Enable debug logging in application.properties:
  ```properties
  logging.level.co.id.virtual.game.web.app=DEBUG
  ```

- For SQL debugging:
  ```properties
  logging.level.org.hibernate.SQL=DEBUG
  logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE
  ```

- Add debug logs in tests:
  ```java
  System.out.println("[DEBUG_LOG] Your debug message");
  ```
