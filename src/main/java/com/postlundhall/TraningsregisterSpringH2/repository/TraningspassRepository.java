package com.postlundhall.TraningsregisterSpringH2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.postlundhall.TraningsregisterSpringH2.model.Traningspass;

/**
 * Repository for future Traningspass-functionality
 * @author postlundhall
 * @since 1.0
 */
public interface TraningspassRepository extends JpaRepository<Traningspass, Long> {
}
