package com.petMed.validation.validators;

import com.petMed.validation.ValidPhoto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

public class PhotoValidator implements ConstraintValidator<ValidPhoto, MultipartFile> {

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext constraintValidatorContext) {
        return file != null && !file.isEmpty();
    }
}
