package com.rds.userservice.dto;

import com.rds.securitylib.constant.RoleEnum;
import jakarta.validation.constraints.*;

public record RegisterRequest(
        @NotBlank @Email
        String email,
        @NotBlank @Size(min = 8, max = 100)
        String password,
        @NotBlank
        String name,
        @Pattern(regexp = "^[0-9+\\- ]{6,20}$", message = "invalid phone")
        String phone,
        @NotBlank
        String address,
        @NotNull
        RoleEnum role
) { }