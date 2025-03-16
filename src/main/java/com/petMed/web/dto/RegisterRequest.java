package com.petMed.web.dto;

import com.petMed.validation.PasswordMatch;
import com.petMed.validation.UniqueEmail;
import com.petMed.validation.UniquePhoneNumber;
import com.petMed.validation.UniqueUsername;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@PasswordMatch
public class RegisterRequest {

    @NotBlank(message = "First name cannot be empty")
    private String firstName;

    @NotBlank(message = "Last name cannot be empty")
    private String lastName;

    @Pattern(regexp = "^(?!\\s*$).{3,20}$", message = "Username must be between 3 and 20 symbols")
    @UniqueUsername
    private String username;

    @NotBlank(message = "Email cannot be empty")
    @UniqueEmail
    @Email(message = "Please enter a valid email")
    private String email;

    @Pattern(regexp = "^(?!\\s*$).{3,20}$", message = "Password must be between 3 and 20 symbols")
    private String password;

    private String confirmPassword;

    @NotBlank(message = "Phone number cannot be empty")
    @UniquePhoneNumber
    private String phone;
}
