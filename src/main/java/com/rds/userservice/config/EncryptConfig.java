package com.rds.userservice.config;

import com.rds.securitylib.common.encrypt.AesGcmEncryptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;

import java.util.Base64;

@Slf4j
@Configuration
public class EncryptConfig {

    @Bean
    public AesGcmEncryptor aeadEncryptor(@Value("${pii.aes.key-base64}") String base64Key){
        byte[] key = Base64.getDecoder().decode(base64Key);
        return new AesGcmEncryptor(key);
    }
}

