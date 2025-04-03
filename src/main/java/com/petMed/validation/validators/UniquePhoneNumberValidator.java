package com.petMed.validation.validators;

import com.petMed.user.repository.UserRepository;
import com.petMed.validation.UniquePhoneNumber;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UniquePhoneNumberValidator implements ConstraintValidator<UniquePhoneNumber, String> {

    private final UserRepository userRepository;

    public UniquePhoneNumberValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean isValid(String phoneNumber, ConstraintValidatorContext constraintValidatorContext) {
        return !userRepository.existsByPhone(phoneNumber);
    }
}
