package com.postlundhall.TraningsregisterSpringH2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Configuration class for encoding passwords using hash encryption.
 * @author postlundhall
 * @since 1.0
 */
@Configuration
public class PasswordEncoderConfig {
    /**
     * Configures a BCryptPasswordEncoder to hash encrypt user passwords.
     * @return BCryptPasswordEncoder instance for encrypting user passwords.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}