package com.postlundhall.TraningsregisterSpringH2.service;

import com.postlundhall.TraningsregisterSpringH2.model.Ovning;
import com.postlundhall.TraningsregisterSpringH2.repository.OvningRepository;
import org.springframework.stereotype.Service;

@Service
public class OvningService extends BaseService<Ovning, Long> {

    public OvningService(OvningRepository repository) {
        super(repository);
    }
}