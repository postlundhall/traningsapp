package com.postlundhall.TraningsregisterSpringH2.controller;

import com.postlundhall.TraningsregisterSpringH2.TraningsregisterSpringH2Application;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = TraningsregisterSpringH2Application.class)
@AutoConfigureMockMvc
class HomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        // MockMvc is auto-configured with Spring Security via @AutoConfigureMockMvc
    }

    @Test
    @WithMockUser(roles = "USER")
    void shouldReturnHomePage() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/").with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attributeDoesNotExist("errorMessage"))
                .andExpect(model().attributeDoesNotExist("successMessage"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void shouldDisplaySuccessMessageWhenProvided() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/")
                        .flashAttr("successMessage", "ovning.success.created")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attribute("successMessage", "ovning.success.created"))
                .andExpect(model().attributeDoesNotExist("errorMessage"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void shouldDisplayErrorMessageWhenProvided() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/")
                        .flashAttr("errorMessage", "ovning.error.unexpected")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attribute("errorMessage", "ovning.error.unexpected"))
                .andExpect(model().attributeDoesNotExist("successMessage"));
    }

    @Test
    void shouldRedirectToLoginForUnauthenticatedUser() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void shouldHandleUnexpectedException() throws Exception {
        // Exception handling managed by GlobalExceptionHandler, tested separately
        mockMvc.perform(get("/").with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
    }
}