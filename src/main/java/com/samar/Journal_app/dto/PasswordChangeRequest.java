package com.samar.Journal_app.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PasswordChangeRequest {

    @NotBlank
    private String oldPassword;

    @Size(min = 6, max = 18, message = "password length should be between 6 and 18 characters.")
    private String newPassword;
}
