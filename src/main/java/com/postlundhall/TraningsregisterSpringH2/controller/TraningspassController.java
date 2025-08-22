package com.postlundhall.TraningsregisterSpringH2.controller;

import com.postlundhall.TraningsregisterSpringH2.model.Traningspass;
import com.postlundhall.TraningsregisterSpringH2.service.TraningspassService;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/traningspass")
public class TraningspassController extends BaseController<Traningspass, Long> {

    public TraningspassController(TraningspassService service) {
        super(service, "traningspass", "traningspass", Traningspass.class);
    }
}