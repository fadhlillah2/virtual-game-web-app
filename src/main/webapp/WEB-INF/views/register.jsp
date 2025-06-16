<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Virtual Game - Register</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://code.jquery.com/jquery-3.7.0.min.js"></script>
    <style>
        body {
            background-color: #f8f9fa;
        }
        .register-container {
            max-width: 500px;
            margin: 50px auto;
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
    <div class="container register-container">
        <div class="card">
            <div class="card-header">
                <h3>Virtual Game Platform</h3>
                <p class="mb-0">Create a new account</p>
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
                
                <form id="registerForm">
                    <div class="mb-3">
                        <label for="username" class="form-label">Username</label>
                        <input type="text" class="form-control" id="username" name="username" required 
                               minlength="3" maxlength="50">
                        <div class="form-text">Username must be between 3-50 characters</div>
                    </div>
                    
                    <div class="mb-3">
                        <label for="email" class="form-label">Email</label>
                        <input type="email" class="form-control" id="email" name="email" required>
                    </div>
                    
                    <div class="mb-3">
                        <label for="password" class="form-label">Password</label>
                        <input type="password" class="form-control" id="password" name="password" required 
                               minlength="8" pattern="^(?=.*[a-z])(?=.*[A-Z])(?=.*\d).*$">
                        <div class="form-text">Password must be at least 8 characters and contain uppercase, lowercase, and number</div>
                    </div>
                    
                    <div class="mb-3">
                        <label for="birthDate" class="form-label">Birth Date</label>
                        <input type="date" class="form-control" id="birthDate" name="birthDate" required>
                        <div class="form-text">You must be at least 18 years old to register</div>
                    </div>
                    
                    <div class="mb-3 form-check">
                        <input type="checkbox" class="form-check-input" id="terms" required>
                        <label class="form-check-label" for="terms">
                            I agree to the <a href="/terms" target="_blank">Terms and Conditions</a>
                        </label>
                    </div>
                    
                    <button type="submit" class="btn btn-primary w-100 mb-3">Create Account</button>
                    
                    <div class="text-center">
                        <p>Already have an account? <a href="/login">Login here</a></p>
                    </div>
                </form>
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
                const monthDiff = today.getMonth() - birthDate.getMonth();
                
                if (age < 18 || (age === 18 && monthDiff < 0)) {
                    alert('You must be at least 18 years old to register');
                    $(this).val('');
                }
            });
            
            // Form submission
            $('#registerForm').on('submit', function(e) {
                e.preventDefault();
                
                if (!$('#terms').is(':checked')) {
                    alert('You must agree to the Terms and Conditions');
                    return;
                }
                
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
                            // Show success message and redirect to login
                            $('.card-body').html(
                                '<div class="alert alert-success" role="alert">' + 
                                'Registration successful! You can now <a href="/login">login</a> to your account.' + 
                                '</div>'
                            );
                            
                            // Redirect to login after 3 seconds
                            setTimeout(function() {
                                window.location.href = '/login';
                            }, 3000);
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
                            (xhr.responseJSON ? xhr.responseJSON.message : 'Registration failed. Please try again.') + 
                            '</div>'
                        );
                    }
                });
            });
        });
    </script>
</body>
</html>
