package com.postlundhall.TraningsregisterSpringH2.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import org.thymeleaf.exceptions.TemplateInputException;
import org.thymeleaf.exceptions.TemplateProcessingException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(new TestController())
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void shouldHandleNoHandlerFoundException() throws Exception {
        mockMvc.perform(get("/not-found"))
                .andExpect(status().isNotFound())
                .andExpect(view().name("error/404"))
                .andExpect(model().attribute("errorMessage", "error.404.message"));
    }

    @Test
    void shouldHandleNoResourceFoundException() throws Exception {
        mockMvc.perform(get("/no-resource"))
                .andExpect(status().isNotFound())
                .andExpect(view().name("error/404"))
                .andExpect(model().attribute("errorMessage", "error.404.message"));
    }

    @Test
    void shouldHandleAccessDeniedException() throws Exception {
        mockMvc.perform(get("/access-denied"))
                .andExpect(status().isForbidden())
                .andExpect(view().name("error/403"))
                .andExpect(model().attribute("errorMessage", "error.403.message"));
    }

    @Test
    void shouldHandleTemplateProcessingException() throws Exception {
        mockMvc.perform(get("/template-processing"))
                .andExpect(status().isInternalServerError())
                .andExpect(view().name("error/500"))
                .andExpect(model().attribute("errorMessage", "error.500.message"));
    }

    @Test
    void shouldHandleTemplateInputException() throws Exception {
        mockMvc.perform(get("/template-input"))
                .andExpect(status().isInternalServerError())
                .andExpect(view().name("error/500"))
                .andExpect(model().attribute("errorMessage", "error.500.message"));
    }

    @Test
    void shouldHandleGeneralException() throws Exception {
        mockMvc.perform(get("/general-exception"))
                .andExpect(status().isInternalServerError())
                .andExpect(view().name("error/general"))
                .andExpect(model().attribute("errorMessage", "error.general.message"));
    }

    /*  Dummy controller – throws the exact exceptions handled by the advice */
    @RestController
    static class TestController {

        @GetMapping("/not-found")
        public String throwNoHandlerFound() throws NoHandlerFoundException {
            throw new NoHandlerFoundException("GET", "/not-found", null);
        }

        @GetMapping("/no-resource")
        public String throwNoResourceFound() throws NoResourceFoundException {
            throw new NoResourceFoundException(HttpMethod.GET, "/no-resource");
        }

        @GetMapping("/access-denied")
        public String throwAccessDenied() {
            throw new AccessDeniedException("Access denied");
        }

        /** Thymeleaf 3.x constructor: (message, templateName, line, column) */
        @GetMapping("/template-processing")
        public String throwTemplateProcessing() {
            throw new TemplateProcessingException(
                    "Template processing error",
                    "test.html",
                    42,   // line
                    5     // column – required by the 4-arg constructor
            );
        }

        /** Thymeleaf 3.x constructor: (message, templateName, line, column) */
        @GetMapping("/template-input")
        public String throwTemplateInput() {
            throw new TemplateInputException(
                    "Template input error",
                    "test.html",
                    10,   // line
                    3     // column – required
            );
        }

        @GetMapping("/general-exception")
        public String throwGeneralException(Model model) {
            throw new RuntimeException("Unexpected error");
        }
    }
}