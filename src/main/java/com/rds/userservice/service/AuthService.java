package com.rds.userservice.service;

import com.rds.securitylib.common.converter.BytesConverter;
import com.rds.securitylib.common.encrypt.AesGcmEncryptor;
import com.rds.securitylib.jwt.JwtService;
import com.rds.userservice.domain.entity.User;
import com.rds.userservice.domain.repository.UserRepository;
import com.rds.userservice.dto.LoginRequest;
import com.rds.userservice.dto.RegisterRequest;
import com.rds.userservice.exception.DataNotFoundException;
import com.rds.userservice.exception.InvalidCredentialException;
import com.rds.userservice.exception.UserAlreadyExistException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AesGcmEncryptor encryptor;

    public void register(RegisterRequest request){
        try {
            // check if user already exists
            userRepository.findByEmail(request.email())
                    .ifPresent(user -> {
                        throw new UserAlreadyExistException("User already exists");
                    });

            // encrypt and hash fields
            var phoneEncrypted = encryptor.encrypt(BytesConverter.stringToBytes(request.phone()));
            var addressEncrypted = encryptor.encrypt(BytesConverter.stringToBytes(request.address()));
            var passwordHashed = passwordEncoder.encode(request.password());

            // create user
            var user = User.builder()
                    .name(request.name())
                    .email(request.email())
                    .password(passwordHashed)
                    .phone(phoneEncrypted)
                    .address(addressEncrypted)
                    .role(request.role())
                    .build();
            userRepository.save(user);
        } catch (UserAlreadyExistException e){
            throw e;
        } catch (Exception e) {
            log.error("Failed to register user", e);
            throw new RuntimeException("Failed to register user");
        }
    }

    public String login(LoginRequest request){
        // check if user exists
        var user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new DataNotFoundException("User not found"));

        // check password
        if(!passwordEncoder.matches(request.password(), user.getPassword())){
            throw new InvalidCredentialException("Invalid credentials");
        }

        // generate JWT
        return jwtService.generate(user.getId().toString(), Map.of("roles", List.of(user.getRole())));
    }

    public String getPhone(Long id){
        var user = userRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("User not found"));
        return new String(encryptor.decrypt(user.getPhone()), StandardCharsets.UTF_8);
    }
}
