package com.postlundhall.TraningsregisterSpringH2.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.postlundhall.TraningsregisterSpringH2.model.Traningspass;
import com.postlundhall.TraningsregisterSpringH2.repository.TraningspassRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional

public class TraningspassService {
    private final TraningspassRepository repository;

    public List<Traningspass> findAll() {
        return repository.findAll();}

    public Optional<Traningspass> findById(Long id) {
        return repository.findById(id);
    }

    public Traningspass save(Traningspass traningspass) {
        return repository.save(traningspass);
    }

    public void deleteById(Long id) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("Träningspass med id " + id + "finns inte registrerat.");
        }
        repository.deleteById(id);
    }
}
