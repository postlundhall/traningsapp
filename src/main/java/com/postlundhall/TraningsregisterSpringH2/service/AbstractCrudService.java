package com.postlundhall.TraningsregisterSpringH2.service;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Abstract class providing default implementations of {@link CrudService}, using Spring Data JPA.
 * Subclasses (e.g. {@link OvningServiceImpl}) extend this class,
 * and provide a concrete {@link JpaRepository} (e.g. OvningRepository).
 * @param <T> the entity type
 * @param <ID> the data type of the ID-identifier (belonging to the entity).
 * @author postlundhall
 * @since 1.0
 */
public abstract class AbstractCrudService<T, ID> implements CrudService<T, ID> {

    protected final JpaRepository<T, ID> repository;

    /**
     * Constructs a new CrudService with the provided repository.
     * @param repository the entity's JPA repository
     */
    protected AbstractCrudService(JpaRepository<T, ID> repository) {
        this.repository = repository;
    }

    /**
     * Retrieves every entity for the entity type.
     * @return a list of all entities
     */
    @Override
    public List<T> findAll() {
        return repository.findAll();
    }

    /**
     * Retrieves one entity by its ID-identifier.
     * @param id the entity's identifier.
     * @return an {@link Optional} that contains the entity if it is found, or that is empty if the entity is not found.
     */
    @Override
    public Optional<T> findById(ID id) {
        return repository.findById(id);
    }

    /**
     * Saves or updates an entity.
     * @param entity the entity to save
     * @return a persisted entity
     */
    @Override
    public T save(T entity) {
        return repository.save(entity);
    }

    /**
     * Deletes an entity by its ID-identifier.<br>
     * <strong>Note:</strong> Subclasses may override this method to add
     * validation (e.g., null-check on {@code id}).
     * @param id the entity's identifier.
     */
    @Override
    public void deleteById(ID id) {
        repository.deleteById(id);
    }
}