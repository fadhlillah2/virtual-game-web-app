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
        .invalid-feedback {
            display: none;
        }
    </style>
</head>
<body>
    <div class="container register-container">
        <div class="card">
            <div class="card-header">
                <h3>Create Your Account</h3>
                <p class="mb-0">Join the Virtual Game Platform</p>
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
                        <input type="text" class="form-control" id="username" name="username" required minlength="3" maxlength="50">
                        <div class="invalid-feedback" id="username-feedback">
                            Username must be between 3 and 50 characters.
                        </div>
                    </div>
                    
                    <div class="mb-3">
                        <label for="email" class="form-label">Email</label>
                        <input type="email" class="form-control" id="email" name="email" required>
                        <div class="invalid-feedback" id="email-feedback">
                            Please enter a valid email address.
                        </div>
                    </div>
                    
                    <div class="mb-3">
                        <label for="password" class="form-label">Password</label>
                        <input type="password" class="form-control" id="password" name="password" required minlength="8">
                        <div class="form-text">
                            Password must be at least 8 characters and include uppercase, lowercase, and numbers.
                        </div>
                        <div class="invalid-feedback" id="password-feedback">
                            Password must be at least 8 characters and include uppercase, lowercase, and numbers.
                        </div>
                    </div>
                    
                    <div class="mb-3">
                        <label for="confirmPassword" class="form-label">Confirm Password</label>
                        <input type="password" class="form-control" id="confirmPassword" name="confirmPassword" required>
                        <div class="invalid-feedback" id="confirmPassword-feedback">
                            Passwords do not match.
                        </div>
                    </div>
                    
                    <div class="mb-3">
                        <label for="birthDate" class="form-label">Birth Date</label>
                        <input type="date" class="form-control" id="birthDate" name="birthDate" required>
                        <div class="invalid-feedback" id="birthDate-feedback">
                            You must be at least 18 years old to register.
                        </div>
                    </div>
                    
                    <div class="mb-3 form-check">
                        <input type="checkbox" class="form-check-input" id="termsCheck" required>
                        <label class="form-check-label" for="termsCheck">
                            I agree to the <a href="#" data-bs-toggle="modal" data-bs-target="#termsModal">Terms and Conditions</a>
                        </label>
                        <div class="invalid-feedback" id="terms-feedback">
                            You must agree to the terms and conditions.
                        </div>
                    </div>
                    
                    <button type="submit" class="btn btn-primary w-100 mb-3">Create Account</button>
                    
                    <div class="text-center">
                        <p>Already have an account? <a href="/login">Sign in here</a></p>
                    </div>
                </form>
            </div>
        </div>
    </div>
    
    <!-- Terms and Conditions Modal -->
    <div class="modal fade" id="termsModal" tabindex="-1" aria-labelledby="termsModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-dialog-scrollable">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="termsModalLabel">Terms and Conditions</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <h6>1. Introduction</h6>
                    <p>Welcome to the Virtual Game Platform. These Terms and Conditions govern your use of our virtual casino game platform, which offers entertainment using virtual chips with no real money value.</p>
                    
                    <h6>2. Age Restriction</h6>
                    <p>You must be at least 18 years old to use our platform. By registering, you confirm that you meet this age requirement.</p>
                    
                    <h6>3. Virtual Chips</h6>
                    <p>All chips on the platform are virtual and have no real-world monetary value. They cannot be exchanged for real money or any other tangible goods or services.</p>
                    
                    <h6>4. Account Responsibility</h6>
                    <p>You are responsible for maintaining the confidentiality of your account information and for all activities that occur under your account.</p>
                    
                    <h6>5. Prohibited Conduct</h6>
                    <p>You agree not to:</p>
                    <ul>
                        <li>Create multiple accounts</li>
                        <li>Use automated scripts or bots</li>
                        <li>Attempt to manipulate games or exploit bugs</li>
                        <li>Harass other users or use offensive language</li>
                        <li>Attempt to sell or transfer virtual chips outside the platform</li>
                    </ul>
                    
                    <h6>6. Termination</h6>
                    <p>We reserve the right to terminate or suspend your account at our sole discretion, without notice, for conduct that we believe violates these Terms or is harmful to other users, us, or third parties, or for any other reason.</p>
                    
                    <h6>7. Changes to Terms</h6>
                    <p>We may modify these Terms at any time. Your continued use of the platform following any changes constitutes your acceptance of the revised Terms.</p>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                    <button type="button" class="btn btn-primary" data-bs-dismiss="modal" id="acceptTermsBtn">Accept</button>
                </div>
            </div>
        </div>
    </div>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        $(document).ready(function() {
            // Form validation
            $('#registerForm').on('submit', function(e) {
                e.preventDefault();
                
                if (validateForm()) {
                    submitRegistration();
                }
            });
            
            // Password validation
            $('#password').on('input', function() {
                validatePassword();
            });
            
            // Confirm password validation
            $('#confirmPassword').on('input', function() {
                validateConfirmPassword();
            });
            
            // Birth date validation
            $('#birthDate').on('change', function() {
                validateAge();
            });
            
            // Accept terms button
            $('#acceptTermsBtn').on('click', function() {
                $('#termsCheck').prop('checked', true);
                $('#terms-feedback').hide();
            });
        });
        
        function validateForm() {
            let isValid = true;
            
            // Validate username
            if (!$('#username').val() || $('#username').val().length < 3 || $('#username').val().length > 50) {
                $('#username').addClass('is-invalid');
                $('#username-feedback').show();
                isValid = false;
            } else {
                $('#username').removeClass('is-invalid');
                $('#username-feedback').hide();
            }
            
            // Validate email
            const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
            if (!$('#email').val() || !emailRegex.test($('#email').val())) {
                $('#email').addClass('is-invalid');
                $('#email-feedback').show();
                isValid = false;
            } else {
                $('#email').removeClass('is-invalid');
                $('#email-feedback').hide();
            }
            
            // Validate password
            if (!validatePassword()) {
                isValid = false;
            }
            
            // Validate confirm password
            if (!validateConfirmPassword()) {
                isValid = false;
            }
            
            // Validate birth date
            if (!validateAge()) {
                isValid = false;
            }
            
            // Validate terms
            if (!$('#termsCheck').is(':checked')) {
                $('#termsCheck').addClass('is-invalid');
                $('#terms-feedback').show();
                isValid = false;
            } else {
                $('#termsCheck').removeClass('is-invalid');
                $('#terms-feedback').hide();
            }
            
            return isValid;
        }
        
        function validatePassword() {
            const password = $('#password').val();
            const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d).{8,}$/;
            
            if (!password || !passwordRegex.test(password)) {
                $('#password').addClass('is-invalid');
                $('#password-feedback').show();
                return false;
            } else {
                $('#password').removeClass('is-invalid');
                $('#password-feedback').hide();
                return true;
            }
        }
        
        function validateConfirmPassword() {
            const password = $('#password').val();
            const confirmPassword = $('#confirmPassword').val();
            
            if (!confirmPassword || password !== confirmPassword) {
                $('#confirmPassword').addClass('is-invalid');
                $('#confirmPassword-feedback').show();
                return false;
            } else {
                $('#confirmPassword').removeClass('is-invalid');
                $('#confirmPassword-feedback').hide();
                return true;
            }
        }
        
        function validateAge() {
            const birthDate = new Date($('#birthDate').val());
            const today = new Date();
            const minAgeDate = new Date();
            minAgeDate.setFullYear(today.getFullYear() - 18);
            
            if (!$('#birthDate').val() || birthDate > minAgeDate) {
                $('#birthDate').addClass('is-invalid');
                $('#birthDate-feedback').show();
                return false;
            } else {
                $('#birthDate').removeClass('is-invalid');
                $('#birthDate-feedback').hide();
                return true;
            }
        }
        
        function submitRegistration() {
            const registrationData = {
                username: $('#username').val(),
                email: $('#email').val(),
                password: $('#password').val(),
                birthDate: $('#birthDate').val()
            };
            
            $.ajax({
                url: '/api/auth/register',
                type: 'POST',
                contentType: 'application/json',
                data: JSON.stringify(registrationData),
                success: function(response) {
                    if (response.success) {
                        // Show success message
                        $('.card-body').prepend(
                            '<div class="alert alert-success" role="alert">' + 
                            'Registration successful! Redirecting to login page...' + 
                            '</div>'
                        );
                        
                        // Store token if provided
                        if (response.data && response.data.accessToken) {
                            localStorage.setItem('token', response.data.accessToken);
                            localStorage.setItem('refreshToken', response.data.refreshToken);
                        }
                        
                        // Redirect to login page after 2 seconds
                        setTimeout(function() {
                            window.location.href = '/login';
                        }, 2000);
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
        }
    </script>
</body>
</html>
