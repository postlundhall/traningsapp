package com.postlundhall.TraningsregisterSpringH2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller for directing root URL to home page template (index).
 * @author postlundhall
 * @since 1.0
 */
@Controller
public class HomeController {

    /**
     * Handles HTTP GET requests to the root URL ("/") and returns the name of the Thymeleaf template
     * to render the home page.
     *
     * @return the name of the home page template ("index").
     */
    @GetMapping("/")
    public String home() {
        return "index";  // points to src/main/resources/templates/index.html
    }
}