<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>Virtual Game - Slots</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <script src="https://code.jquery.com/jquery-3.7.0.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/sockjs-client@1/dist/sockjs.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/stompjs@2.3.3/lib/stomp.min.js"></script>
    <style>
        body {
            background-color: #1a1a2e;
            color: white;
        }
        .navbar {
            background-color: #16213e;
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
        .slot-machine {
            background-color: #0f3460;
            border: 15px solid #e94560;
            border-radius: 20px;
            width: 600px;
            height: 400px;
            position: absolute;
            top: 50%;
            left: 50%;
            transform: translate(-50%, -50%);
            box-shadow: 0 0 30px rgba(233, 69, 96, 0.5);
            padding: 20px;
            display: flex;
            flex-direction: column;
            justify-content: space-between;
        }
        .slot-title {
            text-align: center;
            font-size: 2rem;
            margin-bottom: 20px;
            color: #e94560;
            text-shadow: 0 0 10px rgba(233, 69, 96, 0.7);
        }
        .reels-container {
            display: flex;
            justify-content: center;
            gap: 10px;
            margin-bottom: 20px;
        }
        .reel {
            width: 120px;
            height: 180px;
            background-color: #1a1a2e;
            border: 5px solid #e94560;
            border-radius: 10px;
            overflow: hidden;
            position: relative;
        }
        .reel-item {
            width: 100%;
            height: 100%;
            display: flex;
            justify-content: center;
            align-items: center;
            font-size: 3rem;
        }
        .payline {
            position: absolute;
            top: 50%;
            left: 0;
            width: 100%;
            height: 5px;
            background-color: #e94560;
            transform: translateY(-50%);
            z-index: 10;
        }
        .controls {
            display: flex;
            justify-content: center;
            gap: 20px;
            margin-top: 20px;
        }
        .spin-button {
            background-color: #e94560;
            color: white;
            border: none;
            padding: 10px 30px;
            font-size: 1.5rem;
            border-radius: 50px;
            cursor: pointer;
            transition: all 0.3s;
        }
        .spin-button:hover {
            background-color: #d63553;
            transform: scale(1.05);
        }
        .spin-button:disabled {
            background-color: #666;
            cursor: not-allowed;
            transform: none;
        }
        .bet-controls {
            display: flex;
            align-items: center;
            gap: 10px;
        }
        .bet-amount {
            font-size: 1.2rem;
            background-color: #1a1a2e;
            padding: 5px 15px;
            border-radius: 20px;
            border: 2px solid #e94560;
        }
        .bet-button {
            background-color: #0f3460;
            color: white;
            border: 2px solid #e94560;
            width: 30px;
            height: 30px;
            border-radius: 50%;
            display: flex;
            justify-content: center;
            align-items: center;
            cursor: pointer;
            font-weight: bold;
            transition: all 0.2s;
        }
        .bet-button:hover {
            background-color: #e94560;
        }
        .win-message {
            position: absolute;
            top: 50%;
            left: 50%;
            transform: translate(-50%, -50%);
            background-color: rgba(0, 0, 0, 0.8);
            padding: 20px;
            border-radius: 10px;
            font-size: 2rem;
            font-weight: bold;
            text-align: center;
            z-index: 200;
            display: none;
        }
        .win-amount {
            color: #e94560;
            font-size: 2.5rem;
            margin-top: 10px;
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
        .paytable-button {
            position: fixed;
            right: 20px;
            top: 76px;
            background-color: #e94560;
            color: white;
            border: none;
            padding: 5px 15px;
            border-radius: 5px;
            cursor: pointer;
        }
        .paytable-modal .modal-content {
            background-color: #1a1a2e;
            color: white;
            border: 2px solid #e94560;
        }
        .paytable-modal .modal-header {
            border-bottom: 1px solid #e94560;
        }
        .paytable-modal .modal-footer {
            border-top: 1px solid #e94560;
        }
        .paytable-row {
            display: flex;
            align-items: center;
            margin-bottom: 10px;
        }
        .paytable-symbols {
            display: flex;
            gap: 5px;
            margin-right: 20px;
        }
        .paytable-symbol {
            font-size: 1.5rem;
            width: 40px;
            height: 40px;
            display: flex;
            justify-content: center;
            align-items: center;
        }
        .paytable-payout {
            font-weight: bold;
            color: #e94560;
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
        @keyframes spin {
            0% { transform: translateY(0); }
            100% { transform: translateY(-900%); }
        }
        .spinning {
            animation: spin 0.5s linear infinite;
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
            <div><strong>Game:</strong> <span id="gameName">Slots</span></div>
            <div><strong>Min Bet:</strong> <span id="minBet">5</span></div>
            <div><strong>Max Bet:</strong> <span id="maxBet">100</span></div>
            <div><strong>Your Chips:</strong> <span id="playerChips">1,000</span></div>
        </div>

        <!-- Paytable Button -->
        <button class="paytable-button" data-bs-toggle="modal" data-bs-target="#paytableModal">
            <i class="fas fa-info-circle"></i> Paytable
        </button>

        <!-- Slot Machine -->
        <div class="slot-machine">
            <div class="slot-title">VIRTUAL SLOTS</div>
            
            <div class="reels-container">
                <div class="reel" id="reel1">
                    <div class="payline"></div>
                    <div class="reel-items" id="reelItems1">
                        <!-- Reel items will be generated dynamically -->
                    </div>
                </div>
                <div class="reel" id="reel2">
                    <div class="payline"></div>
                    <div class="reel-items" id="reelItems2">
                        <!-- Reel items will be generated dynamically -->
                    </div>
                </div>
                <div class="reel" id="reel3">
                    <div class="payline"></div>
                    <div class="reel-items" id="reelItems3">
                        <!-- Reel items will be generated dynamically -->
                    </div>
                </div>
            </div>
            
            <div class="controls">
                <div class="bet-controls">
                    <div class="bet-button" id="decreaseBet">-</div>
                    <div class="bet-amount">Bet: <span id="betAmount">10</span></div>
                    <div class="bet-button" id="increaseBet">+</div>
                </div>
                <button class="spin-button" id="spinButton">SPIN</button>
            </div>
        </div>

        <!-- Win Message -->
        <div class="win-message" id="winMessage">
            <div>YOU WIN!</div>
            <div class="win-amount" id="winAmount">+500</div>
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

    <!-- Paytable Modal -->
    <div class="modal fade paytable-modal" id="paytableModal" tabindex="-1" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Paytable</h5>
                    <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <div class="paytable-row">
                        <div class="paytable-symbols">
                            <div class="paytable-symbol">🍒</div>
                            <div class="paytable-symbol">🍒</div>
                            <div class="paytable-symbol">🍒</div>
                        </div>
                        <div class="paytable-payout">x10</div>
                    </div>
                    <div class="paytable-row">
                        <div class="paytable-symbols">
                            <div class="paytable-symbol">🍊</div>
                            <div class="paytable-symbol">🍊</div>
                            <div class="paytable-symbol">🍊</div>
                        </div>
                        <div class="paytable-payout">x15</div>
                    </div>
                    <div class="paytable-row">
                        <div class="paytable-symbols">
                            <div class="paytable-symbol">🍋</div>
                            <div class="paytable-symbol">🍋</div>
                            <div class="paytable-symbol">🍋</div>
                        </div>
                        <div class="paytable-payout">x20</div>
                    </div>
                    <div class="paytable-row">
                        <div class="paytable-symbols">
                            <div class="paytable-symbol">🍇</div>
                            <div class="paytable-symbol">🍇</div>
                            <div class="paytable-symbol">🍇</div>
                        </div>
                        <div class="paytable-payout">x25</div>
                    </div>
                    <div class="paytable-row">
                        <div class="paytable-symbols">
                            <div class="paytable-symbol">🍉</div>
                            <div class="paytable-symbol">🍉</div>
                            <div class="paytable-symbol">🍉</div>
                        </div>
                        <div class="paytable-payout">x30</div>
                    </div>
                    <div class="paytable-row">
                        <div class="paytable-symbols">
                            <div class="paytable-symbol">💎</div>
                            <div class="paytable-symbol">💎</div>
                            <div class="paytable-symbol">💎</div>
                        </div>
                        <div class="paytable-payout">x50</div>
                    </div>
                    <div class="paytable-row">
                        <div class="paytable-symbols">
                            <div class="paytable-symbol">7️⃣</div>
                            <div class="paytable-symbol">7️⃣</div>
                            <div class="paytable-symbol">7️⃣</div>
                        </div>
                        <div class="paytable-payout">x100</div>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                </div>
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
        let currentBet = 10;
        let isSpinning = false;
        
        // Slot symbols
        const symbols = ['🍒', '🍊', '🍋', '🍇', '🍉', '💎', '7️⃣'];
        
        // Symbol payouts
        const payouts = {
            '🍒': 10,
            '🍊': 15,
            '🍋': 20,
            '🍇': 25,
            '🍉': 30,
            '💎': 50,
            '7️⃣': 100
        };

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

            // Initialize reels
            initializeReels();

            // Set up event handlers
            $('#leaveGameBtn').on('click', leaveGame);
            $('#spinButton').on('click', spinReels);
            $('#decreaseBet').on('click', decreaseBet);
            $('#increaseBet').on('click', increaseBet);
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

            // Update player chips
            const playerState = state.players.find(p => p.id === playerId);
            if (playerState) {
                $('#playerChips').text(formatNumber(playerState.chips));
            }

            // If this is a spin result, show it
            if (state.action === 'SPIN_RESULT') {
                showSpinResult(state.result);
            }
        }

        function initializeReels() {
            for (let i = 1; i <= 3; i++) {
                const reelItems = $(`#reelItems${i}`);
                reelItems.empty();
                
                // Create 10 random symbols for each reel
                for (let j = 0; j < 10; j++) {
                    const randomSymbol = symbols[Math.floor(Math.random() * symbols.length)];
                    const reelItem = $('<div class="reel-item"></div>').text(randomSymbol);
                    reelItems.append(reelItem);
                }
            }
        }

        function spinReels() {
            if (isSpinning) return;
            
            const playerChips = parseInt($('#playerChips').text().replace(/,/g, ''));
            
            if (currentBet > playerChips) {
                alert('Not enough chips!');
                return;
            }
            
            isSpinning = true;
            $('#spinButton').prop('disabled', true);
            
            // Start spinning animation
            for (let i = 1; i <= 3; i++) {
                $(`#reelItems${i}`).addClass('spinning');
            }
            
            // Send spin action to server
            sendGameAction('SPIN', currentBet);
            
            // Simulate spinning time (will be replaced by actual server response)
            setTimeout(() => {
                // This will be replaced by the server response
                // For now, we'll just simulate a random result
                const result = {
                    symbols: [
                        symbols[Math.floor(Math.random() * symbols.length)],
                        symbols[Math.floor(Math.random() * symbols.length)],
                        symbols[Math.floor(Math.random() * symbols.length)]
                    ],
                    win: false,
                    winAmount: 0
                };
                
                // Check if all symbols are the same (win)
                if (result.symbols[0] === result.symbols[1] && result.symbols[1] === result.symbols[2]) {
                    result.win = true;
                    result.winAmount = currentBet * payouts[result.symbols[0]];
                }
                
                // Stop spinning and show result
                stopSpinning(result);
            }, 2000);
        }

        function stopSpinning(result) {
            // Stop spinning animation
            for (let i = 1; i <= 3; i++) {
                $(`#reelItems${i}`).removeClass('spinning');
                
                // Set the result symbols
                const reelItems = $(`#reelItems${i}`);
                reelItems.empty();
                
                // Add some random symbols before the result
                for (let j = 0; j < 3; j++) {
                    const randomSymbol = symbols[Math.floor(Math.random() * symbols.length)];
                    const reelItem = $('<div class="reel-item"></div>').text(randomSymbol);
                    reelItems.append(reelItem);
                }
                
                // Add the result symbol
                const resultItem = $('<div class="reel-item"></div>').text(result.symbols[i-1]);
                reelItems.append(resultItem);
                
                // Add some random symbols after the result
                for (let j = 0; j < 6; j++) {
                    const randomSymbol = symbols[Math.floor(Math.random() * symbols.length)];
                    const reelItem = $('<div class="reel-item"></div>').text(randomSymbol);
                    reelItems.append(reelItem);
                }
            }
            
            // Show win message if won
            if (result.win) {
                $('#winAmount').text('+' + result.winAmount);
                $('#winMessage').fadeIn(500).delay(2000).fadeOut(500);
                
                // Update player chips (this would normally come from the server)
                const currentChips = parseInt($('#playerChips').text().replace(/,/g, ''));
                $('#playerChips').text(formatNumber(currentChips + result.winAmount));
                $('#chipBalance').text(formatNumber(currentChips + result.winAmount));
            }
            
            // Re-enable spin button
            setTimeout(() => {
                isSpinning = false;
                $('#spinButton').prop('disabled', false);
            }, 1000);
        }

        function showSpinResult(result) {
            // This function will be called when receiving a spin result from the server
            stopSpinning({
                symbols: result.symbols,
                win: result.win,
                winAmount: result.winAmount
            });
        }

        function decreaseBet() {
            const minBet = parseInt($('#minBet').text());
            if (currentBet > minBet) {
                currentBet -= 5;
                $('#betAmount').text(currentBet);
            }
        }

        function increaseBet() {
            const maxBet = parseInt($('#maxBet').text());
            const playerChips = parseInt($('#playerChips').text().replace(/,/g, ''));
            
            if (currentBet < maxBet && currentBet < playerChips) {
                currentBet += 5;
                $('#betAmount').text(currentBet);
            }
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

        function formatNumber(number) {
            return number.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
        }
    </script>
</body>
</html>
