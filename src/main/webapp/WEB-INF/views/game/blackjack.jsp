<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>Virtual Game - Blackjack</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <script src="https://code.jquery.com/jquery-3.7.0.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/sockjs-client@1/dist/sockjs.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/stompjs@2.3.3/lib/stomp.min.js"></script>
    <style>
        body {
            background-color: #0a5c36;
            color: white;
        }
        .navbar {
            background-color: #0a3c26;
        }
        .navbar-brand, .nav-link {
            color: white;
        }
        .nav-link:hover {
            color: rgba(255, 255, 255, 0.8);
        }
        .game-container {
            position: relative;
            height: calc(100vh - 56px);
            overflow: hidden;
        }
        .blackjack-table {
            background-color: #0a5c36;
            border: 15px solid #7b5d3f;
            border-radius: 200px;
            width: 800px;
            height: 400px;
            position: absolute;
            top: 50%;
            left: 50%;
            transform: translate(-50%, -50%);
            box-shadow: 0 0 20px rgba(0, 0, 0, 0.5);
        }
        .dealer-area {
            position: absolute;
            top: 20%;
            left: 50%;
            transform: translateX(-50%);
            text-align: center;
            width: 300px;
        }
        .player-area {
            position: absolute;
            bottom: 20%;
            left: 50%;
            transform: translateX(-50%);
            text-align: center;
            width: 300px;
        }
        .card-container {
            display: flex;
            justify-content: center;
            gap: 10px;
            margin-top: 10px;
        }
        .card {
            width: 60px;
            height: 90px;
            background-color: white;
            border-radius: 5px;
            display: flex;
            justify-content: center;
            align-items: center;
            color: black;
            font-weight: bold;
            font-size: 1.2rem;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);
        }
        .card.red {
            color: red;
        }
        .card.hidden {
            background-color: #b71540;
            color: white;
        }
        .hand-value {
            margin-top: 10px;
            font-size: 1.2rem;
            font-weight: bold;
        }
        .action-panel {
            position: fixed;
            bottom: 20px;
            left: 50%;
            transform: translateX(-50%);
            background-color: rgba(0, 0, 0, 0.7);
            padding: 15px;
            border-radius: 10px;
            display: flex;
            gap: 10px;
            z-index: 100;
        }
        .action-panel button {
            min-width: 80px;
        }
        .bet-panel {
            position: fixed;
            bottom: 20px;
            left: 50%;
            transform: translateX(-50%);
            background-color: rgba(0, 0, 0, 0.7);
            padding: 15px;
            border-radius: 10px;
            display: flex;
            flex-direction: column;
            align-items: center;
            gap: 10px;
            z-index: 100;
        }
        .bet-panel .bet-amount {
            font-size: 1.2rem;
            margin-bottom: 10px;
        }
        .bet-panel .bet-controls {
            display: flex;
            gap: 10px;
        }
        .bet-panel .chip {
            width: 50px;
            height: 50px;
            border-radius: 50%;
            display: flex;
            justify-content: center;
            align-items: center;
            font-weight: bold;
            cursor: pointer;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);
        }
        .bet-panel .chip.chip-10 {
            background-color: #f1c40f;
        }
        .bet-panel .chip.chip-25 {
            background-color: #e74c3c;
        }
        .bet-panel .chip.chip-50 {
            background-color: #3498db;
        }
        .bet-panel .chip.chip-100 {
            background-color: #2ecc71;
        }
        .chat-panel {
            position: fixed;
            right: 20px;
            bottom: 20px;
            width: 300px;
            height: 300px;
            background-color: rgba(0, 0, 0, 0.7);
            border-radius: 10px;
            display: flex;
            flex-direction: column;
            z-index: 100;
        }
        .chat-messages {
            flex-grow: 1;
            overflow-y: auto;
            padding: 10px;
            font-size: 0.9rem;
        }
        .chat-input {
            display: flex;
            padding: 10px;
            border-top: 1px solid #444;
        }
        .chat-input input {
            flex-grow: 1;
            background-color: rgba(255, 255, 255, 0.2);
            border: none;
            border-radius: 5px;
            padding: 5px 10px;
            color: white;
        }
        .chat-input button {
            margin-left: 5px;
        }
        .game-info {
            position: fixed;
            left: 20px;
            top: 76px;
            background-color: rgba(0, 0, 0, 0.7);
            padding: 10px;
            border-radius: 10px;
            font-size: 0.9rem;
            z-index: 100;
        }
        .result-message {
            position: absolute;
            top: 50%;
            left: 50%;
            transform: translate(-50%, -50%);
            background-color: rgba(0, 0, 0, 0.8);
            padding: 20px;
            border-radius: 10px;
            font-size: 1.5rem;
            font-weight: bold;
            text-align: center;
            z-index: 200;
        }
        .win {
            color: #2ecc71;
        }
        .lose {
            color: #e74c3c;
        }
        .push {
            color: #f1c40f;
        }
    </style>
</head>
<body>
    <!-- Navigation -->
    <nav class="navbar navbar-expand-lg navbar-dark">
        <div class="container-fluid">
            <a class="navbar-brand" href="/lobby">
                <i class="fas fa-dice"></i> Virtual Game Platform
            </a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarNav">
                <ul class="navbar-nav me-auto">
                    <li class="nav-item">
                        <a class="nav-link" href="/lobby">Lobby</a>
                    </li>
                </ul>
                <ul class="navbar-nav">
                    <li class="nav-item">
                        <a class="nav-link" href="#">
                            <span class="badge bg-success">
                                <i class="fas fa-coins"></i> <span id="chipBalance">1,000</span>
                            </span>
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="#" id="leaveGameBtn">
                            <i class="fas fa-sign-out-alt"></i> Leave Game
                        </a>
                    </li>
                </ul>
            </div>
        </div>
    </nav>

    <!-- Game Container -->
    <div class="game-container">
        <!-- Game Info -->
        <div class="game-info">
            <div><strong>Game:</strong> <span id="gameName">Blackjack</span></div>
            <div><strong>Min Bet:</strong> <span id="minBet">10</span></div>
            <div><strong>Max Bet:</strong> <span id="maxBet">500</span></div>
            <div><strong>Your Chips:</strong> <span id="playerChips">1,000</span></div>
            <div><strong>Current Bet:</strong> <span id="currentBet">0</span></div>
        </div>

        <!-- Blackjack Table -->
        <div class="blackjack-table">
            <!-- Dealer Area -->
            <div class="dealer-area">
                <h4>Dealer</h4>
                <div class="card-container" id="dealerCards">
                    <!-- Dealer cards will be displayed here -->
                </div>
                <div class="hand-value" id="dealerValue"></div>
            </div>

            <!-- Player Area -->
            <div class="player-area">
                <h4>Your Hand</h4>
                <div class="card-container" id="playerCards">
                    <!-- Player cards will be displayed here -->
                </div>
                <div class="hand-value" id="playerValue"></div>
            </div>

            <!-- Result Message (hidden by default) -->
            <div class="result-message" id="resultMessage" style="display: none;"></div>
        </div>

        <!-- Action Panel (hidden by default) -->
        <div class="action-panel" id="actionPanel" style="display: none;">
            <button class="btn btn-success" id="hitBtn">Hit</button>
            <button class="btn btn-warning" id="standBtn">Stand</button>
            <button class="btn btn-primary" id="doubleBtn">Double</button>
            <button class="btn btn-info" id="splitBtn">Split</button>
        </div>

        <!-- Bet Panel -->
        <div class="bet-panel" id="betPanel">
            <div class="bet-amount">Bet Amount: <span id="betAmount">0</span></div>
            <div class="bet-controls">
                <div class="chip chip-10" data-value="10">10</div>
                <div class="chip chip-25" data-value="25">25</div>
                <div class="chip chip-50" data-value="50">50</div>
                <div class="chip chip-100" data-value="100">100</div>
                <button class="btn btn-secondary" id="clearBetBtn">Clear</button>
                <button class="btn btn-primary" id="placeBetBtn">Place Bet</button>
            </div>
        </div>

        <!-- Chat Panel -->
        <div class="chat-panel">
            <div class="chat-messages" id="chatMessages">
                <!-- Chat messages will be displayed here -->
            </div>
            <div class="chat-input">
                <input type="text" id="chatInput" placeholder="Type a message...">
                <button class="btn btn-sm btn-primary" id="sendChatBtn">Send</button>
            </div>
        </div>
    </div>

    <script>
        // Global variables
        let stompClient = null;
        let userToken = localStorage.getItem('token');
        let userData = null;
        let gameState = null;
        let sessionId = '${sessionId}';
        let playerId = null;
        let currentBet = 0;
        let gameInProgress = false;

        $(document).ready(function() {
            // Check if user is logged in
            if (!userToken) {
                window.location.href = '/login';
                return;
            }

            // Load user data
            loadUserData();

            // Connect to WebSocket
            connectWebSocket();

            // Set up event handlers
            $('#leaveGameBtn').on('click', leaveGame);
            $('#hitBtn').on('click', () => sendGameAction('HIT'));
            $('#standBtn').on('click', () => sendGameAction('STAND'));
            $('#doubleBtn').on('click', () => sendGameAction('DOUBLE'));
            $('#splitBtn').on('click', () => sendGameAction('SPLIT'));
            $('.chip').on('click', addChipToBet);
            $('#clearBetBtn').on('click', clearBet);
            $('#placeBetBtn').on('click', placeBet);
            $('#sendChatBtn').on('click', sendChatMessage);
            $('#chatInput').on('keypress', function(e) {
                if (e.which === 13) {
                    sendChatMessage();
                }
            });

            // Handle window close/refresh
            $(window).on('beforeunload', function() {
                leaveGame(false);
            });
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
                        playerId = userData.id;
                        $('#chipBalance').text(formatNumber(userData.chipsBalance));
                        $('#playerChips').text(formatNumber(userData.chipsBalance));
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

        function connectWebSocket() {
            const socket = new SockJS('/ws');
            stompClient = Stomp.over(socket);

            stompClient.connect({
                'Authorization': 'Bearer ' + userToken
            }, function() {
                // Subscribe to game state updates
                stompClient.subscribe('/topic/game/' + sessionId, function(message) {
                    const update = JSON.parse(message.body);
                    updateGameState(update);
                });

                // Subscribe to chat messages
                stompClient.subscribe('/topic/chat/' + sessionId, function(message) {
                    const chatMessage = JSON.parse(message.body);
                    displayChatMessage(chatMessage);
                });

                // Join the game
                joinGame();
            });
        }

        function joinGame() {
            stompClient.send('/app/game.join', {}, JSON.stringify({
                sessionId: sessionId
            }));
        }

        function updateGameState(state) {
            gameState = state;

            // Update game info
            $('#gameName').text(state.gameName);
            $('#minBet').text(state.minBet);
            $('#maxBet').text(state.maxBet);
            $('#currentBet').text(formatNumber(state.currentBet));

            // Update player chips
            const playerState = state.players.find(p => p.id === playerId);
            if (playerState) {
                $('#playerChips').text(formatNumber(playerState.chips));
            }

            // Update game state based on phase
            switch (state.gameState) {
                case 'BETTING':
                    showBettingPhase();
                    break;
                case 'PLAYER_TURN':
                    showPlayerTurn(state);
                    break;
                case 'DEALER_TURN':
                    showDealerTurn(state);
                    break;
                case 'GAME_OVER':
                    showGameResult(state);
                    break;
            }
        }

        function showBettingPhase() {
            // Hide action panel and result message
            $('#actionPanel').hide();
            $('#resultMessage').hide();
            
            // Show betting panel
            $('#betPanel').show();
            
            // Clear cards
            $('#dealerCards').empty();
            $('#playerCards').empty();
            $('#dealerValue').text('');
            $('#playerValue').text('');
            
            gameInProgress = false;
        }

        function showPlayerTurn(state) {
            // Hide betting panel and result message
            $('#betPanel').hide();
            $('#resultMessage').hide();
            
            // Show action panel
            $('#actionPanel').show();
            
            // Update cards and values
            updateCards(state);
            
            // Enable/disable buttons based on game state
            $('#doubleBtn').prop('disabled', state.canDouble === false);
            $('#splitBtn').prop('disabled', state.canSplit === false);
            
            gameInProgress = true;
        }

        function showDealerTurn(state) {
            // Hide betting panel and action panel
            $('#betPanel').hide();
            $('#actionPanel').hide();
            
            // Update cards and values
            updateCards(state);
            
            gameInProgress = true;
        }

        function showGameResult(state) {
            // Hide betting panel and action panel
            $('#betPanel').hide();
            $('#actionPanel').hide();
            
            // Update cards and values
            updateCards(state);
            
            // Show result message
            const result = state.result;
            let message = '';
            let resultClass = '';
            
            if (result === 'WIN') {
                message = 'You Win!';
                resultClass = 'win';
            } else if (result === 'LOSE') {
                message = 'You Lose!';
                resultClass = 'lose';
            } else if (result === 'PUSH') {
                message = 'Push!';
                resultClass = 'push';
            } else if (result === 'BLACKJACK') {
                message = 'Blackjack!';
                resultClass = 'win';
            }
            
            $('#resultMessage').text(message).removeClass('win lose push').addClass(resultClass).show();
            
            // Start new round after delay
            setTimeout(function() {
                $('#resultMessage').hide();
                showBettingPhase();
            }, 3000);
            
            gameInProgress = false;
        }

        function updateCards(state) {
            // Update dealer cards
            $('#dealerCards').empty();
            if (state.dealerCards) {
                state.dealerCards.forEach((card, index) => {
                    const isHidden = index === 0 && state.gameState === 'PLAYER_TURN';
                    const cardHtml = createCardHtml(card, isHidden);
                    $('#dealerCards').append(cardHtml);
                });
            }
            
            // Update dealer value
            if (state.dealerValue && state.gameState !== 'PLAYER_TURN') {
                $('#dealerValue').text('Value: ' + state.dealerValue);
            } else {
                $('#dealerValue').text('');
            }
            
            // Update player cards
            $('#playerCards').empty();
            if (state.playerCards) {
                state.playerCards.forEach(card => {
                    const cardHtml = createCardHtml(card, false);
                    $('#playerCards').append(cardHtml);
                });
            }
            
            // Update player value
            if (state.playerValue) {
                $('#playerValue').text('Value: ' + state.playerValue);
            } else {
                $('#playerValue').text('');
            }
        }

        function createCardHtml(card, isHidden) {
            if (isHidden) {
                return '<div class="card hidden">?</div>';
            }
            
            const isRed = card.suit === 'HEARTS' || card.suit === 'DIAMONDS';
            return `<div class="card ${isRed ? 'red' : ''}">${getCardText(card)}</div>`;
        }

        function addChipToBet() {
            const chipValue = parseInt($(this).data('value'));
            const maxBet = parseInt($('#maxBet').text());
            const playerChips = parseInt($('#playerChips').text().replace(/,/g, ''));
            
            if (currentBet + chipValue <= maxBet && currentBet + chipValue <= playerChips) {
                currentBet += chipValue;
                $('#betAmount').text(currentBet);
            }
        }

        function clearBet() {
            currentBet = 0;
            $('#betAmount').text(currentBet);
        }

        function placeBet() {
            const minBet = parseInt($('#minBet').text());
            
            if (currentBet < minBet) {
                alert('Minimum bet is ' + minBet + ' chips');
                return;
            }
            
            sendGameAction('BET', currentBet);
        }

        function sendGameAction(action, amount = 0) {
            stompClient.send('/app/game.action', {}, JSON.stringify({
                sessionId: sessionId,
                action: action,
                amount: amount
            }));
        }

        function sendChatMessage() {
            const message = $('#chatInput').val().trim();
            if (!message) return;

            stompClient.send('/app/chat.message', {}, JSON.stringify({
                roomId: sessionId,
                content: message
            }));

            $('#chatInput').val('');
        }

        function displayChatMessage(message) {
            const isCurrentUser = message.sender === userData.username;
            const html = `
                <div class="mb-2 ${isCurrentUser ? 'text-end' : ''}">
                    <small class="text-muted">${isCurrentUser ? 'You' : message.sender}:</small>
                    <div>${message.content}</div>
                </div>
            `;

            $('#chatMessages').append(html);
            $('#chatMessages').scrollTop($('#chatMessages')[0].scrollHeight);
        }

        function leaveGame(confirm = true) {
            if (confirm && !window.confirm('Are you sure you want to leave the game?')) {
                return;
            }

            $.ajax({
                url: '/api/games/leave',
                type: 'POST',
                headers: {
                    'Authorization': 'Bearer ' + userToken,
                    'Content-Type': 'application/json'
                },
                data: JSON.stringify({
                    sessionId: sessionId
                }),
                success: function() {
                    window.location.href = '/lobby';
                },
                error: function() {
                    // Redirect to lobby anyway
                    window.location.href = '/lobby';
                }
            });
        }

        function getCardText(card) {
            const ranks = {
                'TWO': '2',
                'THREE': '3',
                'FOUR': '4',
                'FIVE': '5',
                'SIX': '6',
                'SEVEN': '7',
                'EIGHT': '8',
                'NINE': '9',
                'TEN': '10',
                'JACK': 'J',
                'QUEEN': 'Q',
                'KING': 'K',
                'ACE': 'A'
            };

            const suits = {
                'SPADES': '♠',
                'HEARTS': '♥',
                'DIAMONDS': '♦',
                'CLUBS': '♣'
            };

            return ranks[card.rank] + suits[card.suit];
        }

        function formatNumber(number) {
            return number.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
        }
    </script>
</body>
</html>
