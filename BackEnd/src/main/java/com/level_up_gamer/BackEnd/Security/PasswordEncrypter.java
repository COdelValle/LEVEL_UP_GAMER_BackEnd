package com.level_up_gamer.BackEnd.Security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Base64;

@Component
public class PasswordEncrypter {
    
    private final BCryptPasswordEncoder passwordEncoder;
    
    public PasswordEncrypter() {
        this.passwordEncoder = new BCryptPasswordEncoder();
    }
    
    public String encryptPassword(String password) {
        return passwordEncoder.encode(password);
    }
    
    public boolean matches(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
    
    public String generateApiKey() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[32];
        random.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
}
