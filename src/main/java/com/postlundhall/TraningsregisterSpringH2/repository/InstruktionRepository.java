package com.postlundhall.TraningsregisterSpringH2.repository;

import com.postlundhall.TraningsregisterSpringH2.model.Instruktion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InstruktionRepository extends JpaRepository<Instruktion, Long> {
}