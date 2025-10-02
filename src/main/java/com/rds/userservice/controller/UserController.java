package com.rds.userservice.controller;

import com.rds.userservice.dto.LoginRequest;
import com.rds.userservice.dto.RegisterRequest;
import com.rds.userservice.service.AuthService;
import com.rds.userservice.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/user")
@AllArgsConstructor
public class UserController {

    private final AuthService authService;
    private final UserService userService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<Object> getUser(@PathVariable Long id) {
        var phone = authService.getPhone(id);
        return new ResponseEntity(phone, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Object> me() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        var userProfileResponse = userService.me(auth);
        return new ResponseEntity(userProfileResponse, HttpStatus.OK);
    }
}
