package com.postlundhall.TraningsregisterSpringH2.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BaseCrudServiceTest {

    @Mock
    private JpaRepository<TestEntity, Long> repository;

    private TestCrudService service;

    @BeforeEach
    void setUp() {
        service = new TestCrudService(repository);
    }

    @Test
    void save_callsRepositorySaveAndReturnsEntity() {
        TestEntity entity = new TestEntity(1L, "Test");
        when(repository.save(entity)).thenReturn(entity);

        TestEntity result = service.save(entity);

        assertSame(entity, result);
        verify(repository).save(entity);
    }

    @Test
    void findById_returnsOptionalWithEntity_whenPresent() {
        TestEntity entity = new TestEntity(1L, "Test");
        when(repository.findById(1L)).thenReturn(Optional.of(entity));

        Optional<TestEntity> result = service.findById(1L);

        assertTrue(result.isPresent());
        assertSame(entity, result.get());
        verify(repository).findById(1L);
    }

    @Test
    void findById_returnsEmptyOptional_whenNotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        Optional<TestEntity> result = service.findById(1L);

        assertFalse(result.isPresent());
        verify(repository).findById(1L);
    }

    @Test
    void findAll_returnsListFromRepository() {
        TestEntity e1 = new TestEntity(1L, "A");
        TestEntity e2 = new TestEntity(2L, "B");
        when(repository.findAll()).thenReturn(List.of(e1, e2));

        List<TestEntity> result = service.findAll();

        assertEquals(2, result.size());
        assertTrue(result.contains(e1));
        assertTrue(result.contains(e2));
        verify(repository).findAll();
    }

    @Test
    void deleteById_callsRepositoryDelete() {
        service.deleteById(1L);
        verify(repository).deleteById(1L);
    }

    // --- Test Entity & Service ---
    static class TestEntity {
        private Long id;
        private String name;

        public TestEntity(Long id, String name) {
            this.id = id;
            this.name = name;
        }

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
    }

    static class TestCrudService extends AbstractCrudService<TestEntity, Long> {
        public TestCrudService(JpaRepository<TestEntity, Long> repository) {
            super(repository);
        }
    }
}