package com.petMed.user.service;

import com.petMed.web.dto.*;
import com.petMed.user.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.List;
import java.util.UUID;

public interface UserService {

     void registerOwner(PetOwnerRegisterRequest petOwnerRegisterRequest);

     void registerVet(VetRegisterRequest vetRegisterRequest);

     User findById(UUID userId);

     List<User> findAllVets();

     User findByUsername(String username);

     List<User> findAllUsers();

     void changeRole(String username, String newRole);

     void updateVetProfile(UUID userId, VetData vetData);

     List<User> findVets(String name, String city);

     UserDetails loadUserByUsername(String username);
}