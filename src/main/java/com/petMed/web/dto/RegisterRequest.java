package com.petMed.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @Pattern(regexp = "^(?!\\s*$).{3,20}$", message = "Username must be between 3 and 20 symbols")
    private String username;

    @NotBlank
    @Email(message = "Please enter a valid email")
    private String email;

    @Pattern(regexp = "^(?!\\s*$).{3,20}$", message = "Password must be between 3 and 20 symbols")
    private String password;

    @Pattern(regexp = "^(?!\\s*$).{3,20}$", message = "Confirm Password must be between 3 and 20 symbols")
    private String confirmPassword;

    @NotBlank
    private String phone;
}
