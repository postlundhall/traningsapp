package com.postlundhall.TraningsregisterSpringH2.repository;

import com.postlundhall.TraningsregisterSpringH2.model.Ovning;
import com.postlundhall.TraningsregisterSpringH2.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

/**
 * Repository interface for {@link User} entities. The interface extends {@link JpaRepository} to provide standard
 * CRUD operations, and adds a custom query method for finding users by username.
 * @author postlundhall
 * @since 1.0
 */
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Finds a user by their unique username.
     * The method is used by Spring Security's {@code UserDetailsService} to load user details during authentication.
     * The query is automatically implemented by Spring Data JPA based on method name: {@code findByUsername}.
     *
     * @param username the username to search for
     * @return the {@link User} matching the username, or {@code null} if not found
     */
    User findByUsername(String username);
    Optional<User> findByUsernameLower(String usernameLower);
}