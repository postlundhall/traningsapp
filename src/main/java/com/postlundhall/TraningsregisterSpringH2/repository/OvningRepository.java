package com.postlundhall.TraningsregisterSpringH2.repository;

import com.postlundhall.TraningsregisterSpringH2.model.Ovning;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OvningRepository extends JpaRepository<Ovning, Long> {
}