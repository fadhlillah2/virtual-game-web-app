<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>Virtual Game - Leaderboard</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <script src="https://code.jquery.com/jquery-3.7.0.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
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
        .leaderboard-table th {
            background-color: #f0f0f0;
        }
        .leaderboard-table .rank-1 {
            background-color: #ffd700;
            font-weight: bold;
        }
        .leaderboard-table .rank-2 {
            background-color: #c0c0c0;
            font-weight: bold;
        }
        .leaderboard-table .rank-3 {
            background-color: #cd7f32;
            font-weight: bold;
        }
        .nav-pills .nav-link.active {
            background-color: #6f42c1;
        }
        .nav-pills .nav-link {
            color: #6f42c1;
        }
        .chip-balance {
            background-color: #28a745;
            color: white;
            padding: 5px 10px;
            border-radius: 20px;
        }
        .user-avatar {
            width: 40px;
            height: 40px;
            border-radius: 50%;
            object-fit: cover;
        }
        .user-rank {
            font-size: 1.2rem;
            font-weight: bold;
            width: 40px;
            height: 40px;
            border-radius: 50%;
            background-color: #f0f0f0;
            display: flex;
            justify-content: center;
            align-items: center;
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
                        <a class="nav-link" href="/lobby">Lobby</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link active" href="/leaderboard">Leaderboard</a>
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
        <div class="row">
            <div class="col-12">
                <div class="card">
                    <div class="card-header d-flex justify-content-between align-items-center">
                        <h4 class="mb-0"><i class="fas fa-trophy"></i> Leaderboard</h4>
                        <div>
                            <select class="form-select form-select-sm" id="leaderboardType">
                                <option value="chips">Chips</option>
                                <option value="wins">Wins</option>
                                <option value="winrate">Win Rate</option>
                            </select>
                        </div>
                    </div>
                    <div class="card-body">
                        <ul class="nav nav-pills mb-3" id="leaderboardTabs" role="tablist">
                            <li class="nav-item" role="presentation">
                                <button class="nav-link active" id="global-tab" data-bs-toggle="pill" data-bs-target="#global" type="button" role="tab">Global</button>
                            </li>
                            <li class="nav-item" role="presentation">
                                <button class="nav-link" id="poker-tab" data-bs-toggle="pill" data-bs-target="#poker" type="button" role="tab">Poker</button>
                            </li>
                            <li class="nav-item" role="presentation">
                                <button class="nav-link" id="blackjack-tab" data-bs-toggle="pill" data-bs-target="#blackjack" type="button" role="tab">Blackjack</button>
                            </li>
                            <li class="nav-item" role="presentation">
                                <button class="nav-link" id="slots-tab" data-bs-toggle="pill" data-bs-target="#slots" type="button" role="tab">Slots</button>
                            </li>
                            <li class="nav-item" role="presentation">
                                <button class="nav-link" id="roulette-tab" data-bs-toggle="pill" data-bs-target="#roulette" type="button" role="tab">Roulette</button>
                            </li>
                        </ul>
                        <div class="tab-content" id="leaderboardTabContent">
                            <div class="tab-pane fade show active" id="global" role="tabpanel">
                                <div class="table-responsive">
                                    <table class="table table-hover leaderboard-table">
                                        <thead>
                                            <tr>
                                                <th>Rank</th>
                                                <th>Player</th>
                                                <th>Score</th>
                                                <th>Games Played</th>
                                                <th>Games Won</th>
                                                <th>Win Rate</th>
                                            </tr>
                                        </thead>
                                        <tbody id="globalLeaderboard">
                                            <!-- Global leaderboard data will be loaded here -->
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                            <div class="tab-pane fade" id="poker" role="tabpanel">
                                <div class="table-responsive">
                                    <table class="table table-hover leaderboard-table">
                                        <thead>
                                            <tr>
                                                <th>Rank</th>
                                                <th>Player</th>
                                                <th>Score</th>
                                                <th>Games Played</th>
                                                <th>Games Won</th>
                                                <th>Win Rate</th>
                                            </tr>
                                        </thead>
                                        <tbody id="pokerLeaderboard">
                                            <!-- Poker leaderboard data will be loaded here -->
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                            <div class="tab-pane fade" id="blackjack" role="tabpanel">
                                <div class="table-responsive">
                                    <table class="table table-hover leaderboard-table">
                                        <thead>
                                            <tr>
                                                <th>Rank</th>
                                                <th>Player</th>
                                                <th>Score</th>
                                                <th>Games Played</th>
                                                <th>Games Won</th>
                                                <th>Win Rate</th>
                                            </tr>
                                        </thead>
                                        <tbody id="blackjackLeaderboard">
                                            <!-- Blackjack leaderboard data will be loaded here -->
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                            <div class="tab-pane fade" id="slots" role="tabpanel">
                                <div class="table-responsive">
                                    <table class="table table-hover leaderboard-table">
                                        <thead>
                                            <tr>
                                                <th>Rank</th>
                                                <th>Player</th>
                                                <th>Score</th>
                                                <th>Games Played</th>
                                                <th>Games Won</th>
                                                <th>Win Rate</th>
                                            </tr>
                                        </thead>
                                        <tbody id="slotsLeaderboard">
                                            <!-- Slots leaderboard data will be loaded here -->
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                            <div class="tab-pane fade" id="roulette" role="tabpanel">
                                <div class="table-responsive">
                                    <table class="table table-hover leaderboard-table">
                                        <thead>
                                            <tr>
                                                <th>Rank</th>
                                                <th>Player</th>
                                                <th>Score</th>
                                                <th>Games Played</th>
                                                <th>Games Won</th>
                                                <th>Win Rate</th>
                                            </tr>
                                        </thead>
                                        <tbody id="rouletteLeaderboard">
                                            <!-- Roulette leaderboard data will be loaded here -->
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <script>
        // Global variables
        let userToken = localStorage.getItem('token');
        let userData = null;
        let currentLeaderboardType = 'chips';
        
        $(document).ready(function() {
            // Check if user is logged in
            if (!userToken) {
                window.location.href = '/login';
                return;
            }
            
            // Load user data
            loadUserData();
            
            // Load global leaderboard
            loadGlobalLeaderboard();
            
            // Set up event handlers
            $('#leaderboardType').on('change', function() {
                currentLeaderboardType = $(this).val();
                loadAllLeaderboards();
            });
            
            $('#leaderboardTabs button').on('shown.bs.tab', function(e) {
                const targetId = $(e.target).attr('id');
                if (targetId === 'poker-tab') {
                    loadGameLeaderboard('POKER', '#pokerLeaderboard');
                } else if (targetId === 'blackjack-tab') {
                    loadGameLeaderboard('BLACKJACK', '#blackjackLeaderboard');
                } else if (targetId === 'slots-tab') {
                    loadGameLeaderboard('SLOTS', '#slotsLeaderboard');
                } else if (targetId === 'roulette-tab') {
                    loadGameLeaderboard('ROULETTE', '#rouletteLeaderboard');
                }
            });
            
            $('#claimDailyBonus').on('click', claimDailyBonus);
            $('#logoutBtn').on('click', logout);
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
        
        function loadAllLeaderboards() {
            loadGlobalLeaderboard();
            
            // Load game-specific leaderboards if their tabs are active
            if ($('#poker-tab').hasClass('active')) {
                loadGameLeaderboard('POKER', '#pokerLeaderboard');
            }
            if ($('#blackjack-tab').hasClass('active')) {
                loadGameLeaderboard('BLACKJACK', '#blackjackLeaderboard');
            }
            if ($('#slots-tab').hasClass('active')) {
                loadGameLeaderboard('SLOTS', '#slotsLeaderboard');
            }
            if ($('#roulette-tab').hasClass('active')) {
                loadGameLeaderboard('ROULETTE', '#rouletteLeaderboard');
            }
        }
        
        function loadGlobalLeaderboard() {
            $.ajax({
                url: '/leaderboard/api',
                type: 'GET',
                headers: {
                    'Authorization': 'Bearer ' + userToken
                },
                data: {
                    type: currentLeaderboardType,
                    limit: 20
                },
                success: function(response) {
                    if (response.success) {
                        displayLeaderboard(response.data, '#globalLeaderboard');
                    }
                }
            });
        }
        
        function loadGameLeaderboard(gameType, targetSelector) {
            $.ajax({
                url: '/leaderboard/api/game',
                type: 'GET',
                headers: {
                    'Authorization': 'Bearer ' + userToken
                },
                data: {
                    gameType: gameType,
                    limit: 20
                },
                success: function(response) {
                    if (response.success) {
                        displayLeaderboard(response.data, targetSelector);
                    }
                }
            });
        }
        
        function displayLeaderboard(entries, targetSelector) {
            let html = '';
            
            if (entries.length === 0) {
                html = '<tr><td colspan="6" class="text-center">No data available</td></tr>';
            } else {
                entries.forEach(function(entry) {
                    const rankClass = entry.rank <= 3 ? 'rank-' + entry.rank : '';
                    const avatarUrl = entry.avatarUrl || 'https://via.placeholder.com/40';
                    const isCurrentUser = userData && entry.id === userData.id;
                    
                    html += `
                        <tr class="${rankClass} ${isCurrentUser ? 'table-primary' : ''}">
                            <td>
                                <div class="user-rank">${entry.rank}</div>
                            </td>
                            <td>
                                <div class="d-flex align-items-center">
                                    <img src="${avatarUrl}" class="user-avatar me-2" alt="${entry.username}">
                                    <span>${entry.username} ${isCurrentUser ? '(You)' : ''}</span>
                                </div>
                            </td>
                            <td>${formatNumber(entry.score)}</td>
                            <td>${formatNumber(entry.gamesPlayed)}</td>
                            <td>${formatNumber(entry.gamesWon)}</td>
                            <td>${(entry.winRate * 100).toFixed(1)}%</td>
                        </tr>
                    `;
                });
            }
            
            $(targetSelector).html(html);
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
        
        function formatNumber(number) {
            return number.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
        }
    </script>
</body>
</html>
