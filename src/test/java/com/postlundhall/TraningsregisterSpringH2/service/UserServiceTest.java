package com.postlundhall.TraningsregisterSpringH2.service;

import com.postlundhall.TraningsregisterSpringH2.model.User;
import com.postlundhall.TraningsregisterSpringH2.repository.UserRepository;
import jakarta.persistence.OptimisticLockException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .username("TestUser")
                .usernameLower("testuser")
                .password("encodedPassword")
                .role("USER")
                .build();
    }

    @Test
    void shouldLoadUserByUsernameSuccessfully() {
        // Arrange
        when(userRepository.findByUsernameLower("testuser")).thenReturn(Optional.of(user));

        // Act
        UserDetails result = userService.loadUserByUsername("TesTUsEr");

        // Assert
        assertNotNull(result);
        assertEquals("TestUser", result.getUsername());
        assertEquals("encodedPassword", result.getPassword());
        assertTrue(result.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_USER")));
        verify(userRepository).findByUsernameLower("testuser");
    }

    @Test
    void shouldThrowUsernameNotFoundExceptionWhenUserNotFound() {
        // Arrange
        when(userRepository.findByUsernameLower("unknown")).thenReturn(Optional.empty());

        // Act & Assert
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class,
                () -> userService.loadUserByUsername("UnKnOwN"));

        assertEquals("Användare med namn UnKnOwN hittades inte", exception.getMessage());
        verify(userRepository).findByUsernameLower("unknown");
    }

    @Test
    void shouldSaveUserSuccessfully() {
        // Arrange
        when(userRepository.findByUsernameLower("newuser")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("plainPassword")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User u = invocation.getArgument(0);
            u.setId(2L);
            u.setUsernameLower("newuser");
            return u;
        });

        // Act
        User newUser = User.builder()
                .username("NewUser")
                .password("plainPassword")
                .role("USER")
                .build();

        User savedUser = userService.save(newUser);

        // Assert
        assertNotNull(savedUser);
        assertEquals("NewUser", savedUser.getUsername());
        assertEquals("newuser", savedUser.getUsernameLower());
        assertEquals("encodedPassword", savedUser.getPassword());
        assertEquals("USER", savedUser.getRole());
        verify(passwordEncoder).encode("plainPassword");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void shouldThrowIllegalArgumentExceptionForBlankUsername() {
        User invalidUser = User.builder()
                .username("   ")
                .password("plainPassword")
                .role("USER")
                .build();

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> userService.save(invalidUser));

        assertEquals("Användarnamnet får inte vara tomt eller enbart blanksteg", ex.getMessage());
        verify(userRepository, never()).save(any());
        verify(passwordEncoder, never()).encode(anyString());
    }

    @Test
    void shouldThrowIllegalArgumentExceptionForInvalidUsername() {
        User invalidUser = User.builder()
                .username("bad@user!")
                .password("plainPassword")
                .role("USER")
                .build();

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> userService.save(invalidUser));

        assertEquals("Användarnamnet får endast innehålla bokstäver och siffror", ex.getMessage());
        verify(userRepository, never()).save(any());
        verify(passwordEncoder, never()).encode(anyString());
    }

    @Test
    void shouldThrowIllegalArgumentExceptionForShortPassword() {
        User invalidUser = User.builder()
                .username("validuser")
                .password("short")
                .role("USER")
                .build();

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> userService.save(invalidUser));

        assertEquals("Lösenordet måste vara minst 6 tecken långt", ex.getMessage());
        verify(userRepository, never()).save(any());
        verify(passwordEncoder, never()).encode(anyString());
    }

    @Test
    void shouldThrowIllegalArgumentExceptionForDuplicateUsernameCaseInsensitive() {
        // Arrange
        when(userRepository.findByUsernameLower("existing")).thenReturn(Optional.of(user));

        User duplicateUser = User.builder()
                .username("ExIsTiNg")
                .password("validPassword123")
                .role("USER")
                .build();

        // Act & Assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> userService.save(duplicateUser));

        assertEquals("Användarnamnet ExIsTiNg är redan taget", ex.getMessage());
        verify(userRepository).findByUsernameLower("existing");
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void shouldThrowOptimisticLockExceptionOnSaveConflict() {
        // Arrange
        when(userRepository.findByUsernameLower("testuser")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("plainPassword")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class)))
                .thenThrow(new OptimisticLockException("Concurrent modification"));

        User newUser = User.builder()
                .username("testuser")
                .password("plainPassword")
                .role("USER")
                .build();

        // Act & Assert
        assertThrows(OptimisticLockException.class, () -> userService.save(newUser));
        verify(passwordEncoder).encode("plainPassword");
        verify(userRepository).save(any(User.class));
    }
}