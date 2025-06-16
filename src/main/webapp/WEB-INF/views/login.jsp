<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Virtual Game - Login</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://code.jquery.com/jquery-3.7.0.min.js"></script>
    <style>
        body {
            background-color: #f8f9fa;
        }
        .login-container {
            max-width: 450px;
            margin: 100px auto;
        }
        .card {
            border-radius: 10px;
            box-shadow: 0 4px 8px rgba(0,0,0,0.1);
        }
        .card-header {
            background-color: #6f42c1;
            color: white;
            text-align: center;
            border-radius: 10px 10px 0 0 !important;
            padding: 20px;
        }
        .btn-primary {
            background-color: #6f42c1;
            border-color: #6f42c1;
        }
        .btn-primary:hover {
            background-color: #5a32a3;
            border-color: #5a32a3;
        }
        .form-control:focus {
            border-color: #6f42c1;
            box-shadow: 0 0 0 0.25rem rgba(111, 66, 193, 0.25);
        }
    </style>
</head>
<body>
    <div class="container login-container">
        <div class="card">
            <div class="card-header">
                <h3>Virtual Game Platform</h3>
                <p class="mb-0">Sign in to your account</p>
            </div>
            <div class="card-body p-4">
                <c:if test="${not empty error}">
                    <div class="alert alert-danger" role="alert">
                        ${error}
                    </div>
                </c:if>
                <c:if test="${not empty message}">
                    <div class="alert alert-success" role="alert">
                        ${message}
                    </div>
                </c:if>
                
                <form id="loginForm">
                    <div class="mb-3">
                        <label for="emailOrUsername" class="form-label">Email or Username</label>
                        <input type="text" class="form-control" id="emailOrUsername" name="emailOrUsername" required>
                    </div>
                    
                    <div class="mb-3">
                        <label for="password" class="form-label">Password</label>
                        <input type="password" class="form-control" id="password" name="password" required>
                    </div>
                    
                    <div class="mb-3 form-check">
                        <input type="checkbox" class="form-check-input" id="rememberMe" name="rememberMe">
                        <label class="form-check-label" for="rememberMe">Remember me</label>
                    </div>
                    
                    <button type="submit" class="btn btn-primary w-100 mb-3">Sign In</button>
                    
                    <div class="text-center">
                        <p>Don't have an account? <a href="/register">Register here</a></p>
                    </div>
                </form>
            </div>
        </div>
    </div>
    
    <script>
        $(document).ready(function() {
            $('#loginForm').on('submit', function(e) {
                e.preventDefault();
                
                $.ajax({
                    url: '/api/auth/login',
                    type: 'POST',
                    contentType: 'application/json',
                    data: JSON.stringify({
                        emailOrUsername: $('#emailOrUsername').val(),
                        password: $('#password').val(),
                        rememberMe: $('#rememberMe').is(':checked')
                    }),
                    success: function(response) {
                        if (response.success) {
                            // Store token in localStorage or sessionStorage
                            localStorage.setItem('token', response.data.accessToken);
                            localStorage.setItem('refreshToken', response.data.refreshToken);
                            
                            // Redirect to game lobby
                            window.location.href = '/lobby';
                        } else {
                            // Show error message
                            $('.card-body').prepend(
                                '<div class="alert alert-danger" role="alert">' + 
                                response.message + 
                                '</div>'
                            );
                        }
                    },
                    error: function(xhr) {
                        // Show error message
                        $('.card-body').prepend(
                            '<div class="alert alert-danger" role="alert">' + 
                            (xhr.responseJSON ? xhr.responseJSON.message : 'Login failed. Please try again.') + 
                            '</div>'
                        );
                    }
                });
            });
        });
    </script>
</body>
</html>
