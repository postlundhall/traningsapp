package com.postlundhall.TraningsregisterSpringH2.service;

import java.util.List;
import java.util.Optional;

/**
 * Generic CRUD-service interface for managing crud operations for the application's entities.
 * @param <T> the entity type.
 * @param <ID> the data type of the entity's ID-identifier (e.g. long).
 * @author postlundhall
 * @since 1.0
 */
public interface CrudService<T, ID> {
    /**
     * Retrieves every entity for the entity type.
     * @return a list of all entities
     */
    List<T> findAll();

    /**
     * Retrieves one entity by its ID-identifier.
     * @param id the entity's identifier.
     * @return an {@link Optional} that contains the entity if it is found, or that is empty if the entity is not found.
     */
    Optional<T> findById(ID id);

    /**
     * Saves or updates an entity.
     * @param entity the entity to save
     * @return a persisted entity
     */
    T save(T entity);

    /**
     * Deletes an entity by its ID-identifier.
     * @param id the entity's identifier.
     */
    void deleteById(ID id);
}