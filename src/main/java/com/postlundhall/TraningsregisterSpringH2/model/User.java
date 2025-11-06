package com.postlundhall.TraningsregisterSpringH2.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Locale;

/**
 * Model representing the application's User-entities.
 * Every User comprises id, name, password, role, authorities, and account status attributes.
 * @author postlundhall
 * @since 1.0
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Användarnamn måste anges")
    @Size(min = 3, max = 50, message = "Användarnamn måste vara mellan 3 och 50 tecken")
    @Column(unique = true)
    private String username;

    @Column(name = "username_lower", nullable = false, unique = true)
    private String usernameLower;

    @PrePersist
    @PreUpdate
    private void normalizeUsername() {
        if (this.username != null) {
            this.usernameLower = this.username.toLowerCase(Locale.ROOT);
        }
    }

    @NotBlank(message = "Lösenord måste anges")
    @Size(min = 6, message = "Lösenord måste vara minst 6 tecken")
    private String password;

    @Column(nullable = false)
    private String role = "USER"; // Default role

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}