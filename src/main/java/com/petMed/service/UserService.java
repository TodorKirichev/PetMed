package com.petMed.service;

import com.petMed.model.dto.PetOwnerRegisterRequest;
import com.petMed.model.dto.RegisterRequest;
import com.petMed.model.dto.VetRegisterRequest;
import com.petMed.model.entity.Clinic;
import com.petMed.model.entity.User;
import com.petMed.model.enums.Role;
import com.petMed.repository.UserRepository;
import com.petMed.security.CurrentUser;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ClinicService clinicService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, ClinicService clinicService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.clinicService = clinicService;
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public void registerOwner(PetOwnerRegisterRequest petOwnerRegisterRequest) {
        validate(petOwnerRegisterRequest);

        User user = createUser(petOwnerRegisterRequest);
        user.setRole(Role.PET_OWNER);
        userRepository.save(user);
    }

    public void registerVet(VetRegisterRequest vetRegisterRequest) {
        validate(vetRegisterRequest);

        User user = createUser(vetRegisterRequest);
        user.setRole(Role.VET);

        Clinic clinic = clinicService.createClinic(vetRegisterRequest);
        user.setClinic(clinic);

        userRepository.save(user);
    }

    private User createUser(RegisterRequest registerRequest) {
        return User.builder()
                .fistName(registerRequest.getFirstName())
                .lastName(registerRequest.getLastName())
                .username(registerRequest.getUsername())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .phone(registerRequest.getPhone())
                .isActive(true)
                .build();
    }

    private void validate(RegisterRequest registerRequest) {
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
    }

    private Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User findById(UUID userId) {
        return userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));

        return new CurrentUser(user.getId(), user.getUsername(), user.getPassword(), user.getRole(), user.isActive());
    }

    public List<User> findAllVets() {
        return userRepository.findAllByRole(Role.VET);
    }

    public User findVetByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("Vet not found"));
    }
}