package co.id.virtual.game.web.app.service.impl;

import co.id.virtual.game.web.app.model.User;
import co.id.virtual.game.web.app.repository.UserRepository;
import co.id.virtual.game.web.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

/**
 * Implementation of the UserService interface.
 */
@Service
public class UserServiceImpl implements UserService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    
    @Override
    @Transactional
    public User registerUser(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("Username already taken: " + user.getUsername());
        }
        
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email already taken: " + user.getEmail());
        }
        
        // Encode the password
        user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
        
        // Set default values
        user.setChipsBalance(1000L);
        user.setTotalChipsWon(0L);
        user.setLevel(1);
        user.setExperiencePoints(0);
        user.setIsPremium(false);
        user.setIsActive(true);
        user.setLastLogin(LocalDateTime.now());
        
        return userRepository.save(user);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<User> findById(UUID id) {
        return userRepository.findById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    @Override
    @Transactional
    public User updateUser(User user) {
        if (!userRepository.existsById(user.getId())) {
            throw new IllegalArgumentException("User not found with ID: " + user.getId());
        }
        
        return userRepository.save(user);
    }
    
    @Override
    @Transactional
    public User updateChipsBalance(UUID userId, Long chipsBalance) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
        
        user.setChipsBalance(chipsBalance);
        return userRepository.save(user);
    }
    
    @Override
    @Transactional
    public User updateLastLogin(UUID userId, LocalDateTime lastLogin) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
        
        user.setLastLogin(lastLogin);
        return userRepository.save(user);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<User> findTopUsersByChipsBalance(Pageable pageable) {
        return userRepository.findAll(pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
