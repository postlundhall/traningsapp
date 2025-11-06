package com.postlundhall.TraningsregisterSpringH2.repository;

import com.postlundhall.TraningsregisterSpringH2.model.Instruktion;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for future Instruktion-functionality
 * @author postlundhall
 * @since 1.0
 */
public interface InstruktionRepository extends JpaRepository<Instruktion, Long> {
}