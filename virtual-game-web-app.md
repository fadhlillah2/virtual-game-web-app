# TEMPLATE LENGKAP PROJECT FLOW
## Contoh: Virtual Casino Chips Game Web App

---

## 1. BRD (BUSINESS REQUIREMENTS DOCUMENT)

### **PROJECT OVERVIEW**
**Project Name:** Virtual Casino Chips Game Platform  
**Project Code:** VCC-001  
**Date:** June 2025  
**Version:** 1.0

### **EXECUTIVE SUMMARY**
Membangun platform web game casino virtual menggunakan chip mainan untuk hiburan pengguna tanpa melibatkan uang sungguhan.

### **BUSINESS OBJECTIVES**
- Menyediakan platform hiburan game casino yang aman dan legal
- Meningkatkan engagement user melalui gamifikasi
- Menciptakan revenue stream melalui iklan dan premium features
- Membangun community gaming yang aktif

### **BUSINESS CASE**
- **Problem:** Kurangnya platform casino game yang purely recreational
- **Solution:** Web app casino dengan virtual chips system
- **Market Size:** 50M+ recreational gamers di Indonesia
- **Revenue Potential:** $100K/tahun dari ads + premium

### **STAKEHOLDERS**
- **Primary:** Gaming enthusiasts, recreational players
- **Secondary:** Advertisers, content creators
- **Internal:** Development team, marketing team, legal team

### **SUCCESS CRITERIA**
- 10K+ registered users dalam 6 bulan
- 70% user retention rate
- Average session time 15+ menit
- Break-even dalam 12 bulan

### **CONSTRAINTS**
- No real money gambling
- Comply dengan regulasi gaming Indonesia
- Mobile-first approach
- Budget: $50K development
- Timeline: 4 bulan development

### **ASSUMPTIONS**
- Users want safe recreational gambling experience
- Virtual rewards akan cukup engaging
- Market appetite exists untuk entertainment gaming

---

## 2. FSD (FUNCTIONAL SPECIFICATION DOCUMENT)

### **SYSTEM OVERVIEW**
Web application untuk casino games dengan virtual chip system, user management, dan social features.

### **USER ROLES & PERMISSIONS**

#### **Guest User**
- View landing page
- Register/Login
- Limited game access (demo mode)

#### **Registered User**
- Full game access
- Virtual chip management
- Social features (chat, leaderboard)
- Profile customization

#### **Premium User**
- Advanced game features
- Exclusive tournaments
- Additional daily chips
- Priority support

#### **Admin**
- User management
- Game configuration
- Analytics dashboard
- Content moderation

### **CORE FUNCTIONALITIES**

#### **2.1 User Authentication System**
- **Registration:**
    - Required fields: Username, email, password, birth date
    - Email verification required
    - Age verification (18+ only)
    - Terms & conditions acceptance

- **Login:**
    - Email/username + password
    - "Remember me" option
    - Password reset functionality
    - Social login (Google, Facebook)

- **Profile Management:**
    - Edit profile information
    - Avatar upload/selection
    - Privacy settings
    - Account deletion

#### **2.2 Virtual Chip System**
- **Initial Chips:** 1000 chips untuk new user
- **Daily Bonus:** 500 chips setiap hari
- **Chip Sources:**
    - Daily login bonus
    - Game achievements
    - Tournament rewards
    - Watch ads (100 chips per ad)
    - Premium purchase (no real money value)

- **Chip Usage:**
    - Game betting
    - Tournament entry fees
    - Cosmetic purchases
    - Gift to other players

#### **2.3 Game Modules**

**Poker Texas Hold'em**
- 6-player tables
- Blind structure: 10/20, 25/50, 50/100
- All-in, fold, call, raise actions
- Side pot calculations
- Tournament mode available

**Blackjack**
- Single/multi-hand options
- Standard blackjack rules
- Insurance, double down, split
- Different bet limits: 10-500 chips

**Slot Machines**
- 5 different themed slots
- Various paylines (9, 15, 25)
- Bonus rounds and free spins
- Progressive jackpot system

**Roulette**
- European roulette (single zero)
- All standard betting options
- Live animation
- Betting history

#### **2.4 Social Features**
- **Chat System:**
    - Table-level chat
    - Global chat room
    - Private messaging
    - Emoji reactions
    - Profanity filter

- **Leaderboard:**
    - Weekly/monthly rankings
    - Total chips leaderboard
    - Game-specific rankings
    - Achievement system

- **Friends System:**
    - Add/remove friends
    - Friend status (online/offline)
    - Gift chips to friends
    - Private table invitations

#### **2.5 Tournament System**
- **Tournament Types:**
    - Poker tournaments (sit-n-go, scheduled)
    - Slot tournaments (highest score)
    - Blackjack tournaments (most chips)

- **Tournament Features:**
    - Entry fees (virtual chips)
    - Prize pools
    - Blind structure progression
    - Tournament lobby
    - Live updates

### **BUSINESS RULES**

#### **Chip Management Rules**
- Minimum bet: 10 chips
- Maximum bet: varies by game and user level
- No transfer chips between users (except gifts)
- Daily gift limit: 100 chips per user
- Chip expiry: 90 days of inactivity

#### **Game Rules**
- House edge maintained for sustainability
- RNG certification for fairness
- Game history stored for 30 days
- Anti-cheating measures implemented

#### **User Behavior Rules**
- No multiple accounts per person
- Chat moderation and reporting system
- Temporary bans for violations
- Permanent ban for severe violations

### **USER INTERFACE SPECIFICATIONS**

#### **Homepage**
- Hero section dengan game highlights
- Quick access to popular games
- User dashboard (chips, level, achievements)
- Recent activity feed
- Promotional banners

#### **Game Lobby**
- Game categories (Poker, Blackjack, Slots, Roulette)
- Table/game filtering
- Search functionality
- Preview mode
- Join game buttons

#### **Game Interface**
- Clean, intuitive design
- Responsive controls
- Real-time updates
- Chat panel (collapsible)
- Game rules accessible

### **SYSTEM WORKFLOWS**

#### **User Registration Flow**
1. User clicks "Sign Up"
2. Fill registration form
3. Email verification sent
4. User verifies email
5. Welcome tutorial
6. Receive initial 1000 chips
7. Redirect to game lobby

#### **Game Play Flow**
1. User selects game type
2. Choose table/room
3. Join with chip requirement
4. Play game with standard rules
5. Chips updated automatically
6. Game history recorded
7. Return to lobby

#### **Tournament Flow**
1. User browses tournaments
2. Register with entry fee
3. Wait for tournament start
4. Play tournament rounds
5. Advance/eliminate based on performance
6. Receive rewards if placed
7. Tournament history updated

---

## 3. TSD (TECHNICAL SPECIFICATION DOCUMENT)

### **SYSTEM ARCHITECTURE**

#### **High-Level Architecture**
```
Client (JSP/jQuery) → Nginx → Apache Tomcat → Spring MVC → Database
                                     ↓
                              Kafka Message Queue
                                     ↓
                              Redis Cache (Redisson)
```

#### **Technology Stack**

**Frontend:**
- View Technology: JSP (Java Server Pages)
- JavaScript: jQuery 3.7, Bootstrap 5
- AJAX: Fetch API / jQuery AJAX
- WebSocket: SockJS + STOMP
- CSS Framework: Bootstrap 5, Custom CSS
- Build Tool: Maven Frontend Plugin

**Backend:**
- Language: Java 21 (LTS)
- Framework: Spring Framework 6.1 (Spring Boot 3.2)
- Web: Spring MVC
- Security: Spring Security 6
- Data: Spring Data JPA + Hibernate 6.4
- Cache: Spring Cache + Redisson 3.38
- Messaging: Spring Kafka
- Validation: Hibernate Validator
- Deployment: Apache Tomcat EE 10

**Database & Storage:**
- Primary: PostgreSQL 15.13
- Cache: Redis 8.0 (with Redisson client)
- Message Queue: Apache Kafka 3.6
- Session Store: Redis
- File Storage: Local + NFS/AWS S3

**Infrastructure:**
- Web Server: Nginx 24.04
- Application Server: Apache Tomcat EE 10
- JVM: OpenJDK 21
- Build Tool: Apache Maven 3.9
- CI/CD: Jenkins/GitHub Actions
- Monitoring: Micrometer + Prometheus
- Load Balancer: Nginx

### **DATABASE DESIGN**

#### **JPA Entity Models**

**User Entity**
```java
@Entity
@Table(name = "users")
@EntityListeners(AuditingEntityListener.class)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @Column(unique = true, nullable = false, length = 50)
    private String username;
    
    @Column(unique = true, nullable = false)
    private String email;
    
    @Column(name = "password_hash", nullable = false)
    private String passwordHash;
    
    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;
    
    @Column(name = "avatar_url", length = 500)
    private String avatarUrl;
    
    @Column(name = "chips_balance")
    private Long chipsBalance = 1000L;
    
    @Column(name = "total_chips_won")
    private Long totalChipsWon = 0L;
    
    @Column(name = "level")
    private Integer level = 1;
    
    @Column(name = "experience_points")
    private Integer experiencePoints = 0;
    
    @Column(name = "is_premium")
    private Boolean isPremium = false;
    
    @Column(name = "is_active")
    private Boolean isActive = true;
    
    @Column(name = "last_login")
    private LocalDateTime lastLogin;
    
    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Constructors, getters, setters
}
```

**Game Entity**
```java
@Entity
@Table(name = "games")
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @Column(nullable = false, length = 100)
    private String name;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GameType type; // POKER, BLACKJACK, SLOTS, ROULETTE
    
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private GameStatus status = GameStatus.ACTIVE;
    
    @Column(name = "min_bet")
    private Integer minBet = 10;
    
    @Column(name = "max_bet")
    private Integer maxBet = 1000;
    
    @Column(name = "max_players")
    private Integer maxPlayers = 6;
    
    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb")
    private GameSettings settings;
    
    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    // Getters, setters
}
```

**GameSession Entity**
```java
@Entity
@Table(name = "game_sessions")
public class GameSession {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id")
    private Game game;
    
    @Type(JsonType.class)
    @Column(name = "session_data", columnDefinition = "jsonb")
    private SessionData sessionData;
    
    @Enumerated(EnumType.STRING)
    private SessionStatus status = SessionStatus.ACTIVE;
    
    @Column(name = "started_at")
    private LocalDateTime startedAt = LocalDateTime.now();
    
    @Column(name = "ended_at")
    private LocalDateTime endedAt;
    
    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL)
    private List<UserGameHistory> gameHistory = new ArrayList<>();
    
    // Getters, setters
}
```

**UserGameHistory Entity**
```java
@Entity
@Table(name = "user_game_history")
public class UserGameHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id")
    private Game game;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id")
    private GameSession session;
    
    @Column(name = "chips_before", nullable = false)
    private Long chipsBefore;
    
    @Column(name = "chips_after", nullable = false)
    private Long chipsAfter;
    
    @Column(name = "chips_wagered", nullable = false)
    private Long chipsWagered;
    
    @Column(name = "chips_won")
    private Long chipsWon = 0L;
    
    @Type(JsonType.class)
    @Column(name = "game_data", columnDefinition = "jsonb")
    private GameData gameData;
    
    @Column(name = "played_at")
    private LocalDateTime playedAt = LocalDateTime.now();
    
    // Getters, setters
}
```

**Transaction Entity**
```java
@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType type; // DAILY_BONUS, GAME_WIN, GIFT_RECEIVED, etc
    
    @Column(nullable = false)
    private Long amount;
    
    @Column(name = "balance_before", nullable = false)
    private Long balanceBefore;
    
    @Column(name = "balance_after", nullable = false)
    private Long balanceAfter;
    
    @Column(name = "reference_id")
    private UUID referenceId;
    
    private String description;
    
    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    // Getters, setters
}
```

### **SPRING MVC CONTROLLER SPECIFICATIONS**

#### **Authentication Controller**

**AuthController.java**
```java
@RestController
@RequestMapping("/api/auth")
@Validated
@Slf4j
public class AuthController {
    
    @Autowired
    private AuthService authService;
    
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(
            @Valid @RequestBody RegisterRequest request) {
        
        try {
            AuthResponse response = authService.register(request);
            return ResponseEntity.ok(ApiResponse.success(
                "Registration successful. Please verify your email.", 
                response
            ));
        } catch (UserAlreadyExistsException e) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error("User already exists"));
        }
    }
    
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletRequest httpRequest) {
        
        try {
            LoginResponse response = authService.authenticate(request);
            
            // Log successful login
            log.info("User logged in: {}", request.getEmail());
            
            return ResponseEntity.ok(ApiResponse.success(
                "Login successful", 
                response
            ));
        } catch (InvalidCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.error("Invalid credentials"));
        }
    }
    
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<TokenResponse>> refreshToken(
            @Valid @RequestBody RefreshTokenRequest request) {
        
        TokenResponse response = authService.refreshToken(request.getRefreshToken());
        return ResponseEntity.ok(ApiResponse.success("Token refreshed", response));
    }
    
    @PostMapping("/logout")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse<Void>> logout(
            @AuthenticationPrincipal UserPrincipal currentUser) {
        
        authService.logout(currentUser.getId());
        return ResponseEntity.ok(ApiResponse.success("Logged out successfully"));
    }
}
```

**Request/Response DTOs:**
```java
// RegisterRequest.java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3-50 characters")
    private String username;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;
    
    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).*$", 
             message = "Password must contain uppercase, lowercase, and number")
    private String password;
    
    @NotNull(message = "Birth date is required")
    @Past(message = "Birth date must be in the past")
    private LocalDate birthDate;
}

// LoginResponse.java
@Data
@Builder
public class LoginResponse {
    private String accessToken;
    private String refreshToken;
    private String tokenType = "Bearer";
    private Long expiresIn;
    private UserDto user;
}
```

#### **Game Controller**

**GameController.java**
```java
@RestController
@RequestMapping("/api/games")
@PreAuthorize("hasRole('USER')")
@Slf4j
public class GameController {
    
    @Autowired
    private GameService gameService;
    
    @Autowired
    private GameSessionService gameSessionService;
    
    @GetMapping
    public ResponseEntity<ApiResponse<List<GameDto>>> getAllGames() {
        List<GameDto> games = gameService.getAllActiveGames();
        return ResponseEntity.ok(ApiResponse.success("Games retrieved", games));
    }
    
    @GetMapping("/{gameId}")
    public ResponseEntity<ApiResponse<GameDetailDto>> getGameDetail(
            @PathVariable UUID gameId) {
        
        GameDetailDto gameDetail = gameService.getGameDetail(gameId);
        return ResponseEntity.ok(ApiResponse.success("Game detail retrieved", gameDetail));
    }
    
    @PostMapping("/join")
    public ResponseEntity<ApiResponse<JoinGameResponse>> joinGame(
            @Valid @RequestBody JoinGameRequest request,
            @AuthenticationPrincipal UserPrincipal currentUser) {
        
        try {
            JoinGameResponse response = gameSessionService.joinGame(
                currentUser.getId(), 
                request
            );
            
            log.info("User {} joined game {}", currentUser.getUsername(), request.getGameId());
            
            return ResponseEntity.ok(ApiResponse.success("Joined game successfully", response));
        } catch (InsufficientChipsException e) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error("Insufficient chips"));
        } catch (GameFullException e) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error("Game is full"));
        }
    }
    
    @PostMapping("/leave")
    public ResponseEntity<ApiResponse<Void>> leaveGame(
            @Valid @RequestBody LeaveGameRequest request,
            @AuthenticationPrincipal UserPrincipal currentUser) {
        
        gameSessionService.leaveGame(currentUser.getId(), request.getSessionId());
        
        log.info("User {} left game session {}", 
                currentUser.getUsername(), request.getSessionId());
        
        return ResponseEntity.ok(ApiResponse.success("Left game successfully"));
    }
    
    @GetMapping("/history")
    public ResponseEntity<ApiResponse<PagedResponse<GameHistoryDto>>> getGameHistory(
            @AuthenticationPrincipal UserPrincipal currentUser,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("playedAt").descending());
        PagedResponse<GameHistoryDto> history = gameService.getUserGameHistory(
            currentUser.getId(), 
            pageable
        );
        
        return ResponseEntity.ok(ApiResponse.success("Game history retrieved", history));
    }
}
```

#### **Chip Management Controller**

**ChipController.java**
```java
@RestController
@RequestMapping("/api/chips")
@PreAuthorize("hasRole('USER')")
@Slf4j
public class ChipController {
    
    @Autowired
    private ChipService chipService;
    
    @GetMapping("/balance")
    public ResponseEntity<ApiResponse<ChipBalanceDto>> getBalance(
            @AuthenticationPrincipal UserPrincipal currentUser) {
        
        ChipBalanceDto balance = chipService.getUserBalance(currentUser.getId());
        return ResponseEntity.ok(ApiResponse.success("Balance retrieved", balance));
    }
    
    @PostMapping("/daily-bonus")
    public ResponseEntity<ApiResponse<DailyBonusResponse>> claimDailyBonus(
            @AuthenticationPrincipal UserPrincipal currentUser) {
        
        try {
            DailyBonusResponse response = chipService.claimDailyBonus(currentUser.getId());
            
            log.info("User {} claimed daily bonus: {} chips", 
                    currentUser.getUsername(), response.getBonusAmount());
            
            return ResponseEntity.ok(ApiResponse.success("Daily bonus claimed", response));
        } catch (DailyBonusAlreadyClaimedException e) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error("Daily bonus already claimed today"));
        }
    }
    
    @PostMapping("/gift")
    public ResponseEntity<ApiResponse<GiftResponse>> sendGift(
            @Valid @RequestBody SendGiftRequest request,
            @AuthenticationPrincipal UserPrincipal currentUser) {
        
        try {
            GiftResponse response = chipService.sendGift(
                currentUser.getId(), 
                request.getRecipientId(), 
                request.getAmount()
            );
            
            log.info("User {} sent {} chips to user {}", 
                    currentUser.getUsername(), request.getAmount(), request.getRecipientId());
            
            return ResponseEntity.ok(ApiResponse.success("Gift sent successfully", response));
        } catch (InsufficientChipsException e) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error("Insufficient chips"));
        } catch (DailyGiftLimitExceededException e) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error("Daily gift limit exceeded"));
        }
    }
    
    @GetMapping("/transactions")
    public ResponseEntity<ApiResponse<PagedResponse<TransactionDto>>> getTransactionHistory(
            @AuthenticationPrincipal UserPrincipal currentUser,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) TransactionType type) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        PagedResponse<TransactionDto> transactions = chipService.getTransactionHistory(
            currentUser.getId(), 
            type, 
            pageable
        );
        
        return ResponseEntity.ok(ApiResponse.success("Transaction history retrieved", transactions));
    }
}
```

### **WEBSOCKET & MESSAGING CONFIGURATION**

#### **Spring WebSocket Configuration**

**WebSocketConfig.java**
```java
@Configuration
@EnableWebSocketMessageBroker
@Slf4j
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Enable simple broker for topics
        config.enableSimpleBroker("/topic", "/queue");
        
        // Set application destination prefix
        config.setApplicationDestinationPrefixes("/app");
        
        // Set user destination prefix
        config.setUserDestinationPrefix("/user");
    }
    
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }
    
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new UserInterceptor());
    }
}
```

**Game WebSocket Controller**
```java
@Controller
@Slf4j
public class GameWebSocketController {
    
    @Autowired
    private GameService gameService;
    
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    
    @MessageMapping("/game.join")
    @SendTo("/topic/game.{gameId}")
    public GameStateMessage joinGame(
            @DestinationVariable String gameId,
            @Payload JoinGameMessage message,
            Principal principal) {
        
        log.info("User {} joining game {}", principal.getName(), gameId);
        
        GameState gameState = gameService.addPlayerToGame(
            UUID.fromString(gameId), 
            message, 
            principal.getName()
        );
        
        return new GameStateMessage(gameState);
    }
    
    @MessageMapping("/game.action")
    @SendTo("/topic/game.{gameId}")
    public GameActionResponse handleGameAction(
            @DestinationVariable String gameId,
            @Payload GameActionMessage action,
            Principal principal) {
        
        log.info("Game action from {}: {} in game {}", 
                principal.getName(), action.getAction(), gameId);
        
        GameActionResponse response = gameService.processGameAction(
            UUID.fromString(gameId),
            action,
            principal.getName()
        );
        
        // Broadcast to all players in the game
        messagingTemplate.convertAndSend(
            "/topic/game/" + gameId + "/action", 
            response
        );
        
        return response;
    }
    
    @MessageMapping("/chat.message")
    @SendTo("/topic/chat.{roomId}")
    public ChatMessage handleChatMessage(
            @DestinationVariable String roomId,
            @Payload ChatMessage message,
            Principal principal) {
        
        message.setSender(principal.getName());
        message.setTimestamp(LocalDateTime.now());
        
        // Log chat message
        log.info("Chat message in room {}: {} - {}", 
                roomId, principal.getName(), message.getContent());
        
        return message;
    }
}
```

#### **Kafka Configuration**

**KafkaConfig.java**
```java
@Configuration
@EnableKafka
@Slf4j
public class KafkaConfig {
    
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;
    
    @Bean
    public ProducerFactory<String, Object> producerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.RETRIES_CONFIG, 3);
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
        
        return new DefaultKafkaProducerFactory<>(props);
    }
    
    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
    
    @Bean
    public ConsumerFactory<String, Object> consumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "virtual-casino-group");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        
        return new DefaultKafkaConsumerFactory<>(props);
    }
    
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Object> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Object> factory = 
            new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }
}
```

**Game Event Publisher**
```java
@Service
@Slf4j
public class GameEventPublisher {
    
    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;
    
    public void publishGameStarted(GameStartedEvent event) {
        log.info("Publishing game started event: {}", event.getGameId());
        kafkaTemplate.send("game-events", "game-started", event);
    }
    
    public void publishGameEnded(GameEndedEvent event) {
        log.info("Publishing game ended event: {}", event.getGameId());
        kafkaTemplate.send("game-events", "game-ended", event);
    }
    
    public void publishUserAction(UserActionEvent event) {
        log.info("Publishing user action: {} in game {}", 
                event.getAction(), event.getGameId());
        kafkaTemplate.send("user-actions", event.getUserId().toString(), event);
    }
    
    public void publishChipTransaction(ChipTransactionEvent event) {
        log.info("Publishing chip transaction: {} chips for user {}", 
                event.getAmount(), event.getUserId());
        kafkaTemplate.send("chip-transactions", event.getUserId().toString(), event);
    }
}
```

**Event Listeners**
```java
@Component
@Slf4j
public class GameEventListener {
    
    @Autowired
    private AnalyticsService analyticsService;
    
    @Autowired
    private NotificationService notificationService;
    
    @KafkaListener(topics = "game-events", groupId = "analytics-group")
    public void handleGameEvents(
            @Payload Object event,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
            @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) String key) {
        
        log.info("Received game event: {} - {}", key, event);
        
        if ("game-started".equals(key) && event instanceof GameStartedEvent) {
            analyticsService.recordGameStart((GameStartedEvent) event);
        } else if ("game-ended".equals(key) && event instanceof GameEndedEvent) {
            analyticsService.recordGameEnd((GameEndedEvent) event);
        }
    }
    
    @KafkaListener(topics = "user-actions", groupId = "monitoring-group")
    public void handleUserActions(
            @Payload UserActionEvent event,
            @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition) {
        
        log.info("Received user action: {} from user {} in partition {}", 
                event.getAction(), event.getUserId(), partition);
        
        // Process user action for analytics/monitoring
        analyticsService.recordUserAction(event);
        
        // Check for suspicious activity
        if (isSupiciousActivity(event)) {
            notificationService.alertSuspiciousActivity(event);
        }
    }
    
    @KafkaListener(topics = "chip-transactions", groupId = "transaction-group")
    public void handleChipTransactions(@Payload ChipTransactionEvent event) {
        log.info("Processing chip transaction: {} chips for user {}", 
                event.getAmount(), event.getUserId());
        
        analyticsService.recordChipTransaction(event);
    }
    
    private boolean isSupiciousActivity(UserActionEvent event) {
        // Implement suspicious activity detection logic
        return false;
    }
}
```

### **REDIS & CACHING CONFIGURATION**

#### **Redisson Configuration**

**RedissonConfig.java**
```java
@Configuration
@Slf4j
public class RedissonConfig {
    
    @Value("${spring.redis.host}")
    private String redisHost;
    
    @Value("${spring.redis.port}")
    private int redisPort;
    
    @Value("${spring.redis.password}")
    private String redisPassword;
    
    @Bean(destroyMethod = "shutdown")
    public RedissonClient redissonClient() {
        Config config = new Config();
        
        config.useSingleServer()
              .setAddress("redis://" + redisHost + ":" + redisPort)
              .setPassword(redisPassword)
              .setConnectionMinimumIdleSize(5)
              .setConnectionPoolSize(10)
              .setIdleConnectionTimeout(10000)
              .setConnectTimeout(10000)
              .setTimeout(3000)
              .setRetryAttempts(3)
              .setRetryInterval(1500);
        
        return Redisson.create(config);
    }
    
    @Bean
    public CacheManager cacheManager(RedissonClient redissonClient) {
        Map<String, CacheConfig> configMap = new HashMap<>();
        
        // User cache - 30 minutes TTL
        configMap.put("users", new CacheConfig(
            Duration.ofMinutes(30).toMillis(),
            Duration.ofMinutes(10).toMillis()
        ));
        
        // Game state cache - 5 minutes TTL
        configMap.put("gameStates", new CacheConfig(
            Duration.ofMinutes(5).toMillis(),
            Duration.ofMinutes(2).toMillis()
        ));
        
        // Leaderboard cache - 1 hour TTL
        configMap.put("leaderboards", new CacheConfig(
            Duration.ofHours(1).toMillis(),
            Duration.ofMinutes(30).toMillis()
        ));
        
        return new RedissonSpringCacheManager(redissonClient, configMap);
    }
}
```

**Game Session Cache Service**
```java
@Service
@Slf4j
public class GameSessionCacheService {
    
    @Autowired
    private RedissonClient redissonClient;
    
    private static final String GAME_SESSION_PREFIX = "game:session:";
    private static final String PLAYER_SESSION_PREFIX = "player:session:";
    
    public void cacheGameSession(UUID sessionId, GameSessionData sessionData) {
        RBucket<GameSessionData> bucket = redissonClient.getBucket(
            GAME_SESSION_PREFIX + sessionId.toString()
        );
        bucket.set(sessionData, Duration.ofHours(2));
        
        log.debug("Cached game session: {}", sessionId);
    }
    
    public GameSessionData getGameSession(UUID sessionId) {
        RBucket<GameSessionData> bucket = redissonClient.getBucket(
            GAME_SESSION_PREFIX + sessionId.toString()
        );
        return bucket.get();
    }
    
    public void removeGameSession(UUID sessionId) {
        redissonClient.getBucket(GAME_SESSION_PREFIX + sessionId.toString()).delete();
        log.debug("Removed game session from cache: {}", sessionId);
    }
    
    public void addPlayerToSession(UUID userId, UUID sessionId) {
        RSet<UUID> playerSessions = redissonClient.getSet(
            PLAYER_SESSION_PREFIX + userId.toString()
        );
        playerSessions.add(sessionId);
        playerSessions.expire(Duration.ofHours(2));
    }
    
    public Set<UUID> getPlayerSessions(UUID userId) {
        RSet<UUID> playerSessions = redissonClient.getSet(
            PLAYER_SESSION_PREFIX + userId.toString()
        );
        return playerSessions.readAll();
    }
    
    public void removePlayerFromSession(UUID userId, UUID sessionId) {
        RSet<UUID> playerSessions = redissonClient.getSet(
            PLAYER_SESSION_PREFIX + userId.toString()
        );
        playerSessions.remove(sessionId);
    }
}
```

### **SECURITY SPECIFICATIONS**

#### **Spring Security Configuration**

**SecurityConfig.java**
```java
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@Slf4j
public class SecurityConfig {
    
    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    
    @Autowired
    private JwtRequestFilter jwtRequestFilter;
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }
    
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
            .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
            .and()
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/public/**").permitAll()
                .requestMatchers("/ws/**").permitAll()
                .requestMatchers("/actuator/health").permitAll()
                .requestMatchers(HttpMethod.GET, "/").permitAll()
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            );
        
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        
        // Security headers
        http.headers(headers -> headers
            .frameOptions().deny()
            .contentTypeOptions().and()
            .httpStrictTransportSecurity(hstsConfig -> hstsConfig
                .maxAgeInSeconds(31536000)
                .includeSubDomains(true)
            )
        );
        
        return http.build();
    }
}
```

**JWT Security Implementation**
```java
@Component
@Slf4j
public class JwtRequestFilter extends OncePerRequestFilter {
    
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    
    @Autowired
    private UserDetailsService userDetailsService;
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                   HttpServletResponse response, 
                                   FilterChain chain) throws ServletException, IOException {
        
        final String requestTokenHeader = request.getHeader("Authorization");
        
        String username = null;
        String jwtToken = null;
        
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7);
            try {
                username = jwtTokenUtil.getUsernameFromToken(jwtToken);
            } catch (IllegalArgumentException e) {
                log.error("Unable to get JWT Token");
            } catch (ExpiredJwtException e) {
                log.error("JWT Token has expired");
            }
        }
        
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            
            if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = 
                    new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        chain.doFilter(request, response);
    }
}
```

### **PERFORMANCE SPECIFICATIONS**

#### **Response Time Requirements**
- API endpoints: < 200ms average
- WebSocket events: < 50ms
- Database queries: < 100ms
- Page load time: < 3 seconds

#### **Scalability Requirements**
- Support 1000 concurrent users
- Handle 10,000 API requests/minute
- Database: 100,000 game sessions/day
- Auto-scaling based on CPU/memory usage

#### **Availability Requirements**
- Uptime: 99.9%
- Planned maintenance window: 2 hours/month
- Disaster recovery: 4-hour RTO

---

## 4. DEVELOPMENT PLAN

### **PROJECT TIMELINE: 16 WEEKS**

#### **PHASE 1: SETUP & FOUNDATION (Weeks 1-2)**

**Week 1: Project Setup**
- [ ] Maven multi-module project structure setup
- [ ] Spring Boot application configuration
- [ ] PostgreSQL 15.13 database setup
- [ ] Redis 8.0 cluster configuration
- [ ] Git repository and branching strategy
- [ ] Jenkins/GitHub Actions CI/CD pipeline

**Week 2: Core Infrastructure**
- [ ] Spring Security JWT configuration
- [ ] JPA/Hibernate entity models
- [ ] Redisson cache configuration
- [ ] Kafka message broker setup
- [ ] Basic user management services
- [ ] JSP view resolver configuration

#### **PHASE 2: USER MANAGEMENT (Weeks 3-4)**

**Week 3: Authentication System**
- [ ] User registration/login REST endpoints
- [ ] JWT token management service
- [ ] Email verification with Spring Mail
- [ ] Password reset functionality
- [ ] JSP authentication pages
- [ ] Spring Security integration

**Week 4: Profile Management**
- [ ] User profile CRUD operations
- [ ] Avatar upload with Spring multipart
- [ ] Settings management pages
- [ ] JSP profile management views
- [ ] Authentication flow testing

#### **PHASE 3: CHIP SYSTEM (Weeks 5-6)**

**Week 5: Virtual Chip Backend**
- [ ] Chip transaction service with JPA
- [ ] Daily bonus scheduled task (@Scheduled)
- [ ] Gift system with validation
- [ ] Transaction history REST APIs
- [ ] Kafka events for chip transactions

**Week 6: Chip Frontend & Integration**
- [ ] JSP chip balance components
- [ ] AJAX transaction history
- [ ] Daily bonus notifications
- [ ] Gift sending interface
- [ ] Integration testing with TestContainers

#### **PHASE 4: GAME DEVELOPMENT (Weeks 7-12)**

**Week 7-8: Poker Implementation**
- [ ] Poker game engine with Spring Service
- [ ] Texas Hold'em rules implementation
- [ ] Multi-player session management
- [ ] WebSocket STOMP integration
- [ ] JSP poker game interface

**Week 9-10: Additional Games**
- [ ] Blackjack service implementation
- [ ] Slot machine mechanics
- [ ] Roulette game logic
- [ ] Game lobby JSP pages
- [ ] Game selection flow

**Week 11-12: Game Polish & Testing**
- [ ] Game UI/UX improvements
- [ ] JavaScript animations
- [ ] WebSocket state synchronization
- [ ] Performance optimization
- [ ] JUnit integration testing

#### **PHASE 5: SOCIAL FEATURES (Weeks 13-14)**

**Week 13: Chat & Social**
- [ ] WebSocket chat system with STOMP
- [ ] Friends list functionality
- [ ] Leaderboard implementation with Redis
- [ ] Kafka notification events
- [ ] JSP social components

**Week 14: Tournament System**
- [ ] Tournament service implementation
- [ ] Tournament lobby JSP
- [ ] Prize distribution with transactions
- [ ] Tournament history tracking
- [ ] Real-time tournament updates

#### **PHASE 6: TESTING & DEPLOYMENT (Weeks 15-16)**

**Week 15: Integration Testing**
- [ ] Spring Boot Test integration
- [ ] Testcontainers for database testing
- [ ] MockMvc for web layer testing
- [ ] Kafka test containers
- [ ] Performance testing with JMeter

**Week 16: Deployment & Launch**
- [ ] Tomcat EE deployment configuration
- [ ] Nginx reverse proxy setup
- [ ] Database migration scripts
- [ ] Production deployment
- [ ] Monitoring with Micrometer/Actuator

### **TEAM STRUCTURE**

**Development Team (6 people):**
- 1 Tech Lead/Senior Java Developer
- 2 Backend Java Developers (Spring)
- 2 Frontend Developers (JSP/JavaScript)
- 1 DevOps Engineer (Tomcat/Nginx)

**Support Team:**
- 1 UI/UX Designer
- 1 QA Engineer (Selenium/JUnit)
- 1 Product Manager
- 1 Security Consultant

### **DEVELOPMENT METHODOLOGY**

**Agile/Scrum with Spring Framework:**
- 2-week sprints
- Daily standups (15 min)
- Sprint planning with story points
- Code review using pull requests
- Maven build automation
- SonarQube code quality analysis

**Java Code Standards:**
- Google Java Style Guide
- SpotBugs for bug detection
- JaCoCo for test coverage (minimum 80%)
- Checkstyle for code formatting
- Maven enforcer plugin for dependency management

### **RISK MANAGEMENT**

**Technical Risks:**
- **Risk:** JPA/Hibernate performance issues
- **Mitigation:** Query optimization, proper indexing, connection pooling

- **Risk:** WebSocket scalability with STOMP
- **Mitigation:** Load testing, Redis session clustering

- **Risk:** Kafka message processing bottlenecks
- **Mitigation:** Proper partitioning, consumer group scaling

**Project Risks:**
- **Risk:** Spring Boot version compatibility
- **Mitigation:** Stick to LTS versions, proper dependency management

- **Risk:** JSP performance vs modern frontends
- **Mitigation:** AJAX optimization, proper caching strategies

---

## 5. DEVELOPMENT PHASE

### **SPRINT BREAKDOWN**

#### **SPRINT 1-2: Foundation Setup**
**Sprint Goals:**
- Complete Spring Boot application setup
- Basic authentication with JWT
- JPA entity implementation

**User Stories:**
```
As a developer, I want a properly configured Spring Boot environment
So that I can start coding efficiently

Acceptance Criteria:
- [ ] Maven multi-module project structure
- [ ] Spring Boot 3.2 with Java 21
- [ ] PostgreSQL connection with HikariCP
- [ ] Redis connection with Redisson
- [ ] JUnit 5 test configuration
```

**Technical Tasks:**
- Setup PostgreSQL with Flyway migrations
- Configure Redisson for session management
- Implement JWT authentication with Spring Security
- Create user registration/login endpoints
- Setup JSP view resolver with JSTL

**Maven Configuration (pom.xml):**
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0">
    <modelVersion>4.0.0</modelVersion>
    
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.2.0</version>
        <relativePath/>
    </parent>
    
    <groupId>com.virtualcasino</groupId>
    <artifactId>virtual-casino-app</artifactId>
    <version>1.0.0</version>
    <packaging>war</packaging>
    
    <properties>
        <java.version>21</java.version>
        <redisson.version>3.38.0</redisson.version>
        <testcontainers.version>1.19.0</testcontainers.version>
    </properties>
    
    <dependencies>
        <!-- Spring Boot Starters -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>
        
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-security</artifactId>
        </dependency>
        
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-websocket</artifactId>
        </dependency>
        
        <dependency>
            <groupId>org.springframework.kafka</groupId>
            <artifactId>spring-kafka</artifactId>
        </dependency>
        
        <!-- JSP Support -->
        <dependency>
            <groupId>org.apache.tomcat.embed</groupId>
            <artifactId>tomcat-embed-jasper</artifactId>
        </dependency>
        
        <dependency>
            <groupId>jakarta.servlet.jsp.jstl</groupId>
            <artifactId>jakarta.servlet.jsp.jstl-api</artifactId>
        </dependency>
        
        <dependency>
            <groupId>org.glassfish.web</groupId>
            <artifactId>jakarta.servlet.jsp.jstl</artifactId>
        </dependency>
        
        <!-- Database -->
        <dependency>
            <groupId>org.postgresql</groupId>
            <artifactId>postgresql</artifactId>
        </dependency>
        
        <dependency>
            <groupId>org.flywaydb</groupId>
            <artifactId>flyway-core</artifactId>
        </dependency>
        
        <!-- Redis -->
        <dependency>
            <groupId>org.redisson</groupId>
            <artifactId>redisson-spring-boot-starter</artifactId>
            <version>${redisson.version}</version>
        </dependency>
        
        <!-- JWT -->
        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt-api</artifactId>
            <version>0.12.3</version>
        </dependency>
        
        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt-impl</artifactId>
            <version>0.12.3</version>
            <scope>runtime</scope>
        </dependency>
        
        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt-jackson</artifactId>
            <version>0.12.3</version>
            <scope>runtime</scope>
        </dependency>
        
        <!-- Testing -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
        
        <dependency>
            <groupId>org.testcontainers</groupId>
            <artifactId>junit-jupiter</artifactId>
            <scope>test</scope>
        </dependency>
        
        <dependency>
            <groupId>org.testcontainers</groupId>
            <artifactId>postgresql</artifactId>
            <scope>test</scope>
        </dependency>
        
        <dependency>
            <groupId>org.testcontainers</groupId>
            <artifactId>kafka</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>
    
    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
            
            <plugin>
                <groupId>org.flywaydb</groupId>
                <artifactId>flyway-maven-plugin</artifactId>
            </plugin>
            
            <plugin>
                <groupId>org.sonarsource.scanner.maven</groupId>
                <artifactId>sonar-maven-plugin</artifactId>
            </plugin>
            
            <plugin>
                <groupId>org.jacoco</groupId>
                <artifactId>jacoco-maven-plugin</artifactId>
                <executions>
                    <execution>
                        <goals>
                            <goal>prepare-agent</goal>
                        </goals>
                    </execution>
                    <execution>
                        <id>report</id>
                        <phase>test</phase>
                        <goals>
                            <goal>report</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
</project>
```

#### **SPRINT 3-4: User Management**
**Sprint Goals:**
- Complete user registration/login flow
- Implement profile management with JSP
- Add email verification with Spring Mail

**User Stories:**
```
As a new user, I want to register for an account
So that I can start playing games

Acceptance Criteria:
- [ ] Registration JSP form with validation
- [ ] Email verification required
- [ ] Age verification (18+)
- [ ] Initial 1000 chips granted automatically
- [ ] Welcome tutorial JSP page
```

**JSP Implementation Examples:**

**Registration Page (register.jsp):**
```jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
<head>
    <title>Virtual Casino - Register</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://code.jquery.com/jquery-3.7.0.min.js"></script>
</head>
<body>
    <div class="container mt-5">
        <div class="row justify-content-center">
            <div class="col-md-6">
                <div class="card">
                    <div class="card-header">
                        <h3 class="text-center">Join Virtual Casino</h3>
                    </div>
                    <div class="card-body">
                        <form:form modelAttribute="registerRequest" method="post" action="/api/auth/register" id="registerForm">
                            <c:if test="${not empty error}">
                                <div class="alert alert-danger">${error}</div>
                            </c:if>
                            
                            <div class="mb-3">
                                <label for="username" class="form-label">Username</label>
                                <form:input path="username" class="form-control" id="username" required="true" 
                                           minlength="3" maxlength="50"/>
                                <form:errors path="username" class="text-danger"/>
                            </div>
                            
                            <div class="mb-3">
                                <label for="email" class="form-label">Email</label>
                                <form:input path="email" type="email" class="form-control" id="email" required="true"/>
                                <form:errors path="email" class="text-danger"/>
                            </div>
                            
                            <div class="mb-3">
                                <label for="password" class="form-label">Password</label>
                                <form:password path="password" class="form-control" id="password" required="true"
                                              minlength="8" pattern="^(?=.*[a-z])(?=.*[A-Z])(?=.*\d).*$"/>
                                <div class="form-text">Must contain uppercase, lowercase, and number</div>
                                <form:errors path="password" class="text-danger"/>
                            </div>
                            
                            <div class="mb-3">
                                <label for="birthDate" class="form-label">Birth Date</label>
                                <form:input path="birthDate" type="date" class="form-control" id="birthDate" required="true"/>
                                <form:errors path="birthDate" class="text-danger"/>
                            </div>
                            
                            <div class="mb-3 form-check">
                                <input type="checkbox" class="form-check-input" id="terms" required>
                                <label class="form-check-label" for="terms">
                                    I agree to the <a href="/terms" target="_blank">Terms and Conditions</a>
                                </label>
                            </div>
                            
                            <button type="submit" class="btn btn-primary w-100" id="submitBtn">
                                Create Account
                            </button>
                        </form:form>
                        
                        <div class="text-center mt-3">
                            <p>Already have an account? <a href="/login">Login here</a></p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <script>
        $(document).ready(function() {
            // Age validation
            $('#birthDate').on('change', function() {
                const birthDate = new Date($(this).val());
                const today = new Date();
                const age = today.getFullYear() - birthDate.getFullYear();
                
                if (age < 18) {
                    alert('You must be 18 years or older to register');
                    $(this).val('');
                }
            });
            
            // Form submission with AJAX
            $('#registerForm').on('submit', function(e) {
                e.preventDefault();
                
                $('#submitBtn').prop('disabled', true).text('Creating Account...');
                
                $.ajax({
                    url: '/api/auth/register',
                    type: 'POST',
                    contentType: 'application/json',
                    data: JSON.stringify({
                        username: $('#username').val(),
                        email: $('#email').val(),
                        password: $('#password').val(),
                        birthDate: $('#birthDate').val()
                    }),
                    success: function(response) {
                        if (response.success) {
                            alert('Registration successful! Please check your email for verification.');
                            window.location.href = '/login';
                        }
                    },
                    error: function(xhr) {
                        const error = xhr.responseJSON?.message || 'Registration failed';
                        $('.card-body').prepend('<div class="alert alert-danger">' + error + '</div>');
                    },
                    complete: function() {
                        $('#submitBtn').prop('disabled', false).text('Create Account');
                    }
                });
            });
        });
    </script>
</body>
</html>
```

**Game Lobby JSP (lobby.jsp):**
```jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>Virtual Casino - Game Lobby</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <script src="https://code.jquery.com/jquery-3.7.0.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/sockjs-client@1/dist/sockjs.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/stompjs@2.3.3/lib/stomp.min.js"></script>
</head>
<body>
    <!-- Navigation -->
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
        <div class="container">
            <a class="navbar-brand" href="/">
                <i class="fas fa-dice"></i> Virtual Casino
            </a>
            
            <div class="navbar-nav ms-auto">
                <div class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown">
                        <i class="fas fa-coins"></i> 
                        <span id="chipBalance"><fmt:formatNumber value="${user.chipsBalance}" pattern="#,###"/></span> chips
                    </a>
                    <ul class="dropdown-menu">
                        <li><a class="dropdown-item" href="#" onclick="claimDailyBonus()">
                            <i class="fas fa-gift"></i> Daily Bonus
                        </a></li>
                        <li><a class="dropdown-item" href="/chips/history">
                            <i class="fas fa-history"></i> Transaction History
                        </a></li>
                    </ul>
                </div>
                
                <div class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown">
                        <i class="fas fa-user"></i> ${user.username}
                    </a>
                    <ul class="dropdown-menu">
                        <li><a class="dropdown-item" href="/profile">
                            <i class="fas fa-user-edit"></i> Profile
                        </a></li>
                        <li><a class="dropdown-item" href="/settings">
                            <i class="fas fa-cog"></i> Settings
                        </a></li>
                        <li><hr class="dropdown-divider"></li>
                        <li><a class="dropdown-item" href="#" onclick="logout()">
                            <i class="fas fa-sign-out-alt"></i> Logout
                        </a></li>
                    </ul>
                </div>
            </div>
        </div>
    </nav>
    
    <!-- Main Content -->
    <div class="container mt-4">
        <!-- Welcome Section -->
        <div class="row mb-4">
            <div class="col-12">
                <div class="card bg-primary text-white">
                    <div class="card-body">
                        <h2>Welcome back, ${user.username}!</h2>
                        <p class="mb-0">Ready to play? Choose your game below.</p>
                    </div>
                </div>
            </div>
        </div>
        
        <!-- Game Categories -->
        <div class="row">
            <!-- Poker Section -->
            <div class="col-md-6 mb-4">
                <div class="card">
                    <div class="card-header">
                        <h4><i class="fas fa-spade"></i> Poker Games</h4>
                    </div>
                    <div class="card-body">
                        <div id="pokerTables" class="game-tables">
                            <!-- Tables will be loaded via AJAX -->
                        </div>
                        <button class="btn btn-primary" onclick="loadPokerTables()">
                            <i class="fas fa-refresh"></i> Refresh Tables
                        </button>
                    </div>
                </div>
            </div>
            
            <!-- Blackjack Section -->
            <div class="col-md-6 mb-4">
                <div class="card">
                    <div class="card-header">
                        <h4><i class="fas fa-heart"></i> Blackjack Tables</h4>
                    </div>
                    <div class="card-body">
                        <div id="blackjackTables" class="game-tables">
                            <!-- Tables will be loaded via AJAX -->
                        </div>
                        <button class="btn btn-primary" onclick="loadBlackjackTables()">
                            <i class="fas fa-refresh"></i> Refresh Tables
                        </button>
                    </div>
                </div>
            </div>
            
            <!-- Slots Section -->
            <div class="col-md-6 mb-4">
                <div class="card">
                    <div class="card-header">
                        <h4><i class="fas fa-coins"></i> Slot Machines</h4>
                    </div>
                    <div class="card-body">
                        <div class="row">
                            <c:forEach items="${slotGames}" var="slot">
                                <div class="col-sm-6 mb-3">
                                    <div class="card">
                                        <div class="card-body text-center">
                                            <h6>${slot.name}</h6>
                                            <p class="small text-muted">Min: ${slot.minBet} | Max: ${slot.maxBet}</p>
                                            <button class="btn btn-success btn-sm" onclick="playSlot('${slot.id}')">
                                                Play Now
                                            </button>
                                        </div>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                    </div>
                </div>
            </div>
            
            <!-- Roulette Section -->
            <div class="col-md-6 mb-4">
                <div class="card">
                    <div class="card-header">
                        <h4><i class="fas fa-circle"></i> Roulette Tables</h4>
                    </div>
                    <div class="card-body">
                        <div id="rouletteTables" class="game-tables">
                            <!-- Tables will be loaded via AJAX -->
                        </div>
                        <button class="btn btn-primary" onclick="loadRouletteTables()">
                            <i class="fas fa-refresh"></i> Refresh Tables
                        </button>
                    </div>
                </div>
            </div>
        </div>
        
        <!-- Leaderboard -->
        <div class="row mt-4">
            <div class="col-12">
                <div class="card">
                    <div class="card-header">
                        <h4><i class="fas fa-trophy"></i> Leaderboard</h4>
                    </div>
                    <div class="card-body">
                        <div class="table-responsive">
                            <table class="table table-striped">
                                <thead>
                                    <tr>
                                        <th>Rank</th>
                                        <th>Player</th>
                                        <th>Total Chips</th>
                                        <th>Level</th>
                                    </tr>
                                </thead>
                                <tbody id="leaderboardBody">
                                    <!-- Leaderboard data loaded via AJAX -->
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <!-- WebSocket and Game Logic -->
    <script>
        let stompClient = null;
        let userToken = '${sessionScope.token}';
        
        $(document).ready(function() {
            connectWebSocket();
            loadGameTables();
            loadLeaderboard();
            
            // Update chip balance every 30 seconds
            setInterval(updateChipBalance, 30000);
        });
        
        function connectWebSocket() {
            const socket = new SockJS('/ws');
            stompClient = Stomp.over(socket);
            
            stompClient.connect({
                'Authorization': 'Bearer ' + userToken
            }, function(frame) {
                console.log('Connected: ' + frame);
                
                // Subscribe to personal notifications
                stompClient.subscribe('/user/queue/notifications', function(notification) {
                    showNotification(JSON.parse(notification.body));
                });
                
                // Subscribe to game updates
                stompClient.subscribe('/topic/lobby/updates', function(update) {
                    handleLobbyUpdate(JSON.parse(update.body));
                });
            });
        }
        
        function loadGameTables() {
            loadPokerTables();
            loadBlackjackTables();
            loadRouletteTables();
        }
        
        function loadPokerTables() {
            $.ajax({
                url: '/api/games/poker/tables',
                type: 'GET',
                headers: {
                    'Authorization': 'Bearer ' + userToken
                },
                success: function(response) {
                    if (response.success) {
                        displayPokerTables(response.data);
                    }
                },
                error: function(xhr) {
                    console.error('Failed to load poker tables:', xhr);
                }
            });
        }
        
        function displayPokerTables(tables) {
            let html = '';
            tables.forEach(function(table) {
                html += `
                    <div class="card mb-2">
                        <div class="card-body">
                            <div class="row align-items-center">
                                <div class="col-md-6">
                                    <h6 class="mb-1">${table.name}</h6>
                                    <small class="text-muted">
                                        Blinds: ${table.smallBlind}/${table.bigBlind} | 
                                        Players: ${table.currentPlayers}/${table.maxPlayers}
                                    </small>
                                </div>
                                <div class="col-md-6 text-end">
                                    <button class="btn btn-success btn-sm" 
                                            onclick="joinPokerTable('${table.id}')"
                                            ${table.currentPlayers >= table.maxPlayers ? 'disabled' : ''}>
                                        ${table.currentPlayers >= table.maxPlayers ? 'Full' : 'Join Table'}
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>
                `;
            });
            $('#pokerTables').html(html);
        }
        
        function joinPokerTable(tableId) {
            const buyIn = prompt('Enter buy-in amount (minimum 100 chips):');
            if (buyIn && parseInt(buyIn) >= 100) {
                $.ajax({
                    url: '/api/games/join',
                    type: 'POST',
                    headers: {
                        'Authorization': 'Bearer ' + userToken,
                        'Content-Type': 'application/json'
                    },
                    data: JSON.stringify({
                        gameId: tableId,
                        buyIn: parseInt(buyIn)
                    }),
                    success: function(response) {
                        if (response.success) {
                            window.location.href = '/game/poker/' + response.data.sessionId;
                        }
                    },
                    error: function(xhr) {
                        const error = xhr.responseJSON?.message || 'Failed to join table';
                        alert(error);
                    }
                });
            }
        }
        
        function claimDailyBonus() {
            $.ajax({
                url: '/api/chips/daily-bonus',
                type: 'POST',
                headers: {
                    'Authorization': 'Bearer ' + userToken
                },
                success: function(response) {
                    if (response.success) {
                        alert('Daily bonus claimed: ' + response.data.bonusAmount + ' chips!');
                        updateChipBalance();
                    }
                },
                error: function(xhr) {
                    const error = xhr.responseJSON?.message || 'Failed to claim daily bonus';
                    alert(error);
                }
            });
        }
        
        function updateChipBalance() {
            $.ajax({
                url: '/api/chips/balance',
                type: 'GET',
                headers: {
                    'Authorization': 'Bearer ' + userToken
                },
                success: function(response) {
                    if (response.success) {
                        $('#chipBalance').text(response.data.balance.toLocaleString());
                    }
                }
            });
        }
        
        function loadLeaderboard() {
            $.ajax({
                url: '/api/leaderboard',
                type: 'GET',
                success: function(response) {
                    if (response.success) {
                        displayLeaderboard(response.data);
                    }
                }
            });
        }
        
        function displayLeaderboard(players) {
            let html = '';
            players.forEach(function(player, index) {
                html += `
                    <tr>
                        <td>${index + 1}</td>
                        <td>${player.username}</td>
                        <td>${player.totalChips.toLocaleString()} chips</td>
                        <td>Level ${player.level}</td>
                    </tr>
                `;
            });
            $('#leaderboardBody').html(html);
        }
        
        function logout() {
            if (confirm('Are you sure you want to logout?')) {
                $.ajax({
                    url: '/api/auth/logout',
                    type: 'POST',
                    headers: {
                        'Authorization': 'Bearer ' + userToken
                    },
                    success: function() {
                        window.location.href = '/login';
                    }
                });
            }
        }
    </script>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
```

### **CODE ARCHITECTURE PATTERNS**

#### **Spring Boot Application Structure**
```
src/
├── main/
│   ├── java/com/virtualcasino/
│   │   ├── VirtualCasinoApplication.java
│   │   ├── config/
│   │   │   ├── SecurityConfig.java
│   │   │   ├── WebSocketConfig.java
│   │   │   ├── RedissonConfig.java
│   │   │   └── KafkaConfig.java
│   │   ├── controller/
│   │   │   ├── AuthController.java
│   │   │   ├── GameController.java
│   │   │   ├── ChipController.java
│   │   │   └── WebController.java
│   │   ├── service/
│   │   │   ├── AuthService.java
│   │   │   ├── GameService.java
│   │   │   ├── ChipService.java
│   │   │   └── NotificationService.java
│   │   ├── repository/
│   │   │   ├── UserRepository.java
│   │   │   ├── GameRepository.java
│   │   │   └── TransactionRepository.java
│   │   ├── model/
│   │   │   ├── entity/
│   │   │   ├── dto/
│   │   │   └── enums/
│   │   ├── security/
│   │   │   ├── JwtTokenUtil.java
│   │   │   ├── JwtRequestFilter.java
│   │   │   └── UserPrincipal.java
│   │   └── websocket/
│   │       ├── GameWebSocketController.java
│   │       └── UserInterceptor.java
│   ├── resources/
│   │   ├── application.yml
│   │   ├── db/migration/
│   │   └── static/
│   └── webapp/
│       └── WEB-INF/
│           └── jsp/
│               ├── auth/
│               ├── game/
│               └── common/
└── test/
    └── java/com/virtualcasino/
        ├── integration/
        ├── service/
        └── controller/
```

**Main Application Class:**
```java
@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling
@EnableKafka
public class VirtualCasinoApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(VirtualCasinoApplication.class, args);
    }
    
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration()
              .setMatchingStrategy(MatchingStrategies.STRICT)
              .setFieldMatchingEnabled(true)
              .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE);
        return mapper;
    }
    
    @Bean
    public SecureRandom secureRandom() {
        return new SecureRandom();
    }
    
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }
}
```

---

## 6. TESTING PHASE

### **TESTING STRATEGY**

#### **Unit Testing (Target: 85% Coverage with JaCoCo)**

**Spring Boot Service Tests (JUnit 5 + Mockito):**
```java
@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    
    @Mock
    private UserRepository userRepository;
    
    @Mock
    private PasswordEncoder passwordEncoder;
    
    @Mock
    private JwtTokenUtil jwtTokenUtil;
    
    @Mock
    private EmailService emailService;
    
    @InjectMocks
    private AuthService authService;
    
    @Test
    @DisplayName("Should register user successfully with valid data")
    void shouldRegisterUserSuccessfully() {
        // Given
        RegisterRequest request = RegisterRequest.builder()
                .username("testuser")
                .email("test@example.com")
                .password("Password123")
                .birthDate(LocalDate.of(1990, 1, 1))
                .build();
        
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        
        when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(userRepository.existsByUsername(request.getUsername())).thenReturn(false);
        when(passwordEncoder.encode(request.getPassword())).thenReturn("hashedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);
        
        // When
        AuthResponse response = authService.register(request);
        
        // Then
        assertThat(response).isNotNull();
        assertThat(response.getUserId()).isEqualTo(user.getId());
        
        verify(userRepository).save(any(User.class));
        verify(emailService).sendVerificationEmail(eq(user.getEmail()), anyString());
    }
    
    @Test
    @DisplayName("Should throw UserAlreadyExistsException when email already exists")
    void shouldThrowExceptionWhenEmailExists() {
        // Given
        RegisterRequest request = RegisterRequest.builder()
                .email("existing@example.com")
                .build();
        
        when(userRepository.existsByEmail(request.getEmail())).thenReturn(true);
        
        // When & Then
        assertThatThrownBy(() -> authService.register(request))
                .isInstanceOf(UserAlreadyExistsException.class)
                .hasMessage("User with email already exists");
    }
    
    @Test
    @DisplayName("Should authenticate user with valid credentials")
    void shouldAuthenticateUserWithValidCredentials() {
        // Given
        LoginRequest request = new LoginRequest("test@example.com", "Password123");
        
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setEmail(request.getEmail());
        user.setPasswordHash("hashedPassword");
        user.setIsActive(true);
        
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(request.getPassword(), user.getPasswordHash())).thenReturn(true);
        when(jwtTokenUtil.generateToken(any(UserDetails.class))).thenReturn("jwt-token");
        when(jwtTokenUtil.generateRefreshToken(any(UserDetails.class))).thenReturn("refresh-token");
        
        // When
        LoginResponse response = authService.authenticate(request);
        
        // Then
        assertThat(response).isNotNull();
        assertThat(response.getAccessToken()).isEqualTo("jwt-token");
        assertThat(response.getRefreshToken()).isEqualTo("refresh-token");
        assertThat(response.getUser().getEmail()).isEqualTo(user.getEmail());
    }
}
```

**Repository Tests with Testcontainers:**
```java
@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {
    
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15.13")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");
    
    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }
    
    @Autowired
    private TestEntityManager entityManager;
    
    @Autowired
    private UserRepository userRepository;
    
    @Test
    @DisplayName("Should find user by email")
    void shouldFindUserByEmail() {
        // Given
        User user = User.builder()
                .username("testuser")
                .email("test@example.com")
                .passwordHash("hashedPassword")
                .birthDate(LocalDate.of(1990, 1, 1))
                .chipsBalance(1000L)
                .build();
        
        entityManager.persistAndFlush(user);
        
        // When
        Optional<User> found = userRepository.findByEmail("test@example.com");
        
        // Then
        assertThat(found).isPresent();
        assertThat(found.get().getUsername()).isEqualTo("testuser");
        assertThat(found.get().getChipsBalance()).isEqualTo(1000L);
    }
    
    @Test
    @DisplayName("Should find top players by chips balance")
    void shouldFindTopPlayersByChipsBalance() {
        // Given
        List<User> users = Arrays.asList(
                createUser("user1", 5000L),
                createUser("user2", 3000L),
                createUser("user3", 7000L),
                createUser("user4", 1000L)
        );
        
        users.forEach(user -> entityManager.persistAndFlush(user));
        
        // When
        Pageable pageable = PageRequest.of(0, 3, Sort.by("chipsBalance").descending());
        List<User> topPlayers = userRepository.findTopPlayersByChipsBalance(pageable);
        
        // Then
        assertThat(topPlayers).hasSize(3);
        assertThat(topPlayers.get(0).getChipsBalance()).isEqualTo(7000L);
        assertThat(topPlayers.get(1).getChipsBalance()).isEqualTo(5000L);
        assertThat(topPlayers.get(2).getChipsBalance()).isEqualTo(3000L);
    }
    
    private User createUser(String username, Long chipsBalance) {
        return User.builder()
                .username(username)
                .email(username + "@example.com")
                .passwordHash("hashedPassword")
                .birthDate(LocalDate.of(1990, 1, 1))
                .chipsBalance(chipsBalance)
                .build();
    }
}
```

#### **Integration Testing**

**Spring Boot Web Layer Tests (MockMvc):**
```java
@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class AuthControllerIntegrationTest {
    
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15.13")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");
    
    @Container
    static GenericContainer<?> redis = new GenericContainer<>("redis:8.0")
            .withExposedPorts(6379);
    
    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.redis.host", redis::getHost);
        registry.add("spring.redis.port", redis::getFirstMappedPort);
    }
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Autowired
    private UserRepository userRepository;
    
    @Test
    @DisplayName("Should register user successfully with valid request")
    @Transactional
    void shouldRegisterUserSuccessfully() throws Exception {
        // Given
        RegisterRequest request = RegisterRequest.builder()
                .username("newuser")
                .email("newuser@example.com")
                .password("Password123")
                .birthDate(LocalDate.of(1995, 5, 15))
                .build();
        
        // When & Then
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Registration successful. Please verify your email."))
                .andExpect(jsonPath("$.data.userId").exists());
        
        // Verify user was saved
        Optional<User> savedUser = userRepository.findByEmail("newuser@example.com");
        assertThat(savedUser).isPresent();
        assertThat(savedUser.get().getChipsBalance()).isEqualTo(1000L);
    }
    
    @Test
    @DisplayName("Should return error for invalid registration data")
    void shouldReturnErrorForInvalidData() throws Exception {
        // Given
        RegisterRequest request = RegisterRequest.builder()
                .username("u") // Too short
                .email("invalid-email") // Invalid format
                .password("123") // Too short
                .birthDate(LocalDate.of(2010, 1, 1)) // Under 18
                .build();
        
        // When & Then
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpected(jsonPath("$.success").value(false));
    }
    
    @Test
    @DisplayName("Should login user with valid credentials")
    void shouldLoginWithValidCredentials() throws Exception {
        // Given
        User user = createAndSaveUser("loginuser@example.com", "hashedPassword123");
        
        LoginRequest request = new LoginRequest("loginuser@example.com", "Password123");
        
        // When & Then
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.accessToken").exists())
                .andExpect(jsonPath("$.data.refreshToken").exists())
                .andExpect(jsonPath("$.data.user.email").value("loginuser@example.com"));
    }
    
    private User createAndSaveUser(String email, String password) {
        User user = User.builder()
                .username("testuser")
                .email(email)
                .passwordHash(new BCryptPasswordEncoder().encode(password))
                .birthDate(LocalDate.of(1990, 1, 1))
                .chipsBalance(1000L)
                .isActive(true)
                .build();
        
        return userRepository.save(user);
    }
}
```

**Game Service Integration Tests:**
```java
@SpringBootTest
@Testcontainers
@Transactional
class GameServiceIntegrationTest {
    
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15.13");
    
    @Container
    static GenericContainer<?> redis = new GenericContainer<>("redis:8.0")
            .withExposedPorts(6379);
    
    @Container
    static KafkaContainer kafka = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.4.0"));
    
    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.redis.host", redis::getHost);
        registry.add("spring.redis.port", redis::getFirstMappedPort);
        registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
    }
    
    @Autowired
    private GameService gameService;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private GameRepository gameRepository;
    
    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;
    
    @Test
    @DisplayName("Should create poker game session and handle player actions")
    void shouldCreatePokerGameAndHandleActions() {
        // Given
        User player1 = createUser("player1@example.com", 2000L);
        User player2 = createUser("player2@example.com", 1500L);
        
        Game pokerGame = createPokerGame();
        
        // When - Join game
        JoinGameRequest joinRequest1 = new JoinGameRequest(pokerGame.getId(), 200L);
        JoinGameResponse response1 = gameService.joinGame(player1.getId(), joinRequest1);
        
        JoinGameRequest joinRequest2 = new JoinGameRequest(pokerGame.getId(), 200L);
        JoinGameResponse response2 = gameService.joinGame(player2.getId(), joinRequest2);
        
        // Then
        assertThat(response1.getSessionId()).isNotNull();
        assertThat(response2.getSessionId()).isNotNull();
        
        GameSession session = gameService.getGameSession(response1.getSessionId());
        assertThat(session.getPlayers()).hasSize(2);
        
        // When - Player action
        GameActionRequest actionRequest = new GameActionRequest(
                response1.getSessionId(), 
                GameAction.BET, 
                50L
        );
        
        GameActionResponse actionResponse = gameService.processPlayerAction(
                player1.getId(), 
                actionRequest
        );
        
        // Then
        assertThat(actionResponse.isSuccess()).isTrue();
        assertThat(actionResponse.getGameState().getCurrentPot()).isEqualTo(50L);
        
        // Verify chip balances updated
        User updatedPlayer1 = userRepository.findById(player1.getId()).orElseThrow();
        assertThat(updatedPlayer1.getChipsBalance()).isEqualTo(1750L); // 2000 - 200 (buy-in) - 50 (bet)
    }
    
    @Test
    @DisplayName("Should handle daily bonus claiming")
    void shouldHandleDailyBonusClaiming() {
        // Given
        User user = createUser("bonus@example.com", 100L);
        
        // When
        DailyBonusResponse response = gameService.claimDailyBonus(user.getId());
        
        // Then
        assertThat(response.getBonusAmount()).isEqualTo(500L);
        assertThat(response.getNewBalance()).isEqualTo(600L);
        
        // Verify user balance updated
        User updatedUser = userRepository.findById(user.getId()).orElseThrow();
        assertThat(updatedUser.getChipsBalance()).isEqualTo(600L);
        
        // Should throw exception if trying to claim again same day
        assertThatThrownBy(() -> gameService.claimDailyBonus(user.getId()))
                .isInstanceOf(DailyBonusAlreadyClaimedException.class);
    }
    
    private User createUser(String email, Long chipsBalance) {
        User user = User.builder()
                .username(email.split("@")[0])
                .email(email)
                .passwordHash("hashedPassword")
                .birthDate(LocalDate.of(1990, 1, 1))
                .chipsBalance(chipsBalance)
                .isActive(true)
                .build();
        
        return userRepository.save(user);
    }
    
    private Game createPokerGame() {
        Game game = Game.builder()
                .name("Texas Hold'em - Low Stakes")
                .type(GameType.POKER)
                .minBet(10)
                .maxBet(200)
                .maxPlayers(6)
                .status(GameStatus.ACTIVE)
                .build();
        
        return gameRepository.save(game);
    }
}
```

#### **End-to-End Testing (Selenium WebDriver)**

**User Journey Tests:**
```java
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class UserJourneyE2ETest {
    
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15.13");
    
    @Container
    static GenericContainer<?> redis = new GenericContainer<>("redis:8.0")
            .withExposedPorts(6379);
    
    @Container
    static BrowserWebDriverContainer<?> browser = new BrowserWebDriverContainer<>()
            .withCapabilities(new ChromeOptions());
    
    @LocalServerPort
    private int port;
    
    private WebDriver driver;
    
    @BeforeEach
    void setUp() {
        driver = browser.getWebDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }
    
    @Test
    @DisplayName("Complete user journey: register -> login -> play game")
    void completeUserJourney() {
        String baseUrl = "http://host.testcontainers.internal:" + port;
        
        // Step 1: Registration
        driver.get(baseUrl + "/register");
        
        WebElement usernameField = driver.findElement(By.id("username"));
        WebElement emailField = driver.findElement(By.id("email"));
        WebElement passwordField = driver.findElement(By.id("password"));
        WebElement birthDateField = driver.findElement(By.id("birthDate"));
        WebElement termsCheckbox = driver.findElement(By.id("terms"));
        WebElement submitButton = driver.findElement(By.id("submitBtn"));
        
        usernameField.sendKeys("e2euser");
        emailField.sendKeys("e2euser@example.com");
        passwordField.sendKeys("Password123");
        birthDateField.sendKeys("1990-01-01");
        termsCheckbox.click();
        submitButton.click();
        
        // Wait for registration success
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.urlContains("/login"));
        
        // Step 2: Login
        WebElement loginEmail = driver.findElement(By.id("email"));
        WebElement loginPassword = driver.findElement(By.id("password"));
        WebElement loginButton = driver.findElement(By.id("loginBtn"));
        
        loginEmail.sendKeys("e2euser@example.com");
        loginPassword.sendKeys("Password123");
        loginButton.click();
        
        // Wait for login success and redirect to lobby
        wait.until(ExpectedConditions.urlContains("/lobby"));
        
        // Step 3: Verify initial chip balance
        WebElement chipBalance = driver.findElement(By.id("chipBalance"));
        assertThat(chipBalance.getText()).contains("1,000");
        
        // Step 4: Claim daily bonus
        WebElement userDropdown = driver.findElement(By.cssSelector(".navbar-nav .dropdown-toggle"));
        userDropdown.click();
        
        WebElement dailyBonusLink = driver.findElement(By.linkText("Daily Bonus"));
        dailyBonusLink.click();
        
        // Handle alert
        wait.until(ExpectedConditions.alertIsPresent());
        Alert alert = driver.switchTo().alert();
        assertThat(alert.getText()).contains("Daily bonus claimed");
        alert.accept();
        
        // Verify updated balance
        wait.until(ExpectedConditions.textToBePresentInElement(chipBalance, "1,500"));
        
        // Step 5: Join a poker table
        WebElement pokerRefreshBtn = driver.findElement(By.xpath("//button[contains(text(), 'Refresh Tables')]"));
        pokerRefreshBtn.click();
        
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//button[contains(text(), 'Join Table')]")));
        
        WebElement joinTableBtn = driver.findElement(By.xpath("//button[contains(text(), 'Join Table')]"));
        joinTableBtn.click();
        
        // Handle buy-in prompt
        wait.until(ExpectedConditions.alertIsPresent());
        Alert buyInAlert = driver.switchTo().alert();
        buyInAlert.sendKeys("200");
        buyInAlert.accept();
        
        // Verify navigation to poker game
        wait.until(ExpectedConditions.urlContains("/game/poker/"));
        
        // Step 6: Verify poker game interface
        WebElement pokerTable = driver.findElement(By.cssSelector(".poker-table"));
        assertThat(pokerTable.isDisplayed()).isTrue();
        
        WebElement playerChips = driver.findElement(By.cssSelector(".player-chips"));
        assertThat(playerChips.getText()).contains("1,300"); // 1500 - 200 buy-in
    }
    
    @Test
    @DisplayName("Should handle form validation errors properly")
    void shouldHandleValidationErrors() {
        String baseUrl = "http://host.testcontainers.internal:" + port;
        
        driver.get(baseUrl + "/register");
        
        // Submit form with invalid data
        WebElement submitButton = driver.findElement(By.id("submitBtn"));
        submitButton.click();
        
        // Check for validation messages
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        
        List<WebElement> validationMessages = driver.findElements(By.cssSelector(".text-danger"));
        assertThat(validationMessages).isNotEmpty();
        
        // Fill with invalid email format
        WebElement emailField = driver.findElement(By.id("email"));
        emailField.sendKeys("invalid-email");
        submitButton.click();
        
        // Verify email validation
        WebElement emailValidation = driver.findElement(By.xpath("//input[@id='email']:invalid"));
        assertThat(emailValidation).isNotNull();
    }
}
```

### **PERFORMANCE TESTING**

#### **Load Testing with JMeter (Maven Plugin)**

**JMeter Test Plan Configuration:**
```xml
<!-- pom.xml JMeter plugin -->
<plugin>
    <groupId>com.lazerycode.jmeter</groupId>
    <artifactId>jmeter-maven-plugin</artifactId>
    <version>3.7.0</version>
    <executions>
        <execution>
            <id>performance-test</id>
            <goals>
                <goal>jmeter</goal>
            </goals>
        </execution>
    </executions>
    <configuration>
        <testFilesDirectory>src/test/jmeter</testFilesDirectory>
        <resultsDirectory>target/jmeter/results</resultsDirectory>
    </configuration>
</plugin>
```

**Spring Boot Performance Tests:**
```java
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class PerformanceTest {
    
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15.13");
    
    @LocalServerPort
    private int port;
    
    @Autowired
    private TestRestTemplate restTemplate;
    
    @Test
    @DisplayName("Should handle concurrent user registrations")
    void shouldHandleConcurrentRegistrations() throws InterruptedException {
        int numberOfThreads = 50;
        CountDownLatch latch = new CountDownLatch(numberOfThreads);
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger errorCount = new AtomicInteger(0);
        
        ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);
        
        for (int i = 0; i < numberOfThreads; i++) {
            final int userId = i;
            executor.submit(() -> {
                try {
                    RegisterRequest request = RegisterRequest.builder()
                            .username("user" + userId)
                            .email("user" + userId + "@example.com")
                            .password("Password123")
                            .birthDate(LocalDate.of(1990, 1, 1))
                            .build();
                    
                    ResponseEntity<ApiResponse> response = restTemplate.postForEntity(
                            "/api/auth/register",
                            request,
                            ApiResponse.class
                    );
                    
                    if (response.getStatusCode().is2xxSuccessful()) {
                        successCount.incrementAndGet();
                    } else {
                        errorCount.incrementAndGet();
                    }
                } catch (Exception e) {
                    errorCount.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }
        
        latch.await(30, TimeUnit.SECONDS);
        executor.shutdown();
        
        assertThat(successCount.get()).isEqualTo(numberOfThreads);
        assertThat(errorCount.get()).isEqualTo(0);
    }
    
    @Test
    @DisplayName("Should maintain response time under load")
    void shouldMaintainResponseTimeUnderLoad() throws InterruptedException {
        // Create test user first
        User testUser = createTestUser();
        String token = generateTokenForUser(testUser);
        
        int numberOfRequests = 100;
        CountDownLatch latch = new CountDownLatch(numberOfRequests);
        List<Long> responseTimes = Collections.synchronizedList(new ArrayList<>());
        
        ExecutorService executor = Executors.newFixedThreadPool(20);
        
        for (int i = 0; i < numberOfRequests; i++) {
            executor.submit(() -> {
                try {
                    long startTime = System.currentTimeMillis();
                    
                    HttpHeaders headers = new HttpHeaders();
                    headers.setBearerAuth(token);
                    HttpEntity<String> entity = new HttpEntity<>(headers);
                    
                    ResponseEntity<ApiResponse> response = restTemplate.exchange(
                            "/api/games",
                            HttpMethod.GET,
                            entity,
                            ApiResponse.class
                    );
                    
                    long endTime = System.currentTimeMillis();
                    responseTimes.add(endTime - startTime);
                    
                } finally {
                    latch.countDown();
                }
            });
        }
        
        latch.await(60, TimeUnit.SECONDS);
        executor.shutdown();
        
        // Assert average response time is under 500ms
        double averageResponseTime = responseTimes.stream()
                .mapToLong(Long::longValue)
                .average()
                .orElse(0.0);
        
        assertThat(averageResponseTime).isLessThan(500.0);
        
        // Assert 95th percentile is under 1000ms
        responseTimes.sort(Long::compareTo);
        long p95 = responseTimes.get((int) (responseTimes.size() * 0.95));
        assertThat(p95).isLessThan(1000L);
    }
}
```

### **SECURITY TESTING**

#### **Spring Security Tests:**
```java
@SpringBootTest
@AutoConfigureMockMvc
class SecurityTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Test
    @DisplayName("Should require authentication for protected endpoints")
    void shouldRequireAuthenticationForProtectedEndpoints() throws Exception {
        mockMvc.perform(get("/api/games"))
                .andExpect(status().isUnauthorized());
        
        mockMvc.perform(get("/api/chips/balance"))
                .andExpected(status().isUnauthorized());
        
        mockMvc.perform(post("/api/games/join"))
                .andExpected(status().isUnauthorized());
    }
    
    @Test
    @DisplayName("Should allow access to public endpoints")
    void shouldAllowAccessToPublicEndpoints() throws Exception {
        mockMvc.perform(get("/api/public/games"))
                .andExpect(status().isOk());
        
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpected(status().isBadRequest()); // Bad request due to validation, not auth
    }
    
    @Test
    @DisplayName("Should validate JWT tokens properly")
    void shouldValidateJwtTokensProperly() throws Exception {
        // Invalid token
        mockMvc.perform(get("/api/games")
                        .header("Authorization", "Bearer invalid-token"))
                .andExpected(status().isUnauthorized());
        
        // Malformed token
        mockMvc.perform(get("/api/games")
                        .header("Authorization", "Bearer "))
                .andExpected(status().isUnauthorized());
        
        // No Bearer prefix
        mockMvc.perform(get("/api/games")
                        .header("Authorization", "invalid-format"))
                .andExpected(status().isUnauthorized());
    }
    
    @Test
    @DisplayName("Should enforce role-based access control")
    @WithMockUser(roles = "USER")
    void shouldEnforceRoleBasedAccessControl() throws Exception {
        // Regular user should not access admin endpoints
        mockMvc.perform(get("/api/admin/users"))
                .andExpected(status().isForbidden());
    }
    
    @Test
    @DisplayName("Should prevent SQL injection attacks")
    void shouldPreventSqlInjectionAttacks() throws Exception {
        String maliciousInput = "'; DROP TABLE users; --";
        
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"" + maliciousInput + "\",\"password\":\"test\"}"))
                .andExpected(status().isBadRequest());
    }
}
```

### **TESTING SCHEDULE**

**Week 13: Unit Testing**
- Service layer tests with Mockito
- Repository tests with Testcontainers
- Utility and helper class tests
- JaCoCo coverage report generation

**Week 14: Integration Testing**
- Spring Boot web layer tests
- Database integration tests
- Kafka messaging tests
- Redis caching tests

**Week 15: System Testing**
- End-to-end user journeys with Selenium
- Performance testing with JMeter
- Security penetration testing
- Cross-browser compatibility testing

**Week 16: User Acceptance Testing**
- Stakeholder testing environment setup
- Test scenario execution
- Bug fixes and validation
- Production readiness verification

---

## 7. DEPLOYMENT PHASE

### **DEPLOYMENT ARCHITECTURE**

#### **Production Environment Setup**

**Infrastructure Overview:**
```
Internet → Nginx Load Balancer → Apache Tomcat EE (Spring Boot) → PostgreSQL 15.13
                    ↓                           ↓
               Static Assets               Redis 8.0 Cluster
                    ↓                           ↓
               CDN/S3                    Kafka Cluster
```

**Server Configuration:**

**Application Server (Tomcat EE):**
```bash
# /opt/tomcat/conf/server.xml
<Server port="8005" shutdown="SHUTDOWN">
    <Service name="Catalina">
        <Connector port="8080" protocol="HTTP/1.1"
                   connectionTimeout="20000"
                   redirectPort="8443"
                   maxThreads="200"
                   minSpareThreads="10"
                   enableLookups="false"
                   acceptCount="100"
                   URIEncoding="UTF-8"/>
        
        <Engine name="Catalina" defaultHost="localhost">
            <Host name="localhost" appBase="webapps"
                  unpackWARs="true" autoDeploy="true">
                  
                <Context path="/virtual-casino" 
                         docBase="/opt/apps/virtual-casino.war"
                         reloadable="false"/>
            </Host>
        </Engine>
    </Service>
</Server>
```

**Nginx Configuration:**
```nginx
# /etc/nginx/sites-available/virtual-casino
upstream tomcat_backend {
    least_conn;
    server 10.0.1.10:8080 max_fails=3 fail_timeout=30s;
    server 10.0.1.11:8080 max_fails=3 fail_timeout=30s;
    server 10.0.1.12:8080 backup;
}

upstream websocket_backend {
    ip_hash;
    server 10.0.1.10:8080;
    server 10.0.1.11:8080;
}

server {
    listen 80;
    server_name virtualcasino.com www.virtualcasino.com;
    return 301 https://$server_name$request_uri;
}

server {
    listen 443 ssl http2;
    server_name virtualcasino.com www.virtualcasino.com;
    
    # SSL Configuration
    ssl_certificate /etc/ssl/certs/virtualcasino.com.pem;
    ssl_certificate_key /etc/ssl/private/virtualcasino.com.key;
    ssl_protocols TLSv1.2 TLSv1.3;
    ssl_ciphers ECDHE-RSA-AES256-GCM-SHA512:DHE-RSA-AES256-GCM-SHA512;
    ssl_prefer_server_ciphers off;
    ssl_session_cache shared:SSL:10m;
    ssl_session_timeout 10m;
    
    # Security Headers
    add_header X-Frame-Options DENY;
    add_header X-Content-Type-Options nosniff;
    add_header X-XSS-Protection "1; mode=block";
    add_header Strict-Transport-Security "max-age=31536000; includeSubDomains" always;
    add_header Content-Security-Policy "default-src 'self'; script-src 'self' 'unsafe-inline' cdnjs.cloudflare.com; style-src 'self' 'unsafe-inline' cdn.jsdelivr.net fonts.googleapis.com; font-src 'self' fonts.gstatic.com; img-src 'self' data: https:; connect-src 'self' wss:";
    
    # Gzip Compression
    gzip on;
    gzip_vary on;
    gzip_min_length 1024;
    gzip_types text/plain text/css text/xml text/javascript application/javascript application/xml+rss application/json;
    
    # Static Assets
    location ~* \.(css|js|jpg|jpeg|png|gif|ico|svg|woff|woff2|ttf|eot)$ {
        expires 1y;
        add_header Cache-Control "public, immutable";
        access_log off;
        try_files $uri @backend;
    }
    
    # WebSocket Support
    location /ws/ {
        proxy_pass http://websocket_backend;
        proxy_http_version 1.1;
        proxy_set_header Upgrade $http_upgrade;
        proxy_set_header Connection "upgrade";
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
        proxy_cache_bypass $http_upgrade;
        proxy_read_timeout 86400;
    }
    
    # API Endpoints
    location /api/ {
        proxy_pass http://tomcat_backend;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
        proxy_connect_timeout 30s;
        proxy_send_timeout 30s;
        proxy_read_timeout 30s;
        
        # Rate Limiting
        limit_req zone=api burst=20 nodelay;
    }
    
    # Main Application
    location / {
        proxy_pass http://tomcat_backend;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
        proxy_connect_timeout 30s;
        proxy_send_timeout 30s;
        proxy_read_timeout 30s;
    }
    
    location @backend {
        proxy_pass http://tomcat_backend;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }
    
    # Rate Limiting Zones
    limit_req_zone $binary_remote_addr zone=api:10m rate=10r/s;
    limit_req_zone $binary_remote_addr zone=auth:10m rate=5r/m;
}
```

#### **Container Configuration**

**Spring Boot Application Dockerfile:**
```dockerfile
FROM openjdk:21-jdk-slim as builder

WORKDIR /app

# Copy Maven files
COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .

# Download dependencies
RUN ./mvnw dependency:go-offline -B

# Copy source code
COPY src ./src

# Build application
RUN ./mvnw clean package -DskipTests

# Production stage
FROM openjdk:21-jre-slim

WORKDIR /app

# Install required packages
RUN apt-get update && apt-get install -y \
    curl \
    dumb-init \
    && rm -rf /var/lib/apt/lists/*

# Create app user
RUN groupadd -r appuser && useradd -r -g appuser appuser

# Copy built application
COPY --from=builder /app/target/virtual-casino-*.war virtual-casino.war

# Set ownership
RUN chown -R appuser:appuser /app

USER appuser

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD curl -f http://localhost:8080/actuator/health || exit 1

EXPOSE 8080

ENTRYPOINT ["dumb-init", "--"]
CMD ["java", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=75.0", "-jar", "virtual-casino.war"]
```

**Docker Compose for Development:**
```yaml
# docker-compose.yml
version: '3.8'

services:
  app:
    build: .
    ports:
      - "8080:8080"
    environment:
      - SPRING_PROFILES_ACTIVE=docker
      - SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/virtualcasino
      - SPRING_DATASOURCE_USERNAME=postgres
      - SPRING_DATASOURCE_PASSWORD=password
      - SPRING_REDIS_HOST=redis
      - SPRING_KAFKA_BOOTSTRAP_SERVERS=kafka:9092
    depends_on:
      - postgres
      - redis
      - kafka
    volumes:
      - ./logs:/app/logs
    networks:
      - casino-network

  postgres:
    image: postgres:15.13
    environment:
      - POSTGRES_DB=virtualcasino
      - POSTGRES_USER=postgres
      - POSTGRES_PASSWORD=password
    ports:
      - "5432:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data
      - ./init.sql:/docker-entrypoint-initdb.d/init.sql
    networks:
      - casino-network

  redis:
    image: redis:8.0
    ports:
      - "6379:6379"
    command: redis-server --appendonly yes --requirepass redispassword
    volumes:
      - redis_data:/data
    networks:
      - casino-network

  zookeeper:
    image: confluentinc/cp-zookeeper:7.4.0
    environment:
      ZOOKEEPER_CLIENT_PORT: 2181
      ZOOKEEPER_TICK_TIME: 2000
    networks:
      - casino-network

  kafka:
    image: confluentinc/cp-kafka:7.4.0
    depends_on:
      - zookeeper
    ports:
      - "9092:9092"
    environment:
      KAFKA_BROKER_ID: 1
      KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181
      KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://kafka:9092
      KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR: 1
      KAFKA_AUTO_CREATE_TOPICS_ENABLE: true
    volumes:
      - kafka_data:/var/lib/kafka/data
    networks:
      - casino-network

  nginx:
    image: nginx:1.24
    ports:
      - "80:80"
      - "443:443"
    volumes:
      - ./nginx.conf:/etc/nginx/nginx.conf
      - ./ssl:/etc/ssl
    depends_on:
      - app
    networks:
      - casino-network

volumes:
  postgres_data:
  redis_data:
  kafka_data:

networks:
  casino-network:
    driver: bridge
```

### **CI/CD PIPELINE**

#### **Jenkins Pipeline Configuration:**
```groovy
// Jenkinsfile
pipeline {
    agent any
    
    tools {
        maven 'Maven-3.9'
        jdk 'OpenJDK-21'
    }
    
    environment {
        DOCKER_REGISTRY = 'your-registry.com'
        APP_NAME = 'virtual-casino'
        SONAR_TOKEN = credentials('sonar-token')
        DB_PASSWORD = credentials('db-password')
    }
    
    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        
        stage('Build') {
            steps {
                sh 'mvn clean compile'
            }
        }
        
        stage('Test') {
            steps {
                sh 'mvn test'
            }
            post {
                always {
                    publishTestResults testResultsPattern: 'target/surefire-reports/*.xml'
                    publishCoverage adapters: [
                        jacocoAdapter('target/site/jacoco/jacoco.xml')
                    ], sourceFileResolver: sourceFiles('STORE_LAST_BUILD')
                }
            }
        }
        
        stage('Code Quality') {
            steps {
                withSonarQubeEnv('SonarQube') {
                    sh '''
                        mvn sonar:sonar \
                        -Dsonar.projectKey=virtual-casino \
                        -Dsonar.host.url=$SONAR_HOST_URL \
                        -Dsonar.login=$SONAR_TOKEN
                    '''
                }
            }
        }
        
        stage('Quality Gate') {
            steps {
                timeout(time: 5, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }
        
        stage('Package') {
            steps {
                sh 'mvn package -DskipTests'
            }
            post {
                success {
                    archiveArtifacts artifacts: 'target/*.war', fingerprint: true
                }
            }
        }
        
        stage('Security Scan') {
            steps {
                sh 'mvn org.owasp:dependency-check-maven:check'
            }
            post {
                always {
                    publishHTML([
                        allowMissing: false,
                        alwaysLinkToLastBuild: true,
                        keepAll: true,
                        reportDir: 'target',
                        reportFiles: 'dependency-check-report.html',
                        reportName: 'OWASP Dependency Check'
                    ])
                }
            }
        }
        
        stage('Build Docker Image') {
            steps {
                script {
                    def image = docker.build("${DOCKER_REGISTRY}/${APP_NAME}:${BUILD_NUMBER}")
                    docker.withRegistry("https://${DOCKER_REGISTRY}", 'docker-registry-credentials') {
                        image.push()
                        image.push('latest')
                    }
                }
            }
        }
        
        stage('Deploy to Staging') {
            when {
                branch 'develop'
            }
            steps {
                script {
                    deployToEnvironment('staging')
                }
            }
        }
        
        stage('Integration Tests') {
            when {
                branch 'develop'
            }
            steps {
                sh 'mvn verify -Pintegration-tests -Dtest.environment=staging'
            }
        }
        
        stage('Deploy to Production') {
            when {
                branch 'main'
            }
            steps {
                script {
                    input message: 'Deploy to Production?', ok: 'Deploy'
                    deployToEnvironment('production')
                }
            }
        }
    }
    
    post {
        always {
            cleanWs()
        }
        failure {
            emailext (
                subject: "Build Failed: ${env.JOB_NAME} - ${env.BUILD_NUMBER}",
                body: "Build failed. Please check Jenkins for details.",
                to: "${env.CHANGE_AUTHOR_EMAIL}"
            )
        }
        success {
            emailext (
                subject: "Build Successful: ${env.JOB_NAME} - ${env.BUILD_NUMBER}",
                body: "Build completed successfully.",
                to: "${env.CHANGE_AUTHOR_EMAIL}"
            )
        }
    }
}

def deployToEnvironment(environment) {
    sh """
        docker-compose -f docker-compose.${environment}.yml down
        docker-compose -f docker-compose.${environment}.yml pull
        docker-compose -f docker-compose.${environment}.yml up -d
        
        # Wait for health check
        timeout 300 bash -c 'until curl -f http://localhost:8080/actuator/health; do sleep 5; done'
    """
}
```

#### **GitHub Actions Alternative:**
```yaml
# .github/workflows/ci-cd.yml
name: CI/CD Pipeline

on:
  push:
    branches: [ main, develop ]
  pull_request:
    branches: [ main ]

jobs:
  test:
    runs-on: ubuntu-latest
    
    services:
      postgres:
        image: postgres:15.13
        env:
          POSTGRES_PASSWORD: postgres
          POSTGRES_DB: testdb
        options: >-
          --health-cmd pg_isready
          --health-interval 10s
          --health-timeout 5s
          --health-retries 5
        ports:
          - 5432:5432
      
      redis:
        image: redis:8.0
        options: >-
          --health-cmd "redis-cli ping"
          --health-interval 10s
          --health-timeout 5s
          --health-retries 5
        ports:
          - 6379:6379
    
    steps:
    - uses: actions/checkout@v4
    
    - name: Set up JDK 21
      uses: actions/setup-java@v4
      with:
        java-version: '21'
        distribution: 'temurin'
    
    - name: Cache Maven dependencies
      uses: actions/cache@v3
      with:
        path: ~/.m2
        key: ${{ runner.os }}-m2-${{ hashFiles('**/pom.xml') }}
    
    - name: Run tests
      run: mvn clean test
      env:
        SPRING_DATASOURCE_URL: jdbc:postgresql://localhost:5432/testdb
        SPRING_DATASOURCE_USERNAME: postgres
        SPRING_DATASOURCE_PASSWORD: postgres
        SPRING_REDIS_HOST: localhost
    
    - name: Generate test report
      uses: dorny/test-reporter@v1
      if: success() || failure()
      with:
        name: Maven Tests
        path: target/surefire-reports/*.xml
        reporter: java-junit
    
    - name: Code Coverage
      run: mvn jacoco:report
    
    - name: Upload coverage to Codecov
      uses: codecov/codecov-action@v3
      with:
        file: target/site/jacoco/jacoco.xml

  security-scan:
    runs-on: ubuntu-latest
    steps:
    - uses: actions/checkout@v4
    
    - name: Set up JDK 21
      uses: actions/setup-java@v4
      with:
        java-version: '21'
        distribution: 'temurin'
    
    - name: OWASP Dependency Check
      run: mvn org.owasp:dependency-check-maven:check
    
    - name: Upload security report
      uses: actions/upload-artifact@v3
      with:
        name: security-report
        path: target/dependency-check-report.html

  build-and-deploy:
    needs: [test, security-scan]
    runs-on: ubuntu-latest
    if: github.ref == 'refs/heads/main'
    
    steps:
    - uses: actions/checkout@v4
    
    - name: Set up JDK 21
      uses: actions/setup-java@v4
      with:
        java-version: '21'
        distribution: 'temurin'
    
    - name: Build application
      run: mvn clean package -DskipTests
    
    - name: Build Docker image
      run: |
        docker build -t virtual-casino:${{ github.sha }} .
        docker tag virtual-casino:${{ github.sha }} virtual-casino:latest
    
    - name: Deploy to staging
      if: github.ref == 'refs/heads/develop'
      run: |
        # Deploy to staging environment
        echo "Deploying to staging..."
    
    - name: Deploy to production
      if: github.ref == 'refs/heads/main'
      run: |
        # Deploy to production environment
        echo "Deploying to production..."
```

### **DATABASE MIGRATION**

#### **Flyway Migration Scripts:**
```sql
-- V1__Initial_schema.sql
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    birth_date DATE NOT NULL,
    avatar_url VARCHAR(500),
    chips_balance BIGINT DEFAULT 1000,
    total_chips_won BIGINT DEFAULT 0,
    level INTEGER DEFAULT 1,
    experience_points INTEGER DEFAULT 0,
    is_premium BOOLEAN DEFAULT FALSE,
    is_active BOOLEAN DEFAULT TRUE,
    last_login TIMESTAMP,
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW()
);

CREATE TABLE games (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(100) NOT NULL,
    type VARCHAR(50) NOT NULL,
    status VARCHAR(20) DEFAULT 'ACTIVE',
    min_bet INTEGER DEFAULT 10,
    max_bet INTEGER DEFAULT 1000,
    max_players INTEGER DEFAULT 6,
    settings JSONB,
    created_at TIMESTAMP DEFAULT NOW()
);

CREATE TABLE game_sessions (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    game_id UUID REFERENCES games(id),
    session_data JSONB,
    status VARCHAR(20) DEFAULT 'ACTIVE',
    started_at TIMESTAMP DEFAULT NOW(),
    ended_at TIMESTAMP
);

CREATE TABLE user_game_history (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID REFERENCES users(id),
    game_id UUID REFERENCES games(id),
    session_id UUID REFERENCES game_sessions(id),
    chips_before BIGINT NOT NULL,
    chips_after BIGINT NOT NULL,
    chips_wagered BIGINT NOT NULL,
    chips_won BIGINT DEFAULT 0,
    game_data JSONB,
    played_at TIMESTAMP DEFAULT NOW()
);

CREATE TABLE transactions (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID REFERENCES users(id),
    type VARCHAR(50) NOT NULL,
    amount BIGINT NOT NULL,
    balance_before BIGINT NOT NULL,
    balance_after BIGINT NOT NULL,
    reference_id UUID,
    description TEXT,
    created_at TIMESTAMP DEFAULT NOW()
);

-- Indexes for performance
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_users_created_at ON users(created_at);
CREATE INDEX idx_users_chips_balance ON users(chips_balance);
CREATE INDEX idx_game_sessions_game_id ON game_sessions(game_id);
CREATE INDEX idx_game_sessions_status ON game_sessions(status);
CREATE INDEX idx_user_game_history_user_id ON user_game_history(user_id);
CREATE INDEX idx_user_game_history_played_at ON user_game_history(played_at);
CREATE INDEX idx_transactions_user_id ON transactions(user_id);
CREATE INDEX idx_transactions_type ON transactions(type);
CREATE INDEX idx_transactions_created_at ON transactions(created_at);
```

```sql
-- V2__Add_additional_indexes.sql
-- Additional indexes for better query performance
CREATE INDEX idx_users_last_login ON users(last_login);
CREATE INDEX idx_users_level ON users(level);
CREATE INDEX idx_game_sessions_started_at ON game_sessions(started_at);
CREATE INDEX idx_user_game_history_game_id ON user_game_history(game_id);
CREATE INDEX idx_user_game_history_session_id ON user_game_history(session_id);

-- Composite indexes for common queries
CREATE INDEX idx_users_active_chips ON users(is_active, chips_balance DESC);
CREATE INDEX idx_transactions_user_type_date ON transactions(user_id, type, created_at);
CREATE INDEX idx_game_history_user_date ON user_game_history(user_id, played_at DESC);
```

```sql
-- V3__Add_tournament_support.sql
CREATE TABLE tournaments (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(100) NOT NULL,
    type VARCHAR(50) NOT NULL,
    entry_fee BIGINT NOT NULL,
    max_participants INTEGER NOT NULL,
    current_participants INTEGER DEFAULT 0,
    prize_pool BIGINT DEFAULT 0,
    status VARCHAR(20) DEFAULT 'SCHEDULED',
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP,
    settings JSONB,
    created_at TIMESTAMP DEFAULT NOW()
);

CREATE TABLE tournament_participants (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    tournament_id UUID REFERENCES tournaments(id),
    user_id UUID REFERENCES users(id),
    entry_fee_paid BIGINT NOT NULL,
    current_position INTEGER,
    final_position INTEGER,
    prize_won BIGINT DEFAULT 0,
    joined_at TIMESTAMP DEFAULT NOW(),
    UNIQUE(tournament_id, user_id)
);

CREATE INDEX idx_tournaments_status ON tournaments(status);
CREATE INDEX idx_tournaments_start_time ON tournaments(start_time);
CREATE INDEX idx_tournament_participants_tournament_id ON tournament_participants(tournament_id);
CREATE INDEX idx_tournament_participants_user_id ON tournament_participants(user_id);
```

#### **Production Database Setup Script:**
```bash
#!/bin/bash
# deploy/db-setup.sh

set -e

echo "Setting up production database..."

# Database connection parameters
DB_HOST=${DB_HOST:-localhost}
DB_PORT=${DB_PORT:-5432}
DB_NAME=${DB_NAME:-virtualcasino}
DB_USER=${DB_USER:-postgres}
export PGPASSWORD=${DB_PASSWORD}

# Wait for database to be ready
echo "Waiting for database to be ready..."
until pg_isready -h $DB_HOST -p $DB_PORT -U $DB_USER; do
    echo "Database is not ready yet. Waiting..."
    sleep 2
done

echo "Database is ready. Running migrations..."

# Run Flyway migrations
mvn flyway:migrate \
    -Dflyway.url=jdbc:postgresql://${DB_HOST}:${DB_PORT}/${DB_NAME} \
    -Dflyway.user=${DB_USER} \
    -Dflyway.password=${DB_PASSWORD}

echo "Migrations completed successfully."

# Create additional indexes if needed
psql -h $DB_HOST -p $DB_PORT -U $DB_USER -d $DB_NAME -c "
-- Performance optimizations
ANALYZE;

-- Create materialized view for leaderboard
CREATE MATERIALIZED VIEW IF NOT EXISTS leaderboard_view AS
SELECT 
    u.id,
    u.username,
    u.avatar_url,
    u.chips_balance,
    u.total_chips_won,
    u.level,
    COUNT(ugh.id) as games_played,
    RANK() OVER (ORDER BY u.chips_balance DESC) as rank
FROM users u
LEFT JOIN user_game_history ugh ON u.id = ugh.user_id
WHERE u.is_active = true
GROUP BY u.id, u.username, u.avatar_url, u.chips_balance, u.total_chips_won, u.level
ORDER BY u.chips_balance DESC
LIMIT 100;

CREATE UNIQUE INDEX IF NOT EXISTS idx_leaderboard_view_id ON leaderboard_view(id);
CREATE INDEX IF NOT EXISTS idx_leaderboard_view_rank ON leaderboard_view(rank);

-- Refresh schedule (run every hour)
COMMENT ON MATERIALIZED VIEW leaderboard_view IS 'Refreshed hourly via cron job';
"

echo "Database setup completed successfully."
```

### **ENVIRONMENT CONFIGURATION**

#### **Production Application Properties:**
```yaml
# application-production.yml
server:
  port: 8080
  servlet:
    context-path: /virtual-casino
  tomcat:
    max-threads: 200
    min-spare-threads: 10
    max-connections: 8192
    accept-count: 100
    connection-timeout: 20000

spring:
  profiles:
    active: production
    
  datasource:
    url: jdbc:postgresql://${DB_HOST:localhost}:${DB_PORT:5432}/${DB_NAME:virtualcasino}
    username: ${DB_USERNAME:postgres}
    password: ${DB_PASSWORD}
    driver-class-name: org.postgresql.Driver
    hikari:
      maximum-pool-size: 20
      minimum-idle: 5
      connection-timeout: 30000
      idle-timeout: 600000
      max-lifetime: 1800000
      leak-detection-threshold: 60000
      
  jpa:
    hibernate:
      ddl-auto: validate
    show-sql: false
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect
        format_sql: true
        jdbc:
          batch_size: 25
        order_inserts: true
        order_updates: true
        generate_statistics: false
        
  cache:
    type: redis
    redis:
      time-to-live: 3600000 # 1 hour
      
  redis:
    host: ${REDIS_HOST:localhost}
    port: ${REDIS_PORT:6379}
    password: ${REDIS_PASSWORD}
    timeout: 2000ms
    lettuce:
      pool:
        max-active: 20
        max-idle: 10
        min-idle: 5
        max-wait: 1000ms
        
  kafka:
    bootstrap-servers: ${KAFKA_BOOTSTRAP_SERVERS:localhost:9092}
    producer:
      key-serializer: org.apache.kafka.common.serialization.StringSerializer
      value-serializer: org.springframework.kafka.support.serializer.JsonSerializer
      acks: all
      retries: 3
      properties:
        enable.idempotence: true
        max.in.flight.requests.per.connection: 5
    consumer:
      group-id: virtual-casino-group
      key-deserializer: org.apache.kafka.common.serialization.StringDeserializer
      value-deserializer: org.springframework.kafka.support.serializer.JsonDeserializer
      auto-offset-reset: earliest
      properties:
        spring.json.trusted.packages: "com.virtualcasino.model"
        
  mail:
    host: ${SMTP_HOST:smtp.gmail.com}
    port: ${SMTP_PORT:587}
    username: ${SMTP_USERNAME}
    password: ${SMTP_PASSWORD}
    properties:
      mail:
        smtp:
          auth: true
          starttls:
            enable: true
            
  mvc:
    view:
      prefix: /WEB-INF/jsp/
      suffix: .jsp
      
  web:
    resources:
      static-locations: classpath:/static/
      cache:
        cachecontrol:
          max-age: 31536000 # 1 year for static resources

# Security Configuration
security:
  jwt:
    secret: ${JWT_SECRET}
    expiration: 86400000 # 24 hours
    refresh-expiration: 604800000 # 7 days
    
# Application Configuration
app:
  security:
    cors:
      allowed-origins: ${ALLOWED_ORIGINS:https://virtualcasino.com}
      allowed-methods: GET,POST,PUT,DELETE,OPTIONS
      allowed-headers: "*"
      allow-credentials: true
      
  game:
    poker:
      max-tables: 50
      max-players-per-table: 6
      default-blind-structure: "10/20,25/50,50/100"
    blackjack:
      max-tables: 30
      deck-count: 6
      shuffle-point: 0.75
    slots:
      max-concurrent-spins: 1000
      rtp-percentage: 96.5
      
  chips:
    daily-bonus: 500
    initial-balance: 1000
    max-gift-amount: 100
    max-daily-gifts: 5

# Logging Configuration
logging:
  level:
    com.virtualcasino: INFO
    org.springframework.security: WARN
    org.hibernate.SQL: WARN
    org.springframework.kafka: INFO
  pattern:
    console: "%d{yyyy-MM-dd HH:mm:ss} - %msg%n"
    file: "%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n"
  file:
    name: /var/log/virtual-casino/application.log
    max-size: 100MB
    max-history: 30

# Actuator Configuration
management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics,prometheus
  endpoint:
    health:
      show-details: when-authorized
  metrics:
    export:
      prometheus:
        enabled: true
  health:
    redis:
      enabled: true
    db:
      enabled: true

# Custom Properties
virtualcasino:
  version: @project.version@
  build-time: @maven.build.timestamp@
  environment: production
```

#### **Environment Variables Template:**
```bash
# .env.production
# Database Configuration
DB_HOST=your-postgres-host
DB_PORT=5432
DB_NAME=virtualcasino
DB_USERNAME=virtualcasino_user
DB_PASSWORD=your-secure-db-password

# Redis Configuration
REDIS_HOST=your-redis-host
REDIS_PORT=6379
REDIS_PASSWORD=your-redis-password

# Kafka Configuration
KAFKA_BOOTSTRAP_SERVERS=kafka1:9092,kafka2:9092,kafka3:9092

# Security
JWT_SECRET=your-256-bit-secret-key-here-make-it-very-long-and-random
ALLOWED_ORIGINS=https://virtualcasino.com,https://www.virtualcasino.com

# Email Configuration
SMTP_HOST=smtp.gmail.com
SMTP_PORT=587
SMTP_USERNAME=noreply@virtualcasino.com
SMTP_PASSWORD=your-email-app-password

# AWS Configuration (if using S3)
AWS_ACCESS_KEY_ID=AKIAXXXXXXXXXXXXX
AWS_SECRET_ACCESS_KEY=your-secret-access-key
AWS_S3_BUCKET=virtualcasino-assets
AWS_REGION=us-east-1

# Monitoring
SENTRY_DSN=https://your-sentry-dsn@sentry.io/project-id
DATADOG_API_KEY=your-datadog-api-key

# SSL
SSL_KEYSTORE_PATH=/etc/ssl/keystore.p12
SSL_KEYSTORE_PASSWORD=your-keystore-password
SSL_KEY_ALIAS=virtualcasino

# JVM Options
JAVA_OPTS=-Xms1g -Xmx2g -XX:+UseG1GC -XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0
```

### **MONITORING & LOGGING**

#### **Spring Boot Actuator Configuration:**
```java
@Configuration
@EnableConfigurationProperties(ManagementProperties.class)
public class MonitoringConfig {
    
    @Bean
    public MeterRegistryCustomizer<MeterRegistry> metricsCommonTags() {
        return registry -> registry.config().commonTags("application", "virtual-casino");
    }
    
    @Bean
    public TimedAspect timedAspect(MeterRegistry registry) {
        return new TimedAspect(registry);
    }
    
    @Bean
    @ConditionalOnProperty(value = "management.metrics.export.prometheus.enabled")
    public PrometheusMeterRegistry prometheusMeterRegistry() {
        return new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);
    }
}
```

**Custom Metrics:**
```java
@Component
@Slf4j
public class GameMetrics {
    
    private final MeterRegistry meterRegistry;
    private final Counter gameStartCounter;
    private final Counter gameEndCounter;
    private final Timer gameSessionTimer;
    private final Gauge activePlayersGauge;
    
    public GameMetrics(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
        
        this.gameStartCounter = Counter.builder("virtual_casino_games_started_total")
                .description("Total number of games started")
                .tag("type", "all")
                .register(meterRegistry);
                
        this.gameEndCounter = Counter.builder("virtual_casino_games_completed_total")
                .description("Total number of games completed")
                .tag("type", "all")
                .register(meterRegistry);
                
        this.gameSessionTimer = Timer.builder("virtual_casino_game_session_duration")
                .description("Game session duration in seconds")
                .register(meterRegistry);
                
        this.activePlayersGauge = Gauge.builder("virtual_casino_active_players")
                .description("Number of currently active players")
                .register(meterRegistry, this, GameMetrics::getActivePlayerCount);
    }
    
    public void recordGameStart(GameType gameType) {
        gameStartCounter.increment(Tags.of("game_type", gameType.name().toLowerCase()));
        log.info("Game started: {}", gameType);
    }
    
    public void recordGameEnd(GameType gameType, Duration duration) {
        gameEndCounter.increment(Tags.of("game_type", gameType.name().toLowerCase()));
        gameSessionTimer.record(duration);
        log.info("Game completed: {} in {}", gameType, duration);
    }
    
    public void recordChipTransaction(TransactionType type, Long amount) {
        Counter.builder("virtual_casino_chip_transactions_total")
                .description("Total chip transactions")
                .tag("type", type.name().toLowerCase())
                .register(meterRegistry)
                .increment();
                
        DistributionSummary.builder("virtual_casino_chip_transaction_amount")
                .description("Chip transaction amounts")
                .tag("type", type.name().toLowerCase())
                .register(meterRegistry)
                .record(amount);
    }
    
    private double getActivePlayerCount() {
        // Implementation to get current active player count
        return 0.0; // Placeholder
    }
}
```

#### **Application Logging Configuration:**
```xml
<!-- logback-spring.xml -->
<?xml version="1.0" encoding="UTF-8"?>
<configuration>
    <include resource="org/springframework/boot/logging/logback/defaults.xml"/>
    
    <!-- Console Appender -->
    <appender name="CONSOLE" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</pattern>
        </encoder>
    </appender>
    
    <!-- File Appender -->
    <appender name="FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <file>/var/log/virtual-casino/application.log</file>
        <encoder>
            <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</pattern>
        </encoder>
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <fileNamePattern>/var/log/virtual-casino/application.%d{yyyy-MM-dd}.%i.log</fileNamePattern>
            <timeBasedFileNamingAndTriggeringPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedFNATP">
                <maxFileSize>100MB</maxFileSize>
            </timeBasedFileNamingAndTriggeringPolicy>
            <maxHistory>30</maxHistory>
            <totalSizeCap>3GB</totalSizeCap>
        </rollingPolicy>
    </appender>
    
    <!-- Game Events Appender -->
    <appender name="GAME_EVENTS" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <file>/var/log/virtual-casino/game-events.log</file>
        <encoder class="net.logstash.logback.encoder.LoggingEventCompositeJsonEncoder">
            <providers>
                <timestamp/>
                <logLevel/>
                <loggerName/>
                <message/>
                <mdc/>
                <arguments/>
            </providers>
        </encoder>
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <fileNamePattern>/var/log/virtual-casino/game-events.%d{yyyy-MM-dd}.log</fileNamePattern>
            <maxHistory>90</maxHistory>
        </rollingPolicy>
    </appender>
    
    <!-- Security Events Appender -->
    <appender name="SECURITY" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <file>/var/log/virtual-casino/security.log</file>
        <encoder class="net.logstash.logback.encoder.LoggingEventCompositeJsonEncoder">
            <providers>
                <timestamp/>
                <logLevel/>
                <loggerName/>
                <message/>
                <mdc/>
                <arguments/>
            </providers>
        </encoder>
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <fileNamePattern>/var/log/virtual-casino/security.%d{yyyy-MM-dd}.log</fileNamePattern>
            <maxHistory>365</maxHistory>
        </rollingPolicy>
    </appender>
    
    <!-- Async Appenders for Performance -->
    <appender name="ASYNC_FILE" class="ch.qos.logback.classic.AsyncAppender">
        <appender-ref ref="FILE"/>
        <queueSize>256</queueSize>
        <discardingThreshold>0</discardingThreshold>
    </appender>
    
    <appender name="ASYNC_GAME_EVENTS" class="ch.qos.logback.classic.AsyncAppender">
        <appender-ref ref="GAME_EVENTS"/>
        <queueSize>256</queueSize>
        <discardingThreshold>0</discardingThreshold>
    </appender>
    
    <!-- Logger Configurations -->
    <logger name="com.virtualcasino.service.GameService" level="INFO" additivity="false">
        <appender-ref ref="ASYNC_GAME_EVENTS"/>
        <appender-ref ref="CONSOLE"/>
    </logger>
    
    <logger name="com.virtualcasino.security" level="INFO" additivity="false">
        <appender-ref ref="SECURITY"/>
        <appender-ref ref="CONSOLE"/>
    </logger>
    
    <logger name="org.springframework.security" level="WARN"/>
    <logger name="org.hibernate.SQL" level="DEBUG"/>
    <logger name="org.hibernate.type.descriptor.sql.BasicBinder" level="TRACE"/>
    <logger name="org.apache.kafka" level="WARN"/>
    <logger name="org.redisson" level="WARN"/>
    
    <!-- Root Logger -->
    <root level="INFO">
        <appender-ref ref="CONSOLE"/>
        <appender-ref ref="ASYNC_FILE"/>
    </root>
    
    <!-- Profile-specific configurations -->
    <springProfile name="production">
        <root level="WARN">
            <appender-ref ref="ASYNC_FILE"/>
        </root>
    </springProfile>
    
    <springProfile name="development">
        <logger name="com.virtualcasino" level="DEBUG"/>
        <logger name="org.springframework.web" level="DEBUG"/>
    </springProfile>
</configuration>
```

#### **Prometheus Metrics Configuration:**
```yaml
# prometheus.yml
global:
  scrape_interval: 15s
  evaluation_interval: 15s

rule_files:
  - "virtual_casino_rules.yml"

scrape_configs:
  - job_name: 'virtual-casino'
    static_configs:
      - targets: ['localhost:8080']
    metrics_path: '/actuator/prometheus'
    scrape_interval: 5s
    
  - job_name: 'postgres'
    static_configs:
      - targets: ['localhost:9187']
      
  - job_name: 'redis'
    static_configs:
      - targets: ['localhost:9121']
      
  - job_name: 'nginx'
    static_configs:
      - targets: ['localhost:9113']

alerting:
  alertmanagers:
    - static_configs:
        - targets:
          - alertmanager:9093
```

**Alerting Rules:**
```yaml
# virtual_casino_rules.yml
groups:
  - name: virtual_casino_alerts
    rules:
      - alert: HighErrorRate
        expr: rate(http_server_requests_seconds_count{status=~"5.."}[5m]) > 0.1
        for: 5m
        labels:
          severity: critical
        annotations:
          summary: "High error rate detected"
          description: "Error rate is {{ $value }} errors per second"
          
      - alert: HighResponseTime
        expr: histogram_quantile(0.95, rate(http_server_requests_seconds_bucket[5m])) > 1
        for: 5m
        labels:
          severity: warning
        annotations:
          summary: "High response time detected"
          description: "95th percentile response time is {{ $value }} seconds"
          
      - alert: DatabaseConnectionsHigh
        expr: hikaricp_connections_active / hikaricp_connections_max > 0.8
        for: 2m
        labels:
          severity: warning
        annotations:
          summary: "Database connections usage high"
          description: "Database connection pool usage is {{ $value }}%"
          
      - alert: RedisConnectionFailure
        expr: up{job="redis"} == 0
        for: 1m
        labels:
          severity: critical
        annotations:
          summary: "Redis connection failure"
          description: "Redis server is down"
          
      - alert: LowChipBalance
        expr: virtual_casino_total_chips < 1000000
        for: 5m
        labels:
          severity: warning
        annotations:
          summary: "Low total chip balance in system"
          description: "Total chips in system: {{ $value }}"
```

### **SECURITY HARDENING**

#### **Spring Security Production Configuration:**
```java
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class ProductionSecurityConfig {
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .maximumSessions(1)
                .maxSessionsPreventsLogin(false)
            )
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf.disable())
            .headers(headers -> headers
                .frameOptions().deny()
                .contentTypeOptions().and()
                .httpStrictTransportSecurity(hstsConfig -> hstsConfig
                    .maxAgeInSeconds(31536000)
                    .includeSubdomains(true)
                    .preload(true)
                )
                .and()
                .addHeaderWriter(new StaticHeadersWriter(
                    "Content-Security-Policy",
                    "default-src 'self'; " +
                    "script-src 'self' 'unsafe-inline' cdnjs.cloudflare.com; " +
                    "style-src 'self' 'unsafe-inline' cdn.jsdelivr.net fonts.googleapis.com; " +
                    "font-src 'self' fonts.gstatic.com; " +
                    "img-src 'self' data: https:; " +
                    "connect-src 'self' wss:; " +
                    "frame-ancestors 'none';"
                ))
            )
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/api/public/**", "/ws/**", "/actuator/health").permitAll()
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/", "/login", "/register").permitAll()
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .exceptionHandling(exception -> exception
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
            )
            .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
            
        return http.build();
    }
    
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Arrays.asList(
            "https://virtualcasino.com",
            "https://*.virtualcasino.com"
        ));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }
}
```

#### **Rate Limiting with Redis:**
```java
@Component
@Slf4j
public class RateLimitingService {
    
    @Autowired
    private RedissonClient redissonClient;
    
    public boolean isAllowed(String key, int maxRequests, Duration timeWindow) {
        RRateLimiter rateLimiter = redissonClient.getRateLimiter(key);
        
        if (!rateLimiter.isExists()) {
            rateLimiter.trySetRate(RateType.OVERALL, maxRequests, timeWindow.getSeconds(), RateIntervalUnit.SECONDS);
        }
        
        boolean allowed = rateLimiter.tryAcquire();
        
        if (!allowed) {
            log.warn("Rate limit exceeded for key: {}", key);
        }
        
        return allowed;
    }
}

@RestControllerAdvice
public class RateLimitingInterceptor implements HandlerInterceptor {
    
    @Autowired
    private RateLimitingService rateLimitingService;
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String clientIP = getClientIP(request);
        String endpoint = request.getRequestURI();
        
        // Apply different rate limits based on endpoint
        if (endpoint.startsWith("/api/auth/")) {
            if (!rateLimitingService.isAllowed("auth:" + clientIP, 5, Duration.ofMinutes(1))) {
                response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
                response.getWriter().write("{\"error\":\"Too many authentication attempts\"}");
                return false;
            }
        } else if (endpoint.startsWith("/api/")) {
            if (!rateLimitingService.isAllowed("api:" + clientIP, 100, Duration.ofMinutes(1))) {
                response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
                response.getWriter().write("{\"error\":\"Rate limit exceeded\"}");
                return false;
            }
        }
        
        return true;
    }
    
    private String getClientIP(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}
```

### **BACKUP & DISASTER RECOVERY**

#### **Database Backup Strategy:**
```bash
#!/bin/bash
# scripts/backup-database.sh

set -e

# Configuration
BACKUP_DIR="/var/backups/virtual-casino"
DB_HOST="${DB_HOST:-localhost}"
DB_PORT="${DB_PORT:-5432}"
DB_NAME="${DB_NAME:-virtualcasino}"
DB_USER="${DB_USER:-postgres}"
RETENTION_DAYS=30
S3_BUCKET="virtualcasino-backups"

# Create backup directory
mkdir -p ${BACKUP_DIR}

# Generate timestamp
TIMESTAMP=$(date +%Y%m%d_%H%M%S)
BACKUP_FILE="${BACKUP_DIR}/virtualcasino_${TIMESTAMP}.sql"

echo "Starting database backup: ${BACKUP_FILE}"

# Create database backup
PGPASSWORD=${DB_PASSWORD} pg_dump \
    -h ${DB_HOST} \
    -p ${DB_PORT} \
    -U ${DB_USER} \
    -d ${DB_NAME} \
    --verbose \
    --no-password \
    --format=custom \
    --compress=9 \
    --file=${BACKUP_FILE}

if [ $? -eq 0 ]; then
    echo "Database backup completed successfully"
    
    # Compress backup
    gzip ${BACKUP_FILE}
    COMPRESSED_FILE="${BACKUP_FILE}.gz"
    
    # Upload to S3
    aws s3 cp ${COMPRESSED_FILE} s3://${S3_BUCKET}/daily/$(basename ${COMPRESSED_FILE})
    
    if [ $? -eq 0 ]; then
        echo "Backup uploaded to S3 successfully"
        # Remove local backup after successful upload
        rm ${COMPRESSED_FILE}
    else
        echo "Failed to upload backup to S3"
        exit 1
    fi
    
    # Clean up old backups
    find ${BACKUP_DIR} -name "virtualcasino_*.sql.gz" -mtime +${RETENTION_DAYS} -delete
    
    # Weekly backup retention
    if [ $(date +%u) -eq 1 ]; then
        WEEKLY_BACKUP="virtualcasino_weekly_${TIMESTAMP}.sql.gz"
        aws s3 cp s3://${S3_BUCKET}/daily/$(basename ${COMPRESSED_FILE}) s3://${S3_BUCKET}/weekly/${WEEKLY_BACKUP}
    fi
    
    # Monthly backup retention
    if [ $(date +%d) -eq 01 ]; then
        MONTHLY_BACKUP="virtualcasino_monthly_${TIMESTAMP}.sql.gz"
        aws s3 cp s3://${S3_BUCKET}/daily/$(basename ${COMPRESSED_FILE}) s3://${S3_BUCKET}/monthly/${MONTHLY_BACKUP}
    fi
    
else
    echo "Database backup failed"
    exit 1
fi

echo "Backup process completed"
```

#### **Application Data Backup:**
```bash
#!/bin/bash
# scripts/backup-application.sh

set -e

BACKUP_DIR="/var/backups/virtual-casino"
APP_DIR="/opt/virtual-casino"
TIMESTAMP=$(date +%Y%m%d_%H%M%S)

echo "Starting application backup"

# Backup configuration files
tar -czf ${BACKUP_DIR}/config_${TIMESTAMP}.tar.gz \
    /opt/virtual-casino/config/ \
    /etc/nginx/sites-available/virtual-casino \
    /opt/tomcat/conf/server.xml

# Backup application logs
tar -czf ${BACKUP_DIR}/logs_${TIMESTAMP}.tar.gz \
    /var/log/virtual-casino/ \
    /opt/tomcat/logs/

# Backup Redis data (if using RDB persistence)
if [ -f /var/lib/redis/dump.rdb ]; then
    cp /var/lib/redis/dump.rdb ${BACKUP_DIR}/redis_${TIMESTAMP}.rdb
    gzip ${BACKUP_DIR}/redis_${TIMESTAMP}.rdb
fi

# Upload to S3
aws s3 sync ${BACKUP_DIR} s3://virtualcasino-backups/application/

echo "Application backup completed"
```

#### **Disaster Recovery Procedures:**
```bash
#!/bin/bash
# scripts/restore-database.sh

set -e

if [ $# -ne 1 ]; then
    echo "Usage: $0 <backup_file>"
    echo "Example: $0 virtualcasino_20231201_120000.sql.gz"
    exit 1
fi

BACKUP_FILE=$1
DB_HOST="${DB_HOST:-localhost}"
DB_PORT="${DB_PORT:-5432}"
DB_NAME="${DB_NAME:-virtualcasino}"
DB_USER="${DB_USER:-postgres}"

echo "Starting database restoration from: ${BACKUP_FILE}"

# Download backup from S3 if not local
if [[ ${BACKUP_FILE} == s3://* ]]; then
    LOCAL_FILE=$(basename ${BACKUP_FILE})
    aws s3 cp ${BACKUP_FILE} ${LOCAL_FILE}
    BACKUP_FILE=${LOCAL_FILE}
fi

# Extract if compressed
if [[ ${BACKUP_FILE} == *.gz ]]; then
    gunzip ${BACKUP_FILE}
    BACKUP_FILE=${BACKUP_FILE%.gz}
fi

# Stop application services
systemctl stop virtual-casino
systemctl stop nginx

# Create backup of current database
CURRENT_BACKUP="/tmp/current_db_backup_$(date +%Y%m%d_%H%M%S).sql"
PGPASSWORD=${DB_PASSWORD} pg_dump \
    -h ${DB_HOST} \
    -p ${DB_PORT} \
    -U ${DB_USER} \
    -d ${DB_NAME} \
    --format=custom \
    --file=${CURRENT_BACKUP}

echo "Current database backed up to: ${CURRENT_BACKUP}"

# Drop and recreate database
PGPASSWORD=${DB_PASSWORD} dropdb -h ${DB_HOST} -p ${DB_PORT} -U ${DB_USER} ${DB_NAME}
PGPASSWORD=${DB_PASSWORD} createdb -h ${DB_HOST} -p ${DB_PORT} -U ${DB_USER} ${DB_NAME}

# Restore from backup
PGPASSWORD=${DB_PASSWORD} pg_restore \
    -h ${DB_HOST} \
    -p ${DB_PORT} \
    -U ${DB_USER} \
    -d ${DB_NAME} \
    --verbose \
    --no-password \
    ${BACKUP_FILE}

if [ $? -eq 0 ]; then
    echo "Database restoration completed successfully"
    
    # Start services
    systemctl start virtual-casino
    systemctl start nginx
    
    # Verify application health
    sleep 30
    curl -f http://localhost:8080/actuator/health
    
    if [ $? -eq 0 ]; then
        echo "Application health check passed"
        echo "Disaster recovery completed successfully"
    else
        echo "Application health check failed"
        echo "Consider rolling back to previous backup"
        exit 1
    fi
else
    echo "Database restoration failed"
    echo "Restoring from current backup"
    
    # Restore from current backup
    PGPASSWORD=${DB_PASSWORD} dropdb -h ${DB_HOST} -p ${DB_PORT} -U ${DB_USER} ${DB_NAME}
    PGPASSWORD=${DB_PASSWORD} createdb -h ${DB_HOST} -p ${DB_PORT} -U ${DB_USER} ${DB_NAME}
    PGPASSWORD=${DB_PASSWORD} pg_restore \
        -h ${DB_HOST} \
        -p ${DB_PORT} \
        -U ${DB_USER} \
        -d ${DB_NAME} \
        ${CURRENT_BACKUP}
    
    systemctl start virtual-casino
    systemctl start nginx
    
    exit 1
fi
```

### **GO-LIVE CHECKLIST**

#### **Pre-deployment Validation:**
- [ ] All unit tests passing (JUnit 5)
- [ ] Integration tests completed (Testcontainers)
- [ ] Security scan with no critical vulnerabilities (OWASP)
- [ ] Performance testing meets SLA requirements (JMeter)
- [ ] Database migrations tested on staging
- [ ] Backup and recovery procedures validated
- [ ] Monitoring and alerting configured (Prometheus/Grafana)
- [ ] SSL certificates installed and validated
- [ ] DNS configuration updated
- [ ] Load balancer health checks configured (Nginx)
- [ ] CDN configuration for static assets

#### **Deployment Steps:**
1. **Database Migration:**
   ```bash
   # Run Flyway migrations
   mvn flyway:migrate -Pdatabase-migration
   
   # Verify migration status
   mvn flyway:info -Pdatabase-migration
   ```

2. **Application Deployment:**
   ```bash
   # Build WAR file
   mvn clean package -Pproduction
   
   # Deploy to Tomcat
   cp target/virtual-casino-*.war /opt/tomcat/webapps/virtual-casino.war
   
   # Restart Tomcat
   systemctl restart tomcat
   ```

3. **Configuration Update:**
   ```bash
   # Update Nginx configuration
   nginx -t
   systemctl reload nginx
   
   # Update application properties
   systemctl restart virtual-casino
   ```

4. **Post-deployment Verification:**
   ```bash
   # Health check
   curl -f https://virtualcasino.com/actuator/health
   
   # Database connectivity
   curl -f https://virtualcasino.com/api/public/health
   
   # WebSocket connectivity test
   wscat -c wss://virtualcasino.com/ws
   
   # Load balancer health check
   curl -f https://virtualcasino.com/api/public/lb-health
   ```

#### **Post-Go-Live Monitoring (First 24 Hours):**
- [ ] Monitor application logs for errors
- [ ] Check performance metrics (response times < 500ms)
- [ ] Verify user registration and authentication flows
- [ ] Monitor database connection pool usage
- [ ] Validate WebSocket connections for real-time features
- [ ] Check Redis cache hit ratios
- [ ] Monitor Kafka message processing
- [ ] Verify SSL certificate and security headers
- [ ] Monitor server resources (CPU < 70%, Memory < 80%)
- [ ] Check backup job execution

### **SUPPORT & MAINTENANCE**

#### **Operational Runbooks:**

**Service Restart Procedure:**
```bash
#!/bin/bash
# runbooks/restart-service.sh

echo "Restarting Virtual Casino Application"

# Stop application gracefully
systemctl stop virtual-casino

# Wait for graceful shutdown
sleep 30

# Check if any Java processes are still running
if pgrep -f "virtual-casino" > /dev/null; then
    echo "Force killing remaining processes"
    pkill -9 -f "virtual-casino"
fi

# Clear application cache
redis-cli -h localhost -p 6379 FLUSHDB

# Start application
systemctl start virtual-casino

# Wait for startup
sleep 60

# Health check
if curl -f http://localhost:8080/actuator/health; then
    echo "Application restarted successfully"
else
    echo "Application startup failed"
    systemctl status virtual-casino
    exit 1
fi
```

**Database Maintenance:**
```sql
-- Monthly database maintenance
-- Run during low-traffic hours

-- Update table statistics
ANALYZE;

-- Reindex frequently used tables
REINDEX TABLE users;
REINDEX TABLE user_game_history;
REINDEX TABLE transactions;

-- Clean up old data (older than 1 year)
DELETE FROM user_game_history WHERE played_at < NOW() - INTERVAL '1 year';
DELETE FROM transactions WHERE created_at < NOW() - INTERVAL '1 year' AND type NOT IN ('PREMIUM_PURCHASE');

-- Vacuum tables to reclaim space
VACUUM ANALYZE users;
VACUUM ANALYZE user_game_history;
VACUUM ANALYZE transactions;

-- Update materialized views
REFRESH MATERIALIZED VIEW CONCURRENTLY leaderboard_view;
```

#### **On-call Procedures:**
- **Level 1 (Critical):** Application down, database unavailable, security breach
    - Response time: 15 minutes
    - Escalation: Level 2 after 30 minutes

- **Level 2 (High):** Performance degradation, payment issues, significant bugs
    - Response time: 1 hour
    - Escalation: Level 3 after 4 hours

- **Level 3 (Medium):** Minor bugs, feature requests, general inquiries
    - Response time: 4 hours during business hours
    - Escalation: Product team next business day

#### **Maintenance Windows:**
- **Regular:** First Sunday of each month, 2-4 AM local time
- **Emergency:** As needed with 2-hour advance notice
- **Database:** Quarterly maintenance during lowest traffic period
- **Security Updates:** Within 48 hours of critical patches

---

## **CONCLUSION**

This comprehensive template provides a complete roadmap for developing a virtual casino chips game web application using **modern Java enterprise technologies**. The Spring Boot framework with Java 21, combined with PostgreSQL, Redis, and Kafka, creates a robust and scalable foundation for a high-performance gaming platform.

**Key Technology Benefits:**
- **Spring Boot 3.2**: Rapid development with auto-configuration and production-ready features
- **Java 21**: Latest LTS version with performance improvements and modern language features
- **PostgreSQL 15.13**: Reliable ACID-compliant database with advanced JSON support
- **Redis 8.0 with Redisson**: High-performance caching and session management
- **Apache Kafka**: Scalable event streaming for real-time game events
- **JSP with JSTL**: Server-side rendering with familiar Java ecosystem integration

**Critical Success Factors:**
- Comprehensive testing strategy with 85%+ code coverage
- Security-first approach with Spring Security and rate limiting
- Scalable architecture supporting 1000+ concurrent users
- Real-time gaming experience with WebSocket integration
- Production-ready monitoring and alerting
- Automated CI/CD pipeline with quality gates

**Production Readiness:**
- Docker containerization for consistent deployments
- Nginx reverse proxy with SSL termination
- Database backup and disaster recovery procedures
- Application performance monitoring with Micrometer
- Structured logging with JSON format for analytics
- Comprehensive security hardening and rate limiting

This template serves as a **battle-tested blueprint** that can be directly implemented or adapted for similar enterprise gaming applications, providing a solid foundation for both development teams and business stakeholders to build upon.

**Next Steps:**
1. **Environment Setup**: Configure development environment with Docker Compose
2. **Team Onboarding**: Train developers on Spring Boot best practices
3. **Sprint Planning**: Begin with foundational setup and authentication
4. **Quality Assurance**: Implement testing automation from day one
5. **Production Planning**: Prepare infrastructure and deployment procedures

The combination of mature Java enterprise technologies with modern development practices ensures a maintainable, scalable, and secure gaming platform ready for production deployment.
