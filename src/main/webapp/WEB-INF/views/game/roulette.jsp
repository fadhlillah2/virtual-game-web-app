<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>Virtual Game - Roulette</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <script src="https://code.jquery.com/jquery-3.7.0.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/sockjs-client@1/dist/sockjs.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/stompjs@2.3.3/lib/stomp.min.js"></script>
    <style>
        body {
            background-color: #0c3b2e;
            color: white;
        }
        .navbar {
            background-color: #051b14;
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
        .roulette-table {
            background-color: #0c3b2e;
            border: 15px solid #7b5d3f;
            border-radius: 10px;
            width: 1000px;
            height: 600px;
            position: absolute;
            top: 50%;
            left: 50%;
            transform: translate(-50%, -50%);
            box-shadow: 0 0 20px rgba(0, 0, 0, 0.5);
            display: flex;
            flex-direction: column;
            padding: 20px;
        }
        .roulette-header {
            display: flex;
            justify-content: space-between;
            margin-bottom: 20px;
        }
        .wheel-container {
            width: 300px;
            height: 300px;
            position: relative;
        }
        .wheel {
            width: 100%;
            height: 100%;
            border-radius: 50%;
            background: conic-gradient(
                #b71c1c 0deg 9.73deg,
                #000000 9.73deg 19.46deg,
                #b71c1c 19.46deg 29.19deg,
                #000000 29.19deg 38.92deg,
                #b71c1c 38.92deg 48.65deg,
                #000000 48.65deg 58.38deg,
                #b71c1c 58.38deg 68.11deg,
                #000000 68.11deg 77.84deg,
                #b71c1c 77.84deg 87.57deg,
                #000000 87.57deg 97.3deg,
                #b71c1c 97.3deg 107.03deg,
                #000000 107.03deg 116.76deg,
                #b71c1c 116.76deg 126.49deg,
                #000000 126.49deg 136.22deg,
                #b71c1c 136.22deg 145.95deg,
                #000000 145.95deg 155.68deg,
                #b71c1c 155.68deg 165.41deg,
                #000000 165.41deg 175.14deg,
                #b71c1c 175.14deg 184.87deg,
                #000000 184.87deg 194.6deg,
                #b71c1c 194.6deg 204.33deg,
                #000000 204.33deg 214.06deg,
                #b71c1c 214.06deg 223.79deg,
                #000000 223.79deg 233.52deg,
                #b71c1c 233.52deg 243.25deg,
                #000000 243.25deg 252.98deg,
                #b71c1c 252.98deg 262.71deg,
                #000000 262.71deg 272.44deg,
                #b71c1c 272.44deg 282.17deg,
                #000000 282.17deg 291.9deg,
                #b71c1c 291.9deg 301.63deg,
                #000000 301.63deg 311.36deg,
                #b71c1c 311.36deg 321.09deg,
                #000000 321.09deg 330.82deg,
                #b71c1c 330.82deg 340.55deg,
                #000000 340.55deg 350.28deg,
                #00a651 350.28deg 360deg
            );
            position: relative;
            transform: rotate(0deg);
            transition: transform 5s cubic-bezier(0.25, 0.1, 0.25, 1);
            display: flex;
            justify-content: center;
            align-items: center;
        }
        .wheel-center {
            width: 50px;
            height: 50px;
            background-color: #7b5d3f;
            border-radius: 50%;
            position: absolute;
            z-index: 10;
        }
        .wheel-numbers {
            position: absolute;
            width: 100%;
            height: 100%;
            border-radius: 50%;
        }
        .wheel-number {
            position: absolute;
            width: 30px;
            height: 30px;
            font-size: 12px;
            display: flex;
            justify-content: center;
            align-items: center;
            transform-origin: center 150px;
            font-weight: bold;
        }
        .wheel-ball {
            width: 15px;
            height: 15px;
            background-color: white;
            border-radius: 50%;
            position: absolute;
            top: 0;
            left: 50%;
            transform: translateX(-50%);
            z-index: 5;
        }
        .betting-grid {
            display: grid;
            grid-template-columns: repeat(13, 1fr);
            grid-template-rows: repeat(4, 1fr);
            gap: 2px;
            margin-top: 20px;
            flex-grow: 1;
        }
        .betting-cell {
            background-color: #0c3b2e;
            border: 1px solid #7b5d3f;
            display: flex;
            justify-content: center;
            align-items: center;
            font-weight: bold;
            cursor: pointer;
            position: relative;
            transition: all 0.2s;
        }
        .betting-cell:hover {
            background-color: rgba(123, 93, 63, 0.3);
        }
        .betting-cell.red {
            background-color: #b71c1c;
        }
        .betting-cell.black {
            background-color: #000000;
        }
        .betting-cell.green {
            background-color: #00a651;
        }
        .betting-cell.selected {
            box-shadow: inset 0 0 0 3px yellow;
        }
        .betting-chip {
            width: 25px;
            height: 25px;
            border-radius: 50%;
            position: absolute;
            top: 5px;
            right: 5px;
            display: flex;
            justify-content: center;
            align-items: center;
            font-size: 10px;
            font-weight: bold;
            color: black;
            z-index: 5;
        }
        .chip-5 {
            background-color: #f44336;
        }
        .chip-10 {
            background-color: #2196f3;
        }
        .chip-25 {
            background-color: #4caf50;
        }
        .chip-100 {
            background-color: #ff9800;
        }
        .controls {
            display: flex;
            justify-content: space-between;
            margin-top: 20px;
        }
        .chip-selector {
            display: flex;
            gap: 10px;
        }
        .chip {
            width: 40px;
            height: 40px;
            border-radius: 50%;
            display: flex;
            justify-content: center;
            align-items: center;
            font-weight: bold;
            cursor: pointer;
            transition: all 0.2s;
            border: 2px solid transparent;
        }
        .chip.selected {
            border-color: yellow;
            transform: scale(1.1);
        }
        .action-buttons {
            display: flex;
            gap: 10px;
        }
        .action-button {
            padding: 5px 15px;
            border: none;
            border-radius: 5px;
            font-weight: bold;
            cursor: pointer;
            transition: all 0.2s;
        }
        .action-button:hover {
            transform: scale(1.05);
        }
        .spin-button {
            background-color: #4caf50;
            color: white;
        }
        .clear-button {
            background-color: #f44336;
            color: white;
        }
        .result-display {
            background-color: rgba(0, 0, 0, 0.7);
            padding: 10px;
            border-radius: 5px;
            text-align: center;
            font-size: 1.2rem;
            font-weight: bold;
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
            color: #4caf50;
            font-size: 2.5rem;
            margin-top: 10px;
        }
        @keyframes spin {
            from { transform: rotate(0deg); }
            to { transform: rotate(360deg); }
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
            <div><strong>Game:</strong> <span id="gameName">Roulette</span></div>
            <div><strong>Min Bet:</strong> <span id="minBet">5</span></div>
            <div><strong>Max Bet:</strong> <span id="maxBet">500</span></div>
            <div><strong>Your Chips:</strong> <span id="playerChips">1,000</span></div>
            <div><strong>Total Bet:</strong> <span id="totalBet">0</span></div>
        </div>

        <!-- Roulette Table -->
        <div class="roulette-table">
            <div class="roulette-header">
                <div class="wheel-container">
                    <div class="wheel" id="rouletteWheel">
                        <div class="wheel-center"></div>
                        <div class="wheel-numbers" id="wheelNumbers">
                            <!-- Numbers will be added dynamically -->
                        </div>
                    </div>
                </div>
                <div class="result-display">
                    <div>Last Result: <span id="lastResult">-</span></div>
                    <div>Last Numbers: <span id="lastNumbers">-</span></div>
                </div>
            </div>
            
            <div class="betting-grid" id="bettingGrid">
                <!-- Betting grid will be generated dynamically -->
            </div>
            
            <div class="controls">
                <div class="chip-selector">
                    <div class="chip chip-5" data-value="5">5</div>
                    <div class="chip chip-10" data-value="10">10</div>
                    <div class="chip chip-25" data-value="25">25</div>
                    <div class="chip chip-100" data-value="100">100</div>
                </div>
                <div class="action-buttons">
                    <button class="action-button clear-button" id="clearBetsBtn">Clear Bets</button>
                    <button class="action-button spin-button" id="spinBtn">Spin</button>
                </div>
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

    <script>
        // Global variables
        let stompClient = null;
        let userToken = localStorage.getItem('token');
        let userData = null;
        let gameState = null;
        let sessionId = '${sessionId}';
        let playerId = null;
        let selectedChipValue = 5;
        let bets = {};
        let isSpinning = false;
        
        // Roulette numbers and their properties
        const rouletteNumbers = [
            { number: 0, color: 'green' },
            { number: 32, color: 'red' },
            { number: 15, color: 'black' },
            { number: 19, color: 'red' },
            { number: 4, color: 'black' },
            { number: 21, color: 'red' },
            { number: 2, color: 'black' },
            { number: 25, color: 'red' },
            { number: 17, color: 'black' },
            { number: 34, color: 'red' },
            { number: 6, color: 'black' },
            { number: 27, color: 'red' },
            { number: 13, color: 'black' },
            { number: 36, color: 'red' },
            { number: 11, color: 'black' },
            { number: 30, color: 'red' },
            { number: 8, color: 'black' },
            { number: 23, color: 'red' },
            { number: 10, color: 'black' },
            { number: 5, color: 'red' },
            { number: 24, color: 'black' },
            { number: 16, color: 'red' },
            { number: 33, color: 'black' },
            { number: 1, color: 'red' },
            { number: 20, color: 'black' },
            { number: 14, color: 'red' },
            { number: 31, color: 'black' },
            { number: 9, color: 'red' },
            { number: 22, color: 'black' },
            { number: 18, color: 'red' },
            { number: 29, color: 'black' },
            { number: 7, color: 'red' },
            { number: 28, color: 'black' },
            { number: 12, color: 'red' },
            { number: 35, color: 'black' },
            { number: 3, color: 'red' },
            { number: 26, color: 'black' }
        ];
        
        // Betting options
        const bettingOptions = [
            // First row - zero
            { id: 'zero', type: 'number', value: 0, text: '0', gridArea: '1 / 1 / 4 / 2', color: 'green' },
            
            // Numbers 1-36
            { id: 'num1', type: 'number', value: 1, text: '1', gridArea: '3 / 2 / 4 / 3', color: 'red' },
            { id: 'num2', type: 'number', value: 2, text: '2', gridArea: '2 / 2 / 3 / 3', color: 'black' },
            { id: 'num3', type: 'number', value: 3, text: '3', gridArea: '1 / 2 / 2 / 3', color: 'red' },
            { id: 'num4', type: 'number', value: 4, text: '4', gridArea: '3 / 3 / 4 / 4', color: 'black' },
            { id: 'num5', type: 'number', value: 5, text: '5', gridArea: '2 / 3 / 3 / 4', color: 'red' },
            { id: 'num6', type: 'number', value: 6, text: '6', gridArea: '1 / 3 / 2 / 4', color: 'black' },
            { id: 'num7', type: 'number', value: 7, text: '7', gridArea: '3 / 4 / 4 / 5', color: 'red' },
            { id: 'num8', type: 'number', value: 8, text: '8', gridArea: '2 / 4 / 3 / 5', color: 'black' },
            { id: 'num9', type: 'number', value: 9, text: '9', gridArea: '1 / 4 / 2 / 5', color: 'red' },
            { id: 'num10', type: 'number', value: 10, text: '10', gridArea: '3 / 5 / 4 / 6', color: 'black' },
            { id: 'num11', type: 'number', value: 11, text: '11', gridArea: '2 / 5 / 3 / 6', color: 'black' },
            { id: 'num12', type: 'number', value: 12, text: '12', gridArea: '1 / 5 / 2 / 6', color: 'red' },
            { id: 'num13', type: 'number', value: 13, text: '13', gridArea: '3 / 6 / 4 / 7', color: 'black' },
            { id: 'num14', type: 'number', value: 14, text: '14', gridArea: '2 / 6 / 3 / 7', color: 'red' },
            { id: 'num15', type: 'number', value: 15, text: '15', gridArea: '1 / 6 / 2 / 7', color: 'black' },
            { id: 'num16', type: 'number', value: 16, text: '16', gridArea: '3 / 7 / 4 / 8', color: 'red' },
            { id: 'num17', type: 'number', value: 17, text: '17', gridArea: '2 / 7 / 3 / 8', color: 'black' },
            { id: 'num18', type: 'number', value: 18, text: '18', gridArea: '1 / 7 / 2 / 8', color: 'red' },
            { id: 'num19', type: 'number', value: 19, text: '19', gridArea: '3 / 8 / 4 / 9', color: 'red' },
            { id: 'num20', type: 'number', value: 20, text: '20', gridArea: '2 / 8 / 3 / 9', color: 'black' },
            { id: 'num21', type: 'number', value: 21, text: '21', gridArea: '1 / 8 / 2 / 9', color: 'red' },
            { id: 'num22', type: 'number', value: 22, text: '22', gridArea: '3 / 9 / 4 / 10', color: 'black' },
            { id: 'num23', type: 'number', value: 23, text: '23', gridArea: '2 / 9 / 3 / 10', color: 'red' },
            { id: 'num24', type: 'number', value: 24, text: '24', gridArea: '1 / 9 / 2 / 10', color: 'black' },
            { id: 'num25', type: 'number', value: 25, text: '25', gridArea: '3 / 10 / 4 / 11', color: 'red' },
            { id: 'num26', type: 'number', value: 26, text: '26', gridArea: '2 / 10 / 3 / 11', color: 'black' },
            { id: 'num27', type: 'number', value: 27, text: '27', gridArea: '1 / 10 / 2 / 11', color: 'red' },
            { id: 'num28', type: 'number', value: 28, text: '28', gridArea: '3 / 11 / 4 / 12', color: 'black' },
            { id: 'num29', type: 'number', value: 29, text: '29', gridArea: '2 / 11 / 3 / 12', color: 'black' },
            { id: 'num30', type: 'number', value: 30, text: '30', gridArea: '1 / 11 / 2 / 12', color: 'red' },
            { id: 'num31', type: 'number', value: 31, text: '31', gridArea: '3 / 12 / 4 / 13', color: 'black' },
            { id: 'num32', type: 'number', value: 32, text: '32', gridArea: '2 / 12 / 3 / 13', color: 'red' },
            { id: 'num33', type: 'number', value: 33, text: '33', gridArea: '1 / 12 / 2 / 13', color: 'black' },
            { id: 'num34', type: 'number', value: 34, text: '34', gridArea: '3 / 13 / 4 / 14', color: 'red' },
            { id: 'num35', type: 'number', value: 35, text: '35', gridArea: '2 / 13 / 3 / 14', color: 'black' },
            { id: 'num36', type: 'number', value: 36, text: '36', gridArea: '1 / 13 / 2 / 14', color: 'red' },
            
            // Outside bets
            { id: 'first12', type: 'dozen', value: '1-12', text: '1st 12', gridArea: '4 / 2 / 5 / 6', color: '' },
            { id: 'second12', type: 'dozen', value: '13-24', text: '2nd 12', gridArea: '4 / 6 / 5 / 10', color: '' },
            { id: 'third12', type: 'dozen', value: '25-36', text: '3rd 12', gridArea: '4 / 10 / 5 / 14', color: '' },
            { id: 'red', type: 'color', value: 'red', text: 'RED', gridArea: '5 / 2 / 6 / 5', color: 'red' },
            { id: 'black', type: 'color', value: 'black', text: 'BLACK', gridArea: '5 / 5 / 6 / 8', color: 'black' },
            { id: 'odd', type: 'odd_even', value: 'odd', text: 'ODD', gridArea: '5 / 8 / 6 / 11', color: '' },
            { id: 'even', type: 'odd_even', value: 'even', text: 'EVEN', gridArea: '5 / 11 / 6 / 14', color: '' }
        ];

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

            // Initialize roulette wheel
            initializeWheel();
            
            // Initialize betting grid
            initializeBettingGrid();

            // Set up event handlers
            $('#leaveGameBtn').on('click', leaveGame);
            $('#spinBtn').on('click', spinWheel);
            $('#clearBetsBtn').on('click', clearBets);
            $('.chip').on('click', selectChip);
            $('#sendChatBtn').on('click', sendChatMessage);
            $('#chatInput').on('keypress', function(e) {
                if (e.which === 13) {
                    sendChatMessage();
                }
            });

            // Select default chip
            $('.chip[data-value="5"]').addClass('selected');

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

        function initializeWheel() {
            const wheelNumbers = $('#wheelNumbers');
            
            // Add numbers to the wheel
            rouletteNumbers.forEach((numObj, index) => {
                const angle = (index * 9.73) + 4.865; // 360 / 37 = 9.73 degrees per number, offset by half
                const numberDiv = $('<div class="wheel-number"></div>')
                    .text(numObj.number)
                    .css('transform', `rotate(${angle}deg)`)
                    .css('color', numObj.color === 'red' ? 'red' : 'white');
                
                wheelNumbers.append(numberDiv);
            });
        }

        function initializeBettingGrid() {
            const grid = $('#bettingGrid');
            
            // Add betting options to the grid
            bettingOptions.forEach(option => {
                const cell = $('<div class="betting-cell"></div>')
                    .attr('id', option.id)
                    .attr('data-type', option.type)
                    .attr('data-value', option.value)
                    .text(option.text)
                    .css('grid-area', option.gridArea);
                
                if (option.color) {
                    cell.addClass(option.color);
                }
                
                cell.on('click', function() {
                    placeBet($(this));
                });
                
                grid.append(cell);
            });
        }

        function selectChip() {
            $('.chip').removeClass('selected');
            $(this).addClass('selected');
            selectedChipValue = parseInt($(this).data('value'));
        }

        function placeBet(cell) {
            if (isSpinning) return;
            
            const betType = cell.data('type');
            const betValue = cell.data('value');
            const betId = cell.attr('id');
            const playerChips = parseInt($('#playerChips').text().replace(/,/g, ''));
            
            if (selectedChipValue > playerChips) {
                alert('Not enough chips!');
                return;
            }
            
            // Add or update bet
            if (bets[betId]) {
                bets[betId].amount += selectedChipValue;
            } else {
                bets[betId] = {
                    type: betType,
                    value: betValue,
                    amount: selectedChipValue
                };
            }
            
            // Update UI
            updateBetUI(cell, bets[betId].amount);
            updateTotalBet();
        }

        function updateBetUI(cell, amount) {
            // Remove existing chip
            cell.find('.betting-chip').remove();
            
            // Add new chip
            let chipClass = 'chip-5';
            if (amount >= 100) chipClass = 'chip-100';
            else if (amount >= 25) chipClass = 'chip-25';
            else if (amount >= 10) chipClass = 'chip-10';
            
            const chip = $('<div class="betting-chip"></div>')
                .addClass(chipClass)
                .text(amount);
            
            cell.append(chip);
            cell.addClass('selected');
        }

        function updateTotalBet() {
            let total = 0;
            for (const betId in bets) {
                total += bets[betId].amount;
            }
            $('#totalBet').text(formatNumber(total));
        }

        function clearBets() {
            if (isSpinning) return;
            
            bets = {};
            $('.betting-cell').removeClass('selected').find('.betting-chip').remove();
            $('#totalBet').text('0');
        }

        function spinWheel() {
            if (isSpinning) return;
            
            const totalBet = parseInt($('#totalBet').text().replace(/,/g, ''));
            if (totalBet === 0) {
                alert('Please place a bet first!');
                return;
            }
            
            isSpinning = true;
            $('#spinBtn').prop('disabled', true);
            $('#clearBetsBtn').prop('disabled', true);
            
            // Send spin action to server with all bets
            sendGameAction('SPIN', {
                bets: bets,
                totalAmount: totalBet
            });
            
            // Simulate spinning (will be replaced by actual server response)
            const randomNumber = rouletteNumbers[Math.floor(Math.random() * rouletteNumbers.length)].number;
            const rotations = 5; // Number of full rotations
            const numberIndex = rouletteNumbers.findIndex(n => n.number === randomNumber);
            const angleToNumber = 360 - (numberIndex * 9.73); // Degrees to the winning number
            const totalRotation = (rotations * 360) + angleToNumber;
            
            // Animate wheel
            $('#rouletteWheel').css('transform', `rotate(${totalRotation}deg)`);
            
            // After animation completes, show result
            setTimeout(() => {
                // This will be replaced by the server response
                const result = {
                    number: randomNumber,
                    color: rouletteNumbers.find(n => n.number === randomNumber).color,
                    winningBets: calculateWinningBets(randomNumber),
                    totalWin: 0
                };
                
                // Calculate total win
                for (const betId in result.winningBets) {
                    result.totalWin += result.winningBets[betId];
                }
                
                showSpinResult(result);
            }, 5000); // Match the CSS transition time
        }

        function calculateWinningBets(winningNumber) {
            const winningColor = rouletteNumbers.find(n => n.number === winningNumber).color;
            const isEven = winningNumber !== 0 && winningNumber % 2 === 0;
            const winningBets = {};
            
            for (const betId in bets) {
                const bet = bets[betId];
                let win = 0;
                
                switch (bet.type) {
                    case 'number':
                        if (bet.value === winningNumber) {
                            win = bet.amount * 36; // 35:1 plus original bet
                        }
                        break;
                    case 'color':
                        if (bet.value === winningColor) {
                            win = bet.amount * 2; // 1:1 plus original bet
                        }
                        break;
                    case 'odd_even':
                        if ((bet.value === 'odd' && winningNumber !== 0 && !isEven) ||
                            (bet.value === 'even' && isEven)) {
                            win = bet.amount * 2; // 1:1 plus original bet
                        }
                        break;
                    case 'dozen':
                        const num = winningNumber;
                        if ((bet.value === '1-12' && num >= 1 && num <= 12) ||
                            (bet.value === '13-24' && num >= 13 && num <= 24) ||
                            (bet.value === '25-36' && num >= 25 && num <= 36)) {
                            win = bet.amount * 3; // 2:1 plus original bet
                        }
                        break;
                }
                
                if (win > 0) {
                    winningBets[betId] = win;
                }
            }
            
            return winningBets;
        }

        function showSpinResult(result) {
            // Update last result display
            $('#lastResult').text(`${result.number} ${result.color.toUpperCase()}`);
            
            // Update last numbers
            const currentLastNumbers = $('#lastNumbers').text();
            if (currentLastNumbers === '-') {
                $('#lastNumbers').text(result.number);
            } else {
                const numbers = currentLastNumbers.split(', ');
                numbers.unshift(result.number);
                if (numbers.length > 5) numbers.pop();
                $('#lastNumbers').text(numbers.join(', '));
            }
            
            // Highlight winning bets
            $('.betting-cell').removeClass('winner');
            for (const betId in result.winningBets) {
                $(`#${betId}`).addClass('winner');
            }
            
            // Show win message if won
            if (result.totalWin > 0) {
                $('#winAmount').text('+' + result.totalWin);
                $('#winMessage').fadeIn(500).delay(2000).fadeOut(500);
                
                // Update player chips (this would normally come from the server)
                const currentChips = parseInt($('#playerChips').text().replace(/,/g, ''));
                $('#playerChips').text(formatNumber(currentChips + result.totalWin));
                $('#chipBalance').text(formatNumber(currentChips + result.totalWin));
            }
            
            // Reset for next round
            setTimeout(() => {
                isSpinning = false;
                $('#spinBtn').prop('disabled', false);
                $('#clearBetsBtn').prop('disabled', false);
                clearBets();
            }, 3000);
        }

        function sendGameAction(action, data = {}) {
            stompClient.send('/app/game.action', {}, JSON.stringify({
                sessionId: sessionId,
                action: action,
                data: data
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
