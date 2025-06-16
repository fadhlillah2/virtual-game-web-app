<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>Virtual Game - Friends</title>
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
        .chip-balance {
            background-color: #28a745;
            color: white;
            padding: 5px 10px;
            border-radius: 20px;
        }
        .friend-card {
            transition: all 0.3s;
        }
        .friend-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 8px 16px rgba(0,0,0,0.2);
        }
        .friend-avatar {
            width: 64px;
            height: 64px;
            border-radius: 50%;
            object-fit: cover;
        }
        .online-indicator {
            width: 12px;
            height: 12px;
            border-radius: 50%;
            display: inline-block;
            margin-right: 5px;
        }
        .online {
            background-color: #28a745;
        }
        .offline {
            background-color: #dc3545;
        }
        .friend-actions {
            display: flex;
            gap: 5px;
        }
        .search-box {
            position: relative;
            margin-bottom: 20px;
        }
        .search-box .form-control {
            padding-right: 40px;
        }
        .search-box .btn {
            position: absolute;
            right: 0;
            top: 0;
            border-radius: 0 0.25rem 0.25rem 0;
        }
        .empty-state {
            text-align: center;
            padding: 40px 20px;
        }
        .empty-state i {
            font-size: 4rem;
            color: #6c757d;
            margin-bottom: 20px;
        }
        .badge-notification {
            position: absolute;
            top: -5px;
            right: -5px;
            font-size: 0.7rem;
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
        <div class="row">
            <div class="col-md-8">
                <div class="card">
                    <div class="card-header d-flex justify-content-between align-items-center">
                        <h4 class="mb-0"><i class="fas fa-user-friends"></i> My Friends</h4>
                        <button class="btn btn-sm btn-light" data-bs-toggle="modal" data-bs-target="#addFriendModal">
                            <i class="fas fa-user-plus"></i> Add Friend
                        </button>
                    </div>
                    <div class="card-body">
                        <div class="search-box">
                            <input type="text" class="form-control" id="friendSearch" placeholder="Search friends...">
                            <button class="btn btn-primary"><i class="fas fa-search"></i></button>
                        </div>
                        
                        <div id="friendsList" class="row">
                            <!-- Friends will be loaded here -->
                        </div>
                        
                        <div id="emptyFriendsList" class="empty-state d-none">
                            <i class="fas fa-user-friends"></i>
                            <h4>No Friends Yet</h4>
                            <p>Add friends to play games together and send gifts!</p>
                            <button class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#addFriendModal">
                                <i class="fas fa-user-plus"></i> Add Friend
                            </button>
                        </div>
                    </div>
                </div>
            </div>
            
            <div class="col-md-4">
                <div class="card">
                    <div class="card-header d-flex justify-content-between align-items-center">
                        <h4 class="mb-0">
                            <i class="fas fa-bell"></i> Friend Requests
                            <span id="requestCount" class="badge bg-danger ms-2 d-none">0</span>
                        </h4>
                    </div>
                    <div class="card-body">
                        <div id="pendingRequestsList">
                            <!-- Pending requests will be loaded here -->
                        </div>
                        
                        <div id="emptyRequestsList" class="empty-state d-none">
                            <i class="fas fa-bell-slash"></i>
                            <h5>No Pending Requests</h5>
                            <p>You don't have any friend requests at the moment.</p>
                        </div>
                    </div>
                </div>
                
                <div class="card mt-4">
                    <div class="card-header">
                        <h4 class="mb-0"><i class="fas fa-gift"></i> Send Gifts</h4>
                    </div>
                    <div class="card-body">
                        <p>Send chips to your friends as gifts!</p>
                        <button class="btn btn-primary w-100" id="sendGiftBtn" data-bs-toggle="modal" data-bs-target="#sendGiftModal">
                            <i class="fas fa-gift"></i> Send Gift
                        </button>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <!-- Add Friend Modal -->
    <div class="modal fade" id="addFriendModal" tabindex="-1">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Add Friend</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <form id="addFriendForm">
                        <div class="mb-3">
                            <label for="friendUsername" class="form-label">Username</label>
                            <input type="text" class="form-control" id="friendUsername" required>
                            <div class="form-text">Enter the username of the player you want to add as a friend.</div>
                        </div>
                    </form>
                    <div id="addFriendError" class="alert alert-danger d-none"></div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                    <button type="button" class="btn btn-primary" id="addFriendBtn">Send Request</button>
                </div>
            </div>
        </div>
    </div>
    
    <!-- Send Gift Modal -->
    <div class="modal fade" id="sendGiftModal" tabindex="-1">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Send Gift</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <form id="sendGiftForm">
                        <div class="mb-3">
                            <label for="giftRecipient" class="form-label">Friend</label>
                            <select class="form-select" id="giftRecipient" required>
                                <option value="">Select a friend</option>
                                <!-- Friends will be loaded here -->
                            </select>
                        </div>
                        <div class="mb-3">
                            <label for="giftAmount" class="form-label">Amount (Chips)</label>
                            <input type="number" class="form-control" id="giftAmount" min="10" max="1000" value="100" required>
                            <div class="form-text">You can send between 10 and 1,000 chips.</div>
                        </div>
                        <div class="mb-3">
                            <label for="giftMessage" class="form-label">Message (Optional)</label>
                            <textarea class="form-control" id="giftMessage" rows="3"></textarea>
                        </div>
                    </form>
                    <div id="sendGiftError" class="alert alert-danger d-none"></div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                    <button type="button" class="btn btn-primary" id="confirmGiftBtn">Send Gift</button>
                </div>
            </div>
        </div>
    </div>
    
    <script>
        // Global variables
        let userToken = localStorage.getItem('token');
        let userData = null;
        let friends = [];
        let pendingRequests = [];
        
        $(document).ready(function() {
            // Check if user is logged in
            if (!userToken) {
                window.location.href = '/login';
                return;
            }
            
            // Load user data
            loadUserData();
            
            // Load friends and pending requests
            loadFriends();
            loadPendingRequests();
            
            // Set up event handlers
            $('#friendSearch').on('input', filterFriends);
            $('#addFriendBtn').on('click', addFriend);
            $('#confirmGiftBtn').on('click', sendGift);
            $('#claimDailyBonus').on('click', claimDailyBonus);
            $('#logoutBtn').on('click', logout);
            
            // Refresh data every 60 seconds
            setInterval(function() {
                loadFriends();
                loadPendingRequests();
            }, 60000);
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
        
        function loadFriends() {
            $.ajax({
                url: '/friends/api',
                type: 'GET',
                headers: {
                    'Authorization': 'Bearer ' + userToken
                },
                success: function(response) {
                    if (response.success) {
                        friends = response.data;
                        displayFriends(friends);
                        updateGiftRecipients();
                    }
                }
            });
        }
        
        function loadPendingRequests() {
            $.ajax({
                url: '/friends/api/pending',
                type: 'GET',
                headers: {
                    'Authorization': 'Bearer ' + userToken
                },
                success: function(response) {
                    if (response.success) {
                        pendingRequests = response.data;
                        displayPendingRequests(pendingRequests);
                    }
                }
            });
        }
        
        function displayFriends(friendsList) {
            const container = $('#friendsList');
            container.empty();
            
            if (friendsList.length === 0) {
                $('#emptyFriendsList').removeClass('d-none');
                return;
            }
            
            $('#emptyFriendsList').addClass('d-none');
            
            friendsList.forEach(function(friend) {
                const avatarUrl = friend.avatarUrl || 'https://via.placeholder.com/64';
                const lastActivity = friend.lastActivity ? new Date(friend.lastActivity).toLocaleString() : 'Never';
                
                const html = `
                    <div class="col-md-6 mb-3">
                        <div class="card friend-card">
                            <div class="card-body">
                                <div class="d-flex align-items-center mb-3">
                                    <img src="${avatarUrl}" class="friend-avatar me-3" alt="${friend.username}">
                                    <div>
                                        <h5 class="mb-1">
                                            <span class="online-indicator ${friend.online ? 'online' : 'offline'}"></span>
                                            ${friend.username}
                                        </h5>
                                        <div class="text-muted small">Last seen: ${lastActivity}</div>
                                    </div>
                                </div>
                                <div class="d-flex justify-content-between align-items-center">
                                    <div>
                                        <i class="fas fa-coins"></i> ${formatNumber(friend.chipsBalance)}
                                    </div>
                                    <div class="friend-actions">
                                        <button class="btn btn-sm btn-outline-primary gift-btn" data-id="${friend.id}" data-username="${friend.username}">
                                            <i class="fas fa-gift"></i>
                                        </button>
                                        <button class="btn btn-sm btn-outline-danger remove-friend-btn" data-id="${friend.id}" data-username="${friend.username}">
                                            <i class="fas fa-user-minus"></i>
                                        </button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                `;
                
                container.append(html);
            });
            
            // Set up event handlers for the dynamically created buttons
            $('.gift-btn').on('click', function() {
                const friendId = $(this).data('id');
                const friendUsername = $(this).data('username');
                
                $('#giftRecipient').val(friendId);
                $('#sendGiftModal').modal('show');
            });
            
            $('.remove-friend-btn').on('click', function() {
                const friendId = $(this).data('id');
                const friendUsername = $(this).data('username');
                
                if (confirm(`Are you sure you want to remove ${friendUsername} from your friends list?`)) {
                    removeFriend(friendId);
                }
            });
        }
        
        function displayPendingRequests(requestsList) {
            const container = $('#pendingRequestsList');
            container.empty();
            
            if (requestsList.length === 0) {
                $('#emptyRequestsList').removeClass('d-none');
                $('#requestCount').addClass('d-none');
                return;
            }
            
            $('#emptyRequestsList').addClass('d-none');
            $('#requestCount').removeClass('d-none').text(requestsList.length);
            
            requestsList.forEach(function(request) {
                const avatarUrl = request.avatarUrl || 'https://via.placeholder.com/64';
                
                const html = `
                    <div class="card mb-3">
                        <div class="card-body">
                            <div class="d-flex align-items-center mb-3">
                                <img src="${avatarUrl}" class="friend-avatar me-3" alt="${request.username}" style="width: 48px; height: 48px;">
                                <div>
                                    <h5 class="mb-1">${request.username}</h5>
                                    <div class="text-muted small">Sent a friend request</div>
                                </div>
                            </div>
                            <div class="d-flex justify-content-end gap-2">
                                <button class="btn btn-sm btn-success accept-request-btn" data-id="${request.id}" data-friendship-id="${request.friendshipId}">
                                    <i class="fas fa-check"></i> Accept
                                </button>
                                <button class="btn btn-sm btn-danger reject-request-btn" data-id="${request.id}" data-friendship-id="${request.friendshipId}">
                                    <i class="fas fa-times"></i> Reject
                                </button>
                            </div>
                        </div>
                    </div>
                `;
                
                container.append(html);
            });
            
            // Set up event handlers for the dynamically created buttons
            $('.accept-request-btn').on('click', function() {
                const friendshipId = $(this).data('friendship-id');
                respondToFriendRequest(friendshipId, true);
            });
            
            $('.reject-request-btn').on('click', function() {
                const friendshipId = $(this).data('friendship-id');
                respondToFriendRequest(friendshipId, false);
            });
        }
        
        function filterFriends() {
            const searchTerm = $('#friendSearch').val().toLowerCase();
            
            if (!searchTerm) {
                displayFriends(friends);
                return;
            }
            
            const filteredFriends = friends.filter(function(friend) {
                return friend.username.toLowerCase().includes(searchTerm);
            });
            
            displayFriends(filteredFriends);
        }
        
        function updateGiftRecipients() {
            const select = $('#giftRecipient');
            select.empty();
            
            select.append('<option value="">Select a friend</option>');
            
            friends.forEach(function(friend) {
                select.append(`<option value="${friend.id}">${friend.username}</option>`);
            });
        }
        
        function addFriend() {
            const username = $('#friendUsername').val().trim();
            
            if (!username) {
                $('#addFriendError').removeClass('d-none').text('Please enter a username');
                return;
            }
            
            $('#addFriendBtn').prop('disabled', true);
            $('#addFriendError').addClass('d-none');
            
            $.ajax({
                url: '/friends/api/add',
                type: 'POST',
                headers: {
                    'Authorization': 'Bearer ' + userToken,
                    'Content-Type': 'application/json'
                },
                data: JSON.stringify({
                    username: username
                }),
                success: function(response) {
                    if (response.success) {
                        $('#addFriendModal').modal('hide');
                        $('#friendUsername').val('');
                        alert('Friend request sent successfully!');
                        loadFriends();
                    } else {
                        $('#addFriendError').removeClass('d-none').text(response.message);
                    }
                },
                error: function(xhr) {
                    $('#addFriendError').removeClass('d-none').text(xhr.responseJSON ? xhr.responseJSON.message : 'Failed to send friend request');
                },
                complete: function() {
                    $('#addFriendBtn').prop('disabled', false);
                }
            });
        }
        
        function respondToFriendRequest(friendshipId, accept) {
            $.ajax({
                url: '/friends/api/respond',
                type: 'POST',
                headers: {
                    'Authorization': 'Bearer ' + userToken,
                    'Content-Type': 'application/json'
                },
                data: JSON.stringify({
                    friendshipId: friendshipId,
                    accept: accept
                }),
                success: function(response) {
                    if (response.success) {
                        loadPendingRequests();
                        loadFriends();
                    } else {
                        alert(response.message);
                    }
                },
                error: function(xhr) {
                    alert(xhr.responseJSON ? xhr.responseJSON.message : 'Failed to respond to friend request');
                }
            });
        }
        
        function removeFriend(friendId) {
            $.ajax({
                url: '/friends/api/' + friendId,
                type: 'DELETE',
                headers: {
                    'Authorization': 'Bearer ' + userToken
                },
                success: function(response) {
                    if (response.success) {
                        loadFriends();
                    } else {
                        alert(response.message);
                    }
                },
                error: function(xhr) {
                    alert(xhr.responseJSON ? xhr.responseJSON.message : 'Failed to remove friend');
                }
            });
        }
        
        function sendGift() {
            const recipientId = $('#giftRecipient').val();
            const amount = parseInt($('#giftAmount').val());
            const message = $('#giftMessage').val();
            
            if (!recipientId) {
                $('#sendGiftError').removeClass('d-none').text('Please select a friend');
                return;
            }
            
            if (isNaN(amount) || amount < 10 || amount > 1000) {
                $('#sendGiftError').removeClass('d-none').text('Please enter a valid amount between 10 and 1,000 chips');
                return;
            }
            
            $('#confirmGiftBtn').prop('disabled', true);
            $('#sendGiftError').addClass('d-none');
            
            $.ajax({
                url: '/api/chips/gift',
                type: 'POST',
                headers: {
                    'Authorization': 'Bearer ' + userToken,
                    'Content-Type': 'application/json'
                },
                data: JSON.stringify({
                    recipientId: recipientId,
                    amount: amount,
                    message: message
                }),
                success: function(response) {
                    if (response.success) {
                        $('#sendGiftModal').modal('hide');
                        $('#giftRecipient').val('');
                        $('#giftAmount').val('100');
                        $('#giftMessage').val('');
                        alert('Gift sent successfully!');
                        loadUserData();
                    } else {
                        $('#sendGiftError').removeClass('d-none').text(response.message);
                    }
                },
                error: function(xhr) {
                    $('#sendGiftError').removeClass('d-none').text(xhr.responseJSON ? xhr.responseJSON.message : 'Failed to send gift');
                },
                complete: function() {
                    $('#confirmGiftBtn').prop('disabled', false);
                }
            });
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
