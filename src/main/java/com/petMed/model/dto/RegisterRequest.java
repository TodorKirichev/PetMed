package com.petMed.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {

    @Pattern(regexp = "^(?!\\s*$).{3,20}$")
    private String username;

    @NotBlank
    @Email
    private String email;

    @Pattern(regexp = "^(?!\\s*$).{3,20}$")
    private String password;

    @Pattern(regexp = "^(?!\\s*$).{3,20}$")
    private String confirmPassword;
}
