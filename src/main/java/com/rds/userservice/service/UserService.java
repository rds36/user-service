package com.rds.userservice.service;

import com.rds.securitylib.common.converter.BytesConverter;
import com.rds.securitylib.common.encrypt.AesGcmEncryptor;
import com.rds.securitylib.common.masking.MaskGenerator;
import com.rds.userservice.domain.entity.User;
import com.rds.userservice.domain.repository.UserRepository;
import com.rds.userservice.dto.UserProfileResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final AesGcmEncryptor encryptor;


    public UserProfileResponse me(Authentication auth){
        Long uid = Long.valueOf(auth.getName()); // subject diisi userId saat issue token
        User user = userRepository.findById(uid).orElseThrow();

        // mask phone and address
        String phone = MaskGenerator.mask(BytesConverter.bytesToString(encryptor.decrypt(user.getPhone())));
        String address = MaskGenerator.maskAddress(BytesConverter.bytesToString(encryptor.decrypt(user.getAddress())));
        return new UserProfileResponse(user.getId(), user.getEmail(), user.getName(), phone, address);
    }

}