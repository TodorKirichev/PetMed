package com.petMed.user.service;

import com.petMed.clinic.service.ClinicService;
import com.petMed.event.UserRegisterEvent;
import com.petMed.web.dto.*;
import com.petMed.clinic.model.Clinic;
import com.petMed.user.model.User;
import com.petMed.user.model.Role;
import com.petMed.user.repository.UserRepository;
import com.petMed.security.CurrentUser;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    private final ApplicationEventPublisher eventPublisher;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ClinicService clinicService;

    public UserService(ApplicationEventPublisher eventPublisher, UserRepository userRepository, PasswordEncoder passwordEncoder, ClinicService clinicService) {
        this.eventPublisher = eventPublisher;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.clinicService = clinicService;
    }

    public Optional<User> findByUsernameOptional(String username) {
        return userRepository.findByUsername(username);
    }

    public void registerOwner(PetOwnerRegisterRequest petOwnerRegisterRequest) {
        validate(petOwnerRegisterRequest);

        User user = createUser(petOwnerRegisterRequest);
        user.setRole(Role.PET_OWNER);
        userRepository.save(user);

        eventPublisher.publishEvent(createUserRegisterEvent(user));
    }

    public void registerVet(VetRegisterRequest vetRegisterRequest, String imageUrl) {
        validate(vetRegisterRequest);

        User user = createUser(vetRegisterRequest);
        user.setImageUrl(imageUrl);
        user.setRole(Role.VET);

        Clinic clinic = createClinic(vetRegisterRequest);
        user.setClinic(clinic);

        userRepository.save(user);

        eventPublisher.publishEvent(createUserRegisterEvent(user));
    }

    private static UserRegisterEvent createUserRegisterEvent(User user) {
        return new UserRegisterEvent(user, user.getFirstName(), user.getLastName(), user.getEmail());
    }

    private Clinic createClinic(VetRegisterRequest vetRegisterRequest) {
        return clinicService.createClinic(
                vetRegisterRequest.getClinicName(),
                vetRegisterRequest.getCity(),
                vetRegisterRequest.getAddress(),
                vetRegisterRequest.getSite()
        );
    }

    private User createUser(RegisterRequest registerRequest) {
        return User.builder()
                .firstName(registerRequest.getFirstName())
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

        Optional<User> byUsername = findByUsernameOptional(registerRequest.getUsername());
        if (byUsername.isPresent()) {
            throw new RuntimeException("Username already exist");
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

    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public void changeRole(String username, String newRole) {
        User byUsername = findByUsername(username);
        byUsername.setRole(Role.valueOf(newRole));
        userRepository.save(byUsername);
    }

    public void updateVetProfile(UUID userId, VetData vetData) {
        User vet = findById(userId);
        Clinic clinic = clinicService.createClinic(
                vetData.getClinicName(),
                vetData.getCity(),
                vetData.getAddress(),
                vetData.getSite()
        );

        vet.setId(vet.getId());
        vet.setFirstName(vetData.getFirstName());
        vet.setLastName(vetData.getLastName());
        vet.setPhone(vetData.getPhone());
        vet.setClinic(clinic);

        userRepository.save(vet);
    }

    public List<User> searchVets(String name, String city) {
        List<User> allVets = userRepository.findAll();

        return allVets.stream()
                .filter(user -> user.getRole() == Role.VET)
                .filter(user -> user.getClinic() != null)
                .filter(user -> (name == null || user.getFirstName().toLowerCase().contains(name.toLowerCase().trim()) || user.getLastName().toLowerCase().contains(name.toLowerCase())))
                .filter(user -> (city == null || user.getClinic().getCity().getCityName().toLowerCase().contains(city.toLowerCase().trim())))
                .collect(Collectors.toList());
    }
}