package com.petMed.service;

import com.petMed.model.dto.LoginRequest;
import com.petMed.model.dto.RegisterRequest;
import com.petMed.model.entity.User;
import com.petMed.model.enums.Role;
import com.petMed.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public void register(RegisterRequest registerRequest) {
        if (!registerRequest.getPassword().equals(registerRequest.getConfirmPassword())) {
            throw new RuntimeException("Passwords do not match");
        }

        Optional<User> byUsername = findByUsername(registerRequest.getUsername());
        if (byUsername.isPresent()) {
            throw new RuntimeException("User already exists");
        }

        Optional<User> byEmail = findByEmail(registerRequest.getEmail());
        if (byEmail.isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        User user = User.builder()
                .username(registerRequest.getUsername())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(Role.PET_OWNER)
                .build();
        userRepository.save(user);
    }

    private Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User login(LoginRequest loginRequest) {
        Optional<User> byUsername = findByUsername(loginRequest.getUsername());

        if (byUsername.isPresent() && passwordEncoder.matches(loginRequest.getPassword(), byUsername.get().getPassword())) {
            return byUsername.get();
        }

        throw new RuntimeException("Invalid username or password");
    }
}