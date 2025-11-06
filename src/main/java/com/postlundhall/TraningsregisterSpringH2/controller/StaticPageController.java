package com.postlundhall.TraningsregisterSpringH2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controller for managing static pages.
 * @author postlundhall
 * @since 1.0
 */
@Controller
public class StaticPageController {

    @GetMapping("/privacy")
    public String privacy() {
        return "/privacy/privacy";
    }

    @GetMapping("/terms")
    public String terms() {
        return "terms/terms";
    }

    @GetMapping("/search")
    public String search(@RequestParam(value = "query", required = false) String query, Model model) {
        model.addAttribute("query", query);
        return "search/search";
    }
}