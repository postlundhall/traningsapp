package com.postlundhall.TraningsregisterSpringH2.controller;

import com.postlundhall.TraningsregisterSpringH2.model.Ovning;
import com.postlundhall.TraningsregisterSpringH2.service.OvningService;
import jakarta.persistence.OptimisticLockException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class OvningControllerTest {

    @Mock
    private OvningService ovningService;

    @InjectMocks
    private OvningController controller;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    @WithMockUser(roles = "USER")
    void shouldReturnListViewWithOvningar() throws Exception {
        Ovning ovning = Ovning.builder().id(1L).ovningsnamn("Bänkpress").utrustning(Ovning.Utrustning.BÄNK).primarMuskel(Ovning.PrimarMuskel.BROST_MITTEN).build();
        Page<Ovning> page = new PageImpl<>(List.of(ovning), PageRequest.of(0, 10), 1);
        when(ovningService.findAllWithFiltersAndSort(null, null, null, "ovningsnamn", "asc", 0, 10)).thenReturn(page);

        mockMvc.perform(get("/ovningar"))
                .andExpect(status().isOk())
                .andExpect(view().name("ovningar/list"))
                .andExpect(model().attributeExists("ovningar"))
                .andExpect(model().attribute("currentPage", 0))
                .andExpect(model().attribute("totalPages", 1))
                .andExpect(model().attribute("totalItems", 1L));
    }

    @Test
    @WithMockUser(roles = "USER")
    void shouldReturnCreateForm() throws Exception {
        mockMvc.perform(get("/ovningar/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("ovningar/form"))
                .andExpect(model().attributeExists("ovning"))
                .andExpect(model().attribute("ovning", org.hamcrest.Matchers.instanceOf(Ovning.class)));
    }

    @Test
    @WithMockUser(roles = "USER")
    void shouldReturnEditFormWhenOvningExists() throws Exception {
        Ovning ovning = Ovning.builder().id(1L).ovningsnamn("Bänkpress").build();
        when(ovningService.findById(1L)).thenReturn(Optional.of(ovning));

        mockMvc.perform(get("/ovningar/1/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("ovningar/form"))
                .andExpect(model().attribute("ovning", ovning));
    }

    @Test
    @WithMockUser(roles = "USER")
    void shouldRedirectToErrorWhenEditOvningNotFound() throws Exception {
        when(ovningService.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/ovningar/1/edit"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/ovningar/error"))
                .andExpect(flash().attribute("errorMessage", "ovning.error.notfound"))
                .andExpect(flash().attribute("entityId", 1L));
    }

    @Test
    @WithMockUser(roles = "USER")
    void shouldCreateOvningSuccessfully() throws Exception {
        Ovning savedOvning = Ovning.builder().id(1L).ovningsnamn("Bänkpress").utrustning(Ovning.Utrustning.BÄNK).primarMuskel(Ovning.PrimarMuskel.BROST_MITTEN).build();
        when(ovningService.save(any(Ovning.class))).thenReturn(savedOvning);

        mockMvc.perform(post("/ovningar")
                        .param("ovningsnamn", "Bänkpress")
                        .param("utrustning", "BÄNK")
                        .param("primarMuskel", "BROST_MITTEN")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/ovningar"))
                .andExpect(flash().attribute("successMessage", "ovning.success.created"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void shouldFailCreateOvningWithValidationErrors() throws Exception {
        mockMvc.perform(post("/ovningar")
                        .param("ovningsnamn", "") // Invalid: blank
                        .param("utrustning", "BÄNK")
                        .param("primarMuskel", "BROST_MITTEN")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("ovningar/form"))
                .andExpect(model().attributeHasFieldErrors("ovning", "ovningsnamn"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void shouldEditOvningSuccessfully() throws Exception {
        Ovning editedOvning = Ovning.builder().id(1L).ovningsnamn("Bänkpress").utrustning(Ovning.Utrustning.BÄNK).primarMuskel(Ovning.PrimarMuskel.BROST_MITTEN).build();
        when(ovningService.save(any(Ovning.class))).thenReturn(editedOvning);

        mockMvc.perform(post("/ovningar/1")
                        .param("id", "1")
                        .param("ovningsnamn", "Bänkpress")
                        .param("utrustning", "BÄNK")
                        .param("primarMuskel", "BROST_MITTEN")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/ovningar"))
                .andExpect(flash().attribute("successMessage", "ovning.success.edited"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void shouldFailEditWithIdMismatch() throws Exception {
        mockMvc.perform(post("/ovningar/1")
                        .param("id", "2") // Mismatch
                        .param("ovningsnamn", "Bänkpress")
                        .param("utrustning", "BÄNK")
                        .param("primarMuskel", "BROST_MITTEN")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("ovningar/form"))
                .andExpect(model().attribute("errorMessage", "ovning.error.id.mismatch"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void shouldDeleteOvningSuccessfully() throws Exception {
        doNothing().when(ovningService).deleteById(1L);

        mockMvc.perform(post("/ovningar/1/delete")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/ovningar"))
                .andExpect(flash().attribute("successMessage", "ovning.success.deleted"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void shouldReturnViewWhenOvningExists() throws Exception {
        Ovning ovning = Ovning.builder().id(1L).ovningsnamn("Bänkpress").build();
        when(ovningService.findById(1L)).thenReturn(Optional.of(ovning));

        mockMvc.perform(get("/ovningar/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("ovningar/view"))
                .andExpect(model().attribute("ovning", ovning));
    }

    @Test
    @WithMockUser(roles = "USER")
    void shouldReturnErrorViewWhenOvningNotFound() throws Exception {
        when(ovningService.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/ovningar/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("ovningar/error"))
                .andExpect(model().attribute("errorMessage", "ovning.error.notfound"))
                .andExpect(model().attribute("entityId", 1L));
    }

    @Test
    @WithMockUser(roles = "USER")
    void shouldReturnListWithFiltersAndSorting() throws Exception {
        Ovning ovning = Ovning.builder().id(1L).ovningsnamn("Bänkpress").utrustning(Ovning.Utrustning.BÄNK).primarMuskel(Ovning.PrimarMuskel.BROST_MITTEN).build();
        Page<Ovning> page = new PageImpl<>(List.of(ovning), PageRequest.of(0, 10), 1);
        when(ovningService.findAllWithFiltersAndSort("Bänk", "BÄNK", "BROST_MITTEN", "primarMuskel", "desc", 0, 10)).thenReturn(page);

        mockMvc.perform(get("/ovningar")
                        .param("search", "Bänk")
                        .param("equipment", "BÄNK")
                        .param("muscle", "BROST_MITTEN")
                        .param("sortBy", "primarMuskel")
                        .param("sortDir", "desc")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(view().name("ovningar/list"))
                .andExpect(model().attributeExists("ovningar"))
                .andExpect(model().attribute("ovningar", hasSize(1)))
                .andExpect(model().attribute("search", "Bänk"))
                .andExpect(model().attribute("equipment", "BÄNK"))
                .andExpect(model().attribute("muscle", "BROST_MITTEN"))
                .andExpect(model().attribute("sortBy", "primarMuskel"))
                .andExpect(model().attribute("sortDir", "desc"))
                .andExpect(model().attribute("currentPage", 0))
                .andExpect(model().attribute("totalPages", 1))
                .andExpect(model().attribute("totalItems", 1L))
                .andExpect(model().attribute("size", 10));
    }

    @Test
    @WithMockUser(roles = "USER")
    void shouldReturnEmptyListWithInvalidParams() throws Exception {
        Page<Ovning> emptyPage = new PageImpl<>(Collections.emptyList(), PageRequest.of(0, 10), 0);
        when(ovningService.findAllWithFiltersAndSort("NonExistent", "INVALID_EQUIP", "INVALID_MUSCLE", "ovningsnamn", "asc", 0, 10)).thenReturn(emptyPage);

        mockMvc.perform(get("/ovningar")
                        .param("search", "NonExistent")
                        .param("equipment", "INVALID_EQUIP")
                        .param("muscle", "INVALID_MUSCLE")
                        .param("sortBy", "ovningsnamn")
                        .param("sortDir", "asc")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(view().name("ovningar/list"))
                .andExpect(model().attributeExists("ovningar"))
                .andExpect(model().attribute("ovningar", hasSize(0)))
                .andExpect(model().attribute("currentPage", 0))
                .andExpect(model().attribute("totalPages", 0))
                .andExpect(model().attribute("totalItems", 0L));
    }

    @Test
    @WithMockUser(roles = "USER")
    void shouldFailCreateWithIllegalArgumentException() throws Exception {
        when(ovningService.save(any(Ovning.class))).thenThrow(new IllegalArgumentException("ovning.error.invalid.characters"));

        mockMvc.perform(post("/ovningar")
                        .param("ovningsnamn", "Invalid@Name")
                        .param("utrustning", "BÄNK")
                        .param("primarMuskel", "BROST_MITTEN")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("ovningar/form"))
                .andExpect(model().attributeExists("ovning"))
                .andExpect(model().attribute("errorMessage", "ovning.error.invalid.characters"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void shouldFailEditWithOptimisticLockException() throws Exception {
        when(ovningService.save(any(Ovning.class))).thenThrow(new OptimisticLockException("Concurrent modification"));

        mockMvc.perform(post("/ovningar/1")
                        .param("id", "1")
                        .param("ovningsnamn", "Bänkpress")
                        .param("utrustning", "BÄNK")
                        .param("primarMuskel", "BROST_MITTEN")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("ovningar/form"))
                .andExpect(model().attributeExists("ovning"))
                .andExpect(model().attribute("errorMessage", "ovning.error.optimistic.lock"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void shouldFailDeleteWithException() throws Exception {
        doThrow(new IllegalArgumentException("Delete failed")).when(ovningService).deleteById(1L);

        mockMvc.perform(post("/ovningar/1/delete")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/ovningar"))
                .andExpect(flash().attribute("errorMessage", "ovning.error.delete"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void shouldReturnErrorViewForViewWithException() throws Exception {
        when(ovningService.findById(1L)).thenThrow(new RuntimeException("Unexpected error"));

        mockMvc.perform(get("/ovningar/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("ovningar/error"))
                .andExpect(model().attribute("errorMessage", "ovning.error.unexpected"))
                .andExpect(model().attribute("entityId", 1L));
    }
}