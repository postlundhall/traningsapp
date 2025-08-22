package com.postlundhall.TraningsregisterSpringH2.service;

import com.postlundhall.TraningsregisterSpringH2.model.Traningspass;
import com.postlundhall.TraningsregisterSpringH2.repository.TraningspassRepository;
import org.springframework.stereotype.Service;

@Service
public class TraningspassService extends BaseService<Traningspass, Long> {

    public TraningspassService(TraningspassRepository repository) {
        super(repository);
    }
}
