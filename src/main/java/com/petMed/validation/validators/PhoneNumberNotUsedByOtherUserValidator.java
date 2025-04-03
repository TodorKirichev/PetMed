package com.petMed.validation.validators;

import com.petMed.user.model.User;
import com.petMed.user.repository.UserRepository;
import com.petMed.validation.PhoneNumberNotUsedByOtherUser;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public class PhoneNumberNotUsedByOtherUserValidator implements ConstraintValidator<PhoneNumberNotUsedByOtherUser, String> {

    private final UserRepository userRepository;

    public PhoneNumberNotUsedByOtherUserValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean isValid(String phone, ConstraintValidatorContext constraintValidatorContext) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        Object principal = authentication.getPrincipal();
        if (!(principal instanceof UserDetails)) {
            return false;
        }

        String username = ((UserDetails) principal).getUsername();
        Optional<User> userOpt = userRepository.findByUsername(username);

        if (userOpt.isEmpty()) {
            return false;
        }

        User user = userOpt.get();
        boolean areSame = user.getPhone().equals(phone);
        boolean isPhoneUsedByAnotherUser = userRepository.existsByPhoneAndIdNot(phone, user.getId());

        return (areSame && !isPhoneUsedByAnotherUser) || (!areSame && !isPhoneUsedByAnotherUser);
    }
}
