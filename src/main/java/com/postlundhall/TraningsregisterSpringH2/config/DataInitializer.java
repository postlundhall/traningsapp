package com.postlundhall.TraningsregisterSpringH2.config;

import com.postlundhall.TraningsregisterSpringH2.model.User;
import com.postlundhall.TraningsregisterSpringH2.repository.UserRepository;
import com.postlundhall.TraningsregisterSpringH2.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for initializing test data in the application.
 * Defines a {@code CommandLineRunner} bean that
 * creates a default test user during application startup if it does not already exist in the database.
 * @author postlundhall
 * @since 1.0
 */
@Configuration
public class DataInitializer {
    /**
     * Configures a CommandLineRunner to build a testuser at application startup if it does not exist in UserRepository,
     * then uses userService to save the testuser.
     * @param userService manages users and encodes passwords
     * @param userRepository contains user data
     * @return CommandLineRunner instance that executes user initialization.
     */
    @Bean
    public CommandLineRunner initData(UserService userService, UserRepository userRepository) {
        return args -> {
            if (userRepository.findByUsername("Testuser") == null) {
                User user = User.builder()
                        .username("Testuser")
                        .usernameLower("testuser")
                        .password("password123")
                        .role("USER")
                        .build();
                userService.save(user);
            }
        };
    }
}