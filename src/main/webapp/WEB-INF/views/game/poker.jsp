<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>Virtual Game - Poker</title>
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
        .poker-table {
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
        .pot {
            position: absolute;
            top: 50%;
            left: 50%;
            transform: translate(-50%, -50%);
            text-align: center;
            font-size: 1.5rem;
            font-weight: bold;
            text-shadow: 1px 1px 2px black;
        }
        .community-cards {
            position: absolute;
            top: 40%;
            left: 50%;
            transform: translate(-50%, -50%);
            display: flex;
            gap: 10px;
        }
        .player-position {
            position: absolute;
            width: 150px;
            height: 120px;
            text-align: center;
        }
        .player-position.position-0 {
            bottom: 0;
            left: 50%;
            transform: translateX(-50%);
        }
        .player-position.position-1 {
            bottom: 20%;
            left: 10%;
        }
        .player-position.position-2 {
            top: 20%;
            left: 10%;
        }
        .player-position.position-3 {
            top: 0;
            left: 50%;
            transform: translateX(-50%);
        }
        .player-position.position-4 {
            top: 20%;
            right: 10%;
        }
        .player-position.position-5 {
            bottom: 20%;
            right: 10%;
        }
        .player-card {
            background-color: #1a1a1a;
            border-radius: 10px;
            padding: 10px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.3);
        }
        .player-info {
            font-size: 0.9rem;
            margin-bottom: 5px;
        }
        .player-name {
            font-weight: bold;
            white-space: nowrap;
            overflow: hidden;
            text-overflow: ellipsis;
        }
        .player-chips {
            color: #ffc107;
        }
        .player-cards {
            display: flex;
            justify-content: center;
            gap: 5px;
            margin-top: 5px;
        }
        .card {
            width: 40px;
            height: 60px;
            background-color: white;
            border-radius: 5px;
            display: flex;
            justify-content: center;
            align-items: center;
            color: black;
            font-weight: bold;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);
        }
        .card.red {
            color: red;
        }
        .card.hidden {
            background-color: #b71540;
            color: white;
        }
        .player-bet {
            position: absolute;
            top: -25px;
            left: 50%;
            transform: translateX(-50%);
            background-color: rgba(0, 0, 0, 0.7);
            padding: 2px 8px;
            border-radius: 10px;
            font-size: 0.8rem;
            color: #ffc107;
        }
        .player-action {
            position: absolute;
            top: -25px;
            right: 0;
            background-color: #007bff;
            padding: 2px 8px;
            border-radius: 10px;
            font-size: 0.8rem;
        }
        .dealer-button {
            position: absolute;
            bottom: -15px;
            right: -15px;
            background-color: white;
            color: black;
            border-radius: 50%;
            width: 25px;
            height: 25px;
            display: flex;
            justify-content: center;
            align-items: center;
            font-weight: bold;
            font-size: 0.8rem;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);
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
        .bet-slider {
            width: 200px;
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
        .timer {
            position: absolute;
            top: 20px;
            left: 50%;
            transform: translateX(-50%);
            background-color: rgba(0, 0, 0, 0.7);
            padding: 5px 15px;
            border-radius: 20px;
            font-size: 1.2rem;
            z-index: 100;
        }
        .active-player {
            box-shadow: 0 0 0 3px #ffc107;
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
        <!-- Timer -->
        <div class="timer" id="timer">00:30</div>

        <!-- Game Info -->
        <div class="game-info">
            <div><strong>Game:</strong> <span id="gameName">Texas Hold'em Poker</span></div>
            <div><strong>Blinds:</strong> <span id="blinds">10/20</span></div>
            <div><strong>Round:</strong> <span id="roundNumber">1</span></div>
            <div><strong>Your Chips:</strong> <span id="playerChips">1,000</span></div>
        </div>

        <!-- Poker Table -->
        <div class="poker-table">
            <!-- Pot -->
            <div class="pot" id="pot">Pot: 0</div>

            <!-- Community Cards -->
            <div class="community-cards" id="communityCards">
                <div class="card hidden">?</div>
                <div class="card hidden">?</div>
                <div class="card hidden">?</div>
                <div class="card hidden">?</div>
                <div class="card hidden">?</div>
            </div>

            <!-- Player Positions -->
            <div class="player-position position-0" id="position-0">
                <!-- Current player's position -->
            </div>
            <div class="player-position position-1" id="position-1"></div>
            <div class="player-position position-2" id="position-2"></div>
            <div class="player-position position-3" id="position-3"></div>
            <div class="player-position position-4" id="position-4"></div>
            <div class="player-position position-5" id="position-5"></div>
        </div>

        <!-- Action Panel -->
        <div class="action-panel" id="actionPanel" style="display: none;">
            <button class="btn btn-danger" id="foldBtn">Fold</button>
            <button class="btn btn-warning" id="checkBtn">Check</button>
            <button class="btn btn-primary" id="callBtn">Call <span id="callAmount">20</span></button>
            <div class="d-flex align-items-center">
                <button class="btn btn-success" id="raiseBtn">Raise</button>
                <input type="range" class="form-range ms-2 bet-slider" id="betSlider" min="0" max="1000" step="10" value="40">
                <span class="ms-2" id="betAmount">40</span>
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
        let timerInterval = null;

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
            $('#foldBtn').on('click', () => sendGameAction('FOLD'));
            $('#checkBtn').on('click', () => sendGameAction('CHECK'));
            $('#callBtn').on('click', () => sendGameAction('CALL'));
            $('#raiseBtn').on('click', () => sendGameAction('RAISE', parseInt($('#betSlider').val())));
            $('#betSlider').on('input', updateBetAmount);
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
            $('#blinds').text(state.smallBlind + '/' + state.bigBlind);
            $('#roundNumber').text(state.roundNumber);
            $('#pot').text('Pot: ' + formatNumber(state.potSize));

            // Update player chips
            const playerState = state.players.find(p => p.id === playerId);
            if (playerState) {
                $('#playerChips').text(formatNumber(playerState.chips));

                // Update bet slider
                const minBet = Math.max(state.bigBlind, state.currentBet * 2);
                const maxBet = playerState.chips;
                $('#betSlider').attr('min', minBet);
                $('#betSlider').attr('max', maxBet);
                $('#betSlider').val(minBet);
                $('#betAmount').text(minBet);
            }

            // Update community cards
            updateCommunityCards(state.communityCards);

            // Update player positions
            updatePlayerPositions(state.players);

            // Update action panel
            updateActionPanel(state);

            // Update timer
            updateTimer(state.timeRemaining);
        }

        function updateCommunityCards(cards) {
            const cardElements = $('#communityCards').children();

            for (let i = 0; i < 5; i++) {
                if (i < cards.length && cards[i]) {
                    const card = cards[i];
                    const isRed = card.suit === 'HEARTS' || card.suit === 'DIAMONDS';
                    $(cardElements[i]).removeClass('hidden').addClass(isRed ? 'red' : '').text(getCardText(card));
                } else {
                    $(cardElements[i]).addClass('hidden').removeClass('red').text('?');
                }
            }
        }

        function updatePlayerPositions(players) {
            // Clear all positions
            for (let i = 0; i < 6; i++) {
                $('#position-' + i).empty();
            }

            // Find current player's position
            const currentPlayerIndex = players.findIndex(p => p.id === playerId);
            if (currentPlayerIndex === -1) return;

            // Rearrange players so current player is at position 0
            const arrangedPlayers = [];
            for (let i = 0; i < players.length; i++) {
                const index = (currentPlayerIndex + i) % players.length;
                arrangedPlayers.push(players[index]);
            }

            // Display players
            for (let i = 0; i < arrangedPlayers.length; i++) {
                const player = arrangedPlayers[i];
                const isCurrentPlayer = player.id === playerId;
                const isActive = player.id === gameState.currentTurn;

                const html = `
                    <div class="player-card ${isActive ? 'active-player' : ''}">
                        <div class="player-info">
                            <div class="player-name">${player.name}</div>
                            <div class="player-chips">${formatNumber(player.chips)} chips</div>
                        </div>
                        <div class="player-cards">
                            ${getPlayerCards(player, isCurrentPlayer)}
                        </div>
                        ${player.bet > 0 ? '<div class="player-bet">' + formatNumber(player.bet) + '</div>' : ''}
                        ${player.lastAction ? '<div class="player-action">' + player.lastAction + '</div>' : ''}
                        ${player.isDealer ? '<div class="dealer-button">D</div>' : ''}
                    </div>
                `;

                $('#position-' + i).html(html);
            }
        }

        function getPlayerCards(player, isCurrentPlayer) {
            if (!player.cards || player.cards.length === 0) {
                return `
                    <div class="card hidden">?</div>
                    <div class="card hidden">?</div>
                `;
            }

            if (!isCurrentPlayer && !gameState.showdown) {
                return `
                    <div class="card hidden">?</div>
                    <div class="card hidden">?</div>
                `;
            }

            return player.cards.map(card => {
                const isRed = card.suit === 'HEARTS' || card.suit === 'DIAMONDS';
                return `<div class="card ${isRed ? 'red' : ''}">${getCardText(card)}</div>`;
            }).join('');
        }

        function updateActionPanel(state) {
            const isPlayerTurn = state.currentTurn === playerId;
            const playerState = state.players.find(p => p.id === playerId);

            if (!isPlayerTurn || !playerState || state.showdown) {
                $('#actionPanel').hide();
                return;
            }

            $('#actionPanel').show();

            // Update call amount
            const callAmount = state.currentBet - playerState.bet;
            $('#callAmount').text(callAmount);

            // Enable/disable buttons based on game state
            $('#checkBtn').prop('disabled', callAmount > 0);
            $('#callBtn').prop('disabled', callAmount === 0);
            $('#raiseBtn').prop('disabled', playerState.chips <= callAmount);

            // Update bet slider
            if (playerState.chips > callAmount) {
                const minRaise = Math.max(state.bigBlind, callAmount * 2);
                const maxRaise = playerState.chips;
                $('#betSlider').attr('min', minRaise);
                $('#betSlider').attr('max', maxRaise);
                $('#betSlider').val(minRaise);
                $('#betAmount').text(minRaise);
            }
        }

        function updateTimer(timeRemaining) {
            clearInterval(timerInterval);

            const updateTimerDisplay = () => {
                const minutes = Math.floor(timeRemaining / 60);
                const seconds = timeRemaining % 60;
                $('#timer').text(
                    String(minutes).padStart(2, '0') + ':' + 
                    String(seconds).padStart(2, '0')
                );

                if (timeRemaining <= 10) {
                    $('#timer').addClass('text-danger');
                } else {
                    $('#timer').removeClass('text-danger');
                }

                timeRemaining--;

                if (timeRemaining < 0) {
                    clearInterval(timerInterval);
                }
            };

            updateTimerDisplay();
            timerInterval = setInterval(updateTimerDisplay, 1000);
        }

        function updateBetAmount() {
            $('#betAmount').text($(this).val());
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
