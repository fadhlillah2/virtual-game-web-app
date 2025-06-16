# Virtual Game Web App

A web-based platform for playing virtual casino games with toy chips for entertainment purposes only. This application allows users to play various casino games like Poker, Blackjack, Slots, and Roulette without involving real money.

## Features

- User authentication and registration
- Virtual chip management system
- Daily bonuses and gift system
- Real-time game play using WebSockets
- Multiple game types (Poker, Blackjack, Slots, Roulette)
- Chat functionality
- Leaderboards and player statistics
- Responsive design for desktop and mobile

## Technology Stack

### Frontend
- JSP (Java Server Pages)
- JavaScript with jQuery
- Bootstrap 5 for responsive design
- WebSocket with SockJS and STOMP for real-time communication

### Backend
- Java 21
- Spring Boot 3.2
- Spring MVC for web layer
- Spring Security with JWT for authentication
- Spring Data JPA with Hibernate for database access
- Spring WebSocket for real-time communication
- Kafka for event messaging
- Redis with Redisson for caching and session management

### Database
- PostgreSQL 15.13 for persistent storage
- Redis for caching and session data
- Flyway for database migrations

## Project Structure

```
virtual-game-web-app/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── co/id/virtual/game/web/app/
│   │   │       ├── config/           # Configuration classes
│   │   │       ├── controller/       # REST and WebSocket controllers
│   │   │       ├── dto/              # Data Transfer Objects
│   │   │       ├── event/            # Event classes and listeners
│   │   │       ├── exception/        # Custom exceptions
│   │   │       ├── model/            # JPA entity models
│   │   │       ├── repository/       # Spring Data repositories
│   │   │       ├── security/         # Security configuration and JWT
│   │   │       ├── service/          # Business logic services
│   │   │       └── VirtualGameWebAppApplication.java
│   │   ├── resources/
│   │   │   ├── application.properties # Application configuration
│   │   │   └── db/migration/         # Flyway migration scripts
│   │   └── webapp/
│   │       └── WEB-INF/
│   │           └── views/            # JSP view templates
│   └── test/                         # Test classes
└── pom.xml                           # Maven configuration
```

## Setup Instructions

### Prerequisites
- Java 21 JDK
- Maven 3.9+
- PostgreSQL 15.13
- Redis 8.0
- Kafka 3.6

### Database Setup
1. Create a PostgreSQL database named `virtual_casino`
2. Configure database connection in `application.properties`
3. Flyway migrations will automatically create the schema on application startup

### Redis Setup
1. Install and start Redis server
2. Configure Redis connection in `application.properties`

### Kafka Setup
1. Install and start Kafka server
2. Configure Kafka connection in `application.properties`

### Building the Application
```bash
mvn clean install
```

### Running the Application
```bash
mvn spring-boot:run
```

The application will be available at `http://localhost:8080`

## API Documentation

### Authentication Endpoints
- `POST /api/auth/register` - Register a new user
- `POST /api/auth/login` - Authenticate a user
- `POST /api/auth/refresh` - Refresh an authentication token
- `POST /api/auth/logout` - Logout a user

### User Endpoints
- `GET /api/users/me` - Get current user profile
- `PUT /api/users/me` - Update current user profile

### Game Endpoints
- `GET /api/games` - Get all active games
- `GET /api/games/{id}` - Get a specific game
- `GET /api/games/history` - Get user's game history

### Chip Endpoints
- `GET /api/chips/balance` - Get user's chip balance
- `POST /api/chips/daily-bonus` - Claim daily bonus
- `POST /api/chips/gift` - Send gift to another user
- `GET /api/chips/transactions` - Get transaction history

### WebSocket Endpoints
- `/ws` - WebSocket connection endpoint
- `/app/game.join` - Join a game
- `/app/game.action` - Perform a game action
- `/app/chat.message` - Send a chat message

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Acknowledgments

- This project was created as part of a coding exercise
- No real money gambling is involved
- All games are for entertainment purposes only
