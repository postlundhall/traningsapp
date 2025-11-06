package com.postlundhall.TraningsregisterSpringH2.service;

import com.postlundhall.TraningsregisterSpringH2.model.Ovning;
import com.postlundhall.TraningsregisterSpringH2.repository.OvningRepository;
import jakarta.persistence.OptimisticLockException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link OvningServiceImpl}.
 * Uses Mockito to mock {@link OvningRepository} and {@link SanitizationUtil}.
 */
@ExtendWith(MockitoExtension.class)
class OvningServiceImplTest {

    @Mock
    private OvningRepository ovningRepository;

    @InjectMocks
    private OvningServiceImpl ovningService;

    private Ovning ovning;

    @BeforeEach
    void setUp() {
        ovning = Ovning.builder()
                .id(1L)
                .ovningsnamn("Bänkpress")
                .utrustning(Ovning.Utrustning.BÄNK)
                .primarMuskel(Ovning.PrimarMuskel.BROST_MITTEN)
                .version(0L)
                .build();
    }

    // ────────────────────── FILTERING & PAGINATION ──────────────────────

    @Test
    void findAllWithFiltersAndSort_appliesAllFiltersAndSortsAscending() {
        Page<Ovning> page = new PageImpl<>(List.of(ovning), PageRequest.of(0, 10), 1);
        when(ovningRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(page);

        Page<Ovning> result = ovningService.findAllWithFiltersAndSort(
                "bänk", "BÄNK", "BROST_MITTEN", "ovningsnamn", "asc", 0, 10);

        assertEquals(1, result.getTotalElements());
        assertEquals("Bänkpress", result.getContent().get(0).getOvningsnamn());

        verify(ovningRepository).findAll(any(Specification.class), eq(PageRequest.of(0, 10,
                Sort.by("ovningsnamn").ascending())));
    }

    @Test
    void findAllWithFiltersAndSort_ignoresInvalidEnumValues() {
        Page<Ovning> page = new PageImpl<>(List.of(ovning));
        when(ovningRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(page);

        Page<Ovning> result = ovningService.findAllWithFiltersAndSort(
                null, "INVALID_EQUIP", "INVALID_MUSCLE", "id", "desc", 2, 5);

        assertEquals(1, result.getTotalElements());
        verify(ovningRepository).findAll(any(Specification.class),
                eq(PageRequest.of(2, 5, Sort.by("id").descending())));
    }

    @Test
    void findAllWithFiltersAndSort_clampsPageAndSize() {
        Page<Ovning> emptyPage = Page.empty(PageRequest.of(0, 1));
        when(ovningRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(emptyPage);

        ovningService.findAllWithFiltersAndSort(null, null, null, "id", "asc", -5, 0);

        verify(ovningRepository).findAll(any(Specification.class),
                eq(PageRequest.of(0, 1, Sort.by("id").ascending())));
    }

    @Test
    void save_sanitizesAndPersistsValidOvningPopup() {
        try (MockedStatic<SanitizationUtil> util = mockStatic(SanitizationUtil.class)) {
            util.when(() -> SanitizationUtil.sanitize("Bänkpress", 255))
                    .thenReturn("Bänkpress");

            when(ovningRepository.save(ovning)).thenReturn(ovning);

            Ovning saved = ovningService.save(ovning);

            assertSame(ovning, saved);
            assertEquals("Bänkpress", saved.getOvningsnamn());
            verify(ovningRepository).save(ovning);
            util.verify(() -> SanitizationUtil.sanitize("Bänkpress", 255));
        }
    }

    @Test
    void save_rejectsXSS() {
        Ovning xss = Ovning.builder()
                .ovningsnamn("<script>alert(1)</script>")
                .utrustning(Ovning.Utrustning.HANTLAR)
                .primarMuskel(Ovning.PrimarMuskel.BICEPS)
                .build();

        try (MockedStatic<SanitizationUtil> util = mockStatic(SanitizationUtil.class)) {
            ValidationException ve = new ValidationException("XSS");
            util.when(() -> SanitizationUtil.sanitize(anyString(), anyInt()))
                    .thenThrow(ve);

            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                    () -> ovningService.save(xss));

            assertEquals("ovning.error.invalid.XSS", ex.getMessage());
            verify(ovningRepository, never()).save(any());
        }
    }

    @Test
    void save_rejectsBlankNameAfterSanitization() {
        Ovning blank = Ovning.builder()
                .ovningsnamn("   ")
                .utrustning(Ovning.Utrustning.BÄNK)
                .primarMuskel(Ovning.PrimarMuskel.BROST_MITTEN)
                .build();

        try (MockedStatic<SanitizationUtil> util = mockStatic(SanitizationUtil.class)) {
            util.when(() -> SanitizationUtil.sanitize("   ", 255))
                    .thenReturn("");

            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                    () -> ovningService.save(blank));

            assertEquals("ovning.error.name.blank", ex.getMessage());
            verify(ovningRepository, never()).save(any());
        }
    }

    @Test
    void save_propagatesOptimisticLockException() {
        try (MockedStatic<SanitizationUtil> util = mockStatic(SanitizationUtil.class)) {
            util.when(() -> SanitizationUtil.sanitize(anyString(), anyInt()))
                    .thenReturn("Bänkpress");

            when(ovningRepository.save(any())).thenThrow(new OptimisticLockException());

            assertThrows(OptimisticLockException.class, () -> ovningService.save(ovning));
            verify(ovningRepository).save(ovning);
        }
    }

    // ────────────────────── DELETE ──────────────────────

    @Test
    void deleteById_rejectsNullId() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> ovningService.deleteById(null));

        assertEquals("ID cannot be null", ex.getMessage());
        verify(ovningRepository, never()).deleteById(any());
    }

    @Test
    void deleteById_callsRepository() {
        ovningService.deleteById(1L);
        verify(ovningRepository).deleteById(1L);
    }
}