package com.postlundhall.TraningsregisterSpringH2.controller;

import com.postlundhall.TraningsregisterSpringH2.model.Instruktion;
import com.postlundhall.TraningsregisterSpringH2.service.InstruktionService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/instruktioner")
public class InstruktionController extends BaseController<Instruktion, Long> {

    public InstruktionController(InstruktionService service) {
        super(service, "ovning", "ovningar", Instruktion.class);
    }
}