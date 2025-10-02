package com.rds.userservice.dto;

public record UserProfileResponse(
        Long id,
        String email,
        String name,
        String phoneMasked,
        String addressMasked
){ }