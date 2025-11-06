package com.postlundhall.TraningsregisterSpringH2.service;

import com.postlundhall.TraningsregisterSpringH2.model.User;
import com.postlundhall.TraningsregisterSpringH2.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Locale;

/**
 * Service for managing {@link User} entities and Spring Security authentication.
 * Implements {@link UserDetailsService} to load users by username during login.
 * Provides secure user creation with input validation and password encoding.
 * @author postlundhall
 * @since 1.0
 */
@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Constructs a new {@code UserService} with required dependencies,
     * using {@link UserRepository} and {@link PasswordEncoder}.
     *
     * @param userRepository   the repository for {@link User} persistence
     * @param passwordEncoder  the encoder for hashing passwords
     */
    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Loads a user by their username for Spring Security authentication.
     * The returned {@link User} must implement {@link UserDetails}.
     *
     * @param username the username to search for
     * @return the {@link UserDetails} object representing the user
     * @throws UsernameNotFoundException if user is not by found by the provided username
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsernameLower(username.toLowerCase(Locale.ROOT))
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Användare med namn " + username + " hittades inte"
                ));
    }

    /**
     * Saves or updates a {@link User} with validation and BCrypt password encoding.
     * Validation rules:
     * <ul>
     *   <li>Username: not blank, 3–50 alphanumeric chars</li>
     *   <li>Password: not blank, minimum 6 characters</li>
     *   <li>Role: not blank</li>
     * </ul>
     * {@link PasswordEncoder} is used to automatically encode the password.
     *
     * @param user the user entity to save
     * @return the persisted user with encoded password
     * @throws IllegalArgumentException with reason if validation fails
     */
    public User save(User user) {
        // Validate username
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("Användarnamnet får inte vara tomt eller enbart blanksteg");
        }
        if (user.getUsername().length() < 3 || user.getUsername().length() > 50) {
            throw new IllegalArgumentException("Användarnamnet måste vara mellan 3 och 50 tecken");
        }
        if (!user.getUsername().matches("^[a-zA-Z0-9]+$")) {
            throw new IllegalArgumentException("Användarnamnet får endast innehålla bokstäver och siffror");
        }

        String normalizedUsername = user.getUsername().toLowerCase(Locale.ROOT);
        // Check if username (case-insensitive) already exists
        if (userRepository.findByUsernameLower(normalizedUsername).isPresent()) {
            throw new IllegalArgumentException(
                    "Användarnamnet " + user.getUsername() + " är redan taget"
            );
        }

        // Validate password
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("Lösenordet får inte vara tomt");
        }
        if (user.getPassword().length() < 6) {
            throw new IllegalArgumentException("Lösenordet måste vara minst 6 tecken långt");
        }

        // Validate role
        if (user.getRole() == null || user.getRole().trim().isEmpty()) {
            throw new IllegalArgumentException("Rollen får inte vara tom");
        }

        // Encode password and save
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }
}