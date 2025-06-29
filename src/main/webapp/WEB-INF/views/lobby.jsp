<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>Virtual Game - Lobby</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <script src="https://code.jquery.com/jquery-3.7.0.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/sockjs-client@1/dist/sockjs.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/stompjs@2.3.3/lib/stomp.min.js"></script>
    <style>
        body {
            background-color: #f8f9fa;
        }
        .navbar {
            background-color: #6f42c1;
        }
        .navbar-brand, .nav-link {
            color: white;
        }
        .nav-link:hover {
            color: rgba(255, 255, 255, 0.8);
        }
        .card {
            border-radius: 10px;
            box-shadow: 0 4px 8px rgba(0,0,0,0.1);
            margin-bottom: 20px;
            transition: transform 0.3s;
        }
        .card:hover {
            transform: translateY(-5px);
        }
        .card-header {
            background-color: #6f42c1;
            color: white;
            border-radius: 10px 10px 0 0 !important;
        }
        .btn-primary {
            background-color: #6f42c1;
            border-color: #6f42c1;
        }
        .btn-primary:hover {
            background-color: #5a32a3;
            border-color: #5a32a3;
        }
        .game-card {
            height: 100%;
        }
        .game-icon {
            font-size: 2rem;
            margin-bottom: 10px;
        }
        .chip-balance {
            background-color: #28a745;
            color: white;
            padding: 5px 10px;
            border-radius: 20px;
        }
    </style>
</head>
<body>
    <!-- Navigation -->
    <nav class="navbar navbar-expand-lg navbar-dark">
        <div class="container">
            <a class="navbar-brand" href="/lobby">
                <i class="fas fa-dice"></i> Virtual Game Platform
            </a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarNav">
                <ul class="navbar-nav me-auto">
                    <li class="nav-item">
                        <a class="nav-link active" href="/lobby">Lobby</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="/leaderboard">Leaderboard</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="/history">Game History</a>
                    </li>
                </ul>
                <ul class="navbar-nav">
                    <li class="nav-item">
                        <a class="nav-link" href="#" id="claimDailyBonus">
                            <i class="fas fa-gift"></i> Daily Bonus
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="#">
                            <span class="chip-balance">
                                <i class="fas fa-coins"></i> <span id="chipBalance">1,000</span>
                            </span>
                        </a>
                    </li>
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" href="#" id="userDropdown" role="button" data-bs-toggle="dropdown">
                            <i class="fas fa-user"></i> <span id="username">User</span>
                        </a>
                        <ul class="dropdown-menu dropdown-menu-end">
                            <li><a class="dropdown-item" href="/profile"><i class="fas fa-user-edit"></i> Profile</a></li>
                            <li><a class="dropdown-item" href="/transactions"><i class="fas fa-history"></i> Transactions</a></li>
                            <li><hr class="dropdown-divider"></li>
                            <li><a class="dropdown-item" href="#" id="logoutBtn"><i class="fas fa-sign-out-alt"></i> Logout</a></li>
                        </ul>
                    </li>
                </ul>
            </div>
        </div>
    </nav>

    <!-- Main Content -->
    <div class="container mt-4">
        <!-- Welcome Banner -->
        <div class="row mb-4">
            <div class="col-12">
                <div class="card">
                    <div class="card-body">
                        <h2>Welcome to the Virtual Game Platform!</h2>
                        <p>Choose a game below to start playing. You can earn chips by playing games, claiming daily bonuses, and more.</p>
                    </div>
                </div>
            </div>
        </div>
        
        <!-- Games Section -->
        <h3 class="mb-3">Available Games</h3>
        <div class="row" id="gamesList">
            <!-- Games will be loaded here -->
        </div>
        
        <!-- Active Sessions Section -->
        <h3 class="mb-3 mt-4">Active Game Sessions</h3>
        <div class="row" id="sessionsList">
            <!-- Active sessions will be loaded here -->
        </div>
    </div>
    
    <!-- Join Game Modal -->
    <div class="modal fade" id="joinGameModal" tabindex="-1">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="joinGameTitle">Join Game</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <form id="joinGameForm">
                        <input type="hidden" id="gameId" name="gameId">
                        <div class="mb-3">
                            <label for="buyIn" class="form-label">Buy-in Amount (Chips)</label>
                            <input type="number" class="form-control" id="buyIn" name="buyIn" min="100" required>
                            <div class="form-text">Minimum buy-in: <span id="minBuyIn">100</span> chips</div>
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                    <button type="button" class="btn btn-primary" id="joinGameBtn">Join Game</button>
                </div>
            </div>
        </div>
    </div>
    
    <script>
        // Global variables
        let stompClient = null;
        let userToken = localStorage.getItem('token');
        let userData = null;
        
        $(document).ready(function() {
            // Check if user is logged in
            if (!userToken) {
                window.location.href = '/login';
                return;
            }
            
            // Load user data
            loadUserData();
            
            // Load games and sessions
            loadGames();
            loadActiveSessions();
            
            // Connect to WebSocket
            connectWebSocket();
            
            // Set up event handlers
            $('#claimDailyBonus').on('click', claimDailyBonus);
            $('#logoutBtn').on('click', logout);
            $('#joinGameBtn').on('click', joinGame);
            
            // Refresh data every 30 seconds
            setInterval(function() {
                loadUserData();
                loadActiveSessions();
            }, 30000);
        });
        
        function loadUserData() {
            $.ajax({
                url: '/api/users/me',
                type: 'GET',
                headers: {
                    'Authorization': 'Bearer ' + userToken
                },
                success: function(response) {
                    if (response.success) {
                        userData = response.data;
                        $('#username').text(userData.username);
                        $('#chipBalance').text(formatNumber(userData.chipsBalance));
                    }
                },
                error: function(xhr) {
                    if (xhr.status === 401) {
                        // Token expired, redirect to login
                        localStorage.removeItem('token');
                        window.location.href = '/login';
                    }
                }
            });
        }
        
        function loadGames() {
            $.ajax({
                url: '/api/games',
                type: 'GET',
                headers: {
                    'Authorization': 'Bearer ' + userToken
                },
                success: function(response) {
                    if (response.success) {
                        displayGames(response.data);
                    }
                }
            });
        }
        
        function displayGames(games) {
            let html = '';
            
            games.forEach(function(game) {
                let icon = getGameIcon(game.type);
                
                html += `
                    <div class="col-md-3 mb-4">
                        <div class="card game-card">
                            <div class="card-header">
                                <h5 class="mb-0">${game.name}</h5>
                            </div>
                            <div class="card-body text-center">
                                <div class="game-icon">${icon}</div>
                                <p>Min Bet: <span class="min-bet">${game.minBet}</span> chips</p>
                                <p>Max Players: ${game.maxPlayers}</p>
                                <button class="btn btn-primary w-100" onclick="showJoinGameModal('${game.id}', '${game.name}', ${game.minBet})">
                                    Play Now
                                </button>
                            </div>
                        </div>
                    </div>
                `;
            });
            
            $('#gamesList').html(html);
        }
        
        function loadActiveSessions() {
            $.ajax({
                url: '/api/games/sessions/active',
                type: 'GET',
                headers: {
                    'Authorization': 'Bearer ' + userToken
                },
                success: function(response) {
                    if (response.success) {
                        displaySessions(response.data);
                    }
                }
            });
        }
        
        function displaySessions(sessions) {
            if (sessions.length === 0) {
                $('#sessionsList').html('<div class="col-12"><p>No active sessions found. Start a new game!</p></div>');
                return;
            }
            
            let html = '';
            
            sessions.forEach(function(session) {
                let icon = getGameIcon(session.gameType);
                
                html += `
                    <div class="col-md-4 mb-4">
                        <div class="card">
                            <div class="card-header d-flex justify-content-between align-items-center">
                                <h5 class="mb-0">${session.gameName}</h5>
                                <span class="badge bg-success">${session.playerCount}/${session.maxPlayers} Players</span>
                            </div>
                            <div class="card-body">
                                <div class="d-flex justify-content-between mb-3">
                                    <div>${icon}</div>
                                    <div>
                                        <p class="mb-0">Started: <span class="start-time">${session.startedAt}</span></p>
                                        <p class="mb-0">Pot: <span class="pot-size">${session.potSize}</span> chips</p>
                                    </div>
                                </div>
                                <button class="btn btn-primary w-100" onclick="joinSession('${session.id}', ${session.minBuyIn})">
                                    Join Session
                                </button>
                            </div>
                        </div>
                    </div>
                `;
            });
            
            $('#sessionsList').html(html);
        }
        
        function showJoinGameModal(gameId, gameName, minBet) {
            $('#gameId').val(gameId);
            $('#joinGameTitle').text('Join ' + gameName);
            $('#minBuyIn').text(minBet);
            $('#buyIn').attr('min', minBet);
            $('#buyIn').val(minBet);
            
            new bootstrap.Modal(document.getElementById('joinGameModal')).show();
        }
        
        function joinGame() {
            const gameId = $('#gameId').val();
            const buyIn = parseInt($('#buyIn').val());
            
            $.ajax({
                url: '/api/games/join',
                type: 'POST',
                headers: {
                    'Authorization': 'Bearer ' + userToken,
                    'Content-Type': 'application/json'
                },
                data: JSON.stringify({
                    gameId: gameId,
                    buyIn: buyIn
                }),
                success: function(response) {
                    if (response.success) {
                        // Hide modal
                        bootstrap.Modal.getInstance(document.getElementById('joinGameModal')).hide();
                        
                        // Redirect to game page
                        window.location.href = '/game/' + response.data.sessionId;
                    } else {
                        alert(response.message);
                    }
                },
                error: function(xhr) {
                    alert(xhr.responseJSON ? xhr.responseJSON.message : 'Failed to join game');
                }
            });
        }
        
        function joinSession(sessionId, minBuyIn) {
            const buyIn = prompt('Enter buy-in amount (minimum ' + minBuyIn + ' chips):', minBuyIn);
            
            if (buyIn && parseInt(buyIn) >= minBuyIn) {
                $.ajax({
                    url: '/api/games/sessions/join',
                    type: 'POST',
                    headers: {
                        'Authorization': 'Bearer ' + userToken,
                        'Content-Type': 'application/json'
                    },
                    data: JSON.stringify({
                        sessionId: sessionId,
                        buyIn: parseInt(buyIn)
                    }),
                    success: function(response) {
                        if (response.success) {
                            // Redirect to game page
                            window.location.href = '/game/' + sessionId;
                        } else {
                            alert(response.message);
                        }
                    },
                    error: function(xhr) {
                        alert(xhr.responseJSON ? xhr.responseJSON.message : 'Failed to join session');
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
                        alert('Daily bonus claimed! You received ' + formatNumber(response.data.bonusAmount) + ' chips.');
                        loadUserData();
                    } else {
                        alert(response.message);
                    }
                },
                error: function(xhr) {
                    alert(xhr.responseJSON ? xhr.responseJSON.message : 'Failed to claim daily bonus');
                }
            });
        }
        
        function logout() {
            $.ajax({
                url: '/api/auth/logout',
                type: 'POST',
                headers: {
                    'Authorization': 'Bearer ' + userToken
                },
                success: function() {
                    localStorage.removeItem('token');
                    localStorage.removeItem('refreshToken');
                    window.location.href = '/login';
                }
            });
        }
        
        function connectWebSocket() {
            const socket = new SockJS('/ws');
            stompClient = Stomp.over(socket);
            
            stompClient.connect({
                'Authorization': 'Bearer ' + userToken
            }, function() {
                // Subscribe to lobby updates
                stompClient.subscribe('/topic/lobby', function(message) {
                    const update = JSON.parse(message.body);
                    handleLobbyUpdate(update);
                });
                
                // Subscribe to personal notifications
                stompClient.subscribe('/user/queue/notifications', function(message) {
                    const notification = JSON.parse(message.body);
                    showNotification(notification);
                });
            });
        }
        
        function handleLobbyUpdate(update) {
            if (update.type === 'SESSION_CREATED' || update.type === 'SESSION_UPDATED') {
                loadActiveSessions();
            } else if (update.type === 'GAME_UPDATED') {
                loadGames();
            }
        }
        
        function showNotification(notification) {
            alert(notification.message);
        }
        
        function getGameIcon(gameType) {
            switch (gameType) {
                case 'POKER':
                    return '<i class="fas fa-cards fa-fw"></i>';
                case 'BLACKJACK':
                    return '<i class="fas fa-dice fa-fw"></i>';
                case 'SLOTS':
                    return '<i class="fas fa-slot-machine fa-fw"></i>';
                case 'ROULETTE':
                    return '<i class="fas fa-circle fa-fw"></i>';
                default:
                    return '<i class="fas fa-gamepad fa-fw"></i>';
            }
        }
        
        function formatNumber(number) {
            return number.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
        }
        
        function formatTime(timestamp) {
            const date = new Date(timestamp);
            return date.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
        }
    </script>
</body>
</html>
