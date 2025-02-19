package com.petMed.service;

import com.petMed.model.dto.LoginRequest;
import com.petMed.model.dto.RegisterRequest;
import com.petMed.model.entity.User;
import com.petMed.model.enums.Role;
import com.petMed.repository.UserRepository;
import com.petMed.security.AuthenticationDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

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
                .isActive(true)
                .build();
        userRepository.save(user);
    }

    private Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));

        return new AuthenticationDetails(user.getId(), username, user.getPassword(), user.getRole(), user.isActive());
    }
}