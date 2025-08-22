package com.postlundhall.TraningsregisterSpringH2.service;

import com.postlundhall.TraningsregisterSpringH2.model.Instruktion;
import com.postlundhall.TraningsregisterSpringH2.repository.InstruktionRepository;
import org.springframework.stereotype.Service;

@Service
public class InstruktionService extends BaseService<Instruktion, Long> {

    public InstruktionService(InstruktionRepository repository) {
        super(repository);
    }
}