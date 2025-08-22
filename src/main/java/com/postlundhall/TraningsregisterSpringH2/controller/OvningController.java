package com.postlundhall.TraningsregisterSpringH2.controller;

import com.postlundhall.TraningsregisterSpringH2.model.Ovning;
import com.postlundhall.TraningsregisterSpringH2.service.OvningService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/ovningar")
public class OvningController extends BaseController<Ovning, Long> {

    public OvningController(OvningService service) {
        super(service, "ovning", "ovningar", Ovning.class);
    }
}