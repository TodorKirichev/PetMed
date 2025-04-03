package com.petMed.user.repository;

import com.petMed.user.model.User;
import com.petMed.user.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByUsername(String username);

    List<User> findAllByRole(Role role);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByPhone(String phoneNumber);

    boolean existsByPhoneAndIdNot(String phone, UUID userId);
}
