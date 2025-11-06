package com.postlundhall.TraningsregisterSpringH2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller for directing login to login page template.
 * @author postlundhall
 * @since 1.0
 */
@Controller
public class LoginController {

    /**
     * Handles HTTP GET requests to the login page ("/login") and returns the name of the Thymeleaf template
     * to render the login page.
     *
     * @return the name of the login page template ("login").
     */

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}