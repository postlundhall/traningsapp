package com.postlundhall.TraningsregisterSpringH2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.postlundhall.TraningsregisterSpringH2.model.Traningspass;
import java.util.Optional;

public interface TraningspassRepository extends JpaRepository<Traningspass, Long> {
    Optional<Traningspass> findByPassnamn(String passnamn);
}
