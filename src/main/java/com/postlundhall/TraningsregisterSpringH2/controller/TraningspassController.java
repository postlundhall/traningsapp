package com.postlundhall.TraningsregisterSpringH2.controller;

import jakarta.validation.constraints.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.postlundhall.TraningsregisterSpringH2.model.Traningspass;
import com.postlundhall.TraningsregisterSpringH2.service.TraningspassService;

import java.util.List;

@RestController
@RequestMapping("/api/traningspass")
@RequiredArgsConstructor
public class TraningspassController {
    private final TraningspassService service;

    @GetMapping
    public List<Traningspass> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Traningspass> getOne(@PathVariable @Positive Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Traningspass> create(@Valid @RequestBody Traningspass traningspass) {
        Traningspass saved = service.save(traningspass);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Traningspass> update(
            @PathVariable Long id,
            @Valid @RequestBody Traningspass updated) {

        return service.findById(id)
                .map(existing -> {
                    updated.setId(id);  // se till att ID inte ändras
                    return ResponseEntity.ok(service.save(updated));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            service.deleteById(id);
            ;
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
