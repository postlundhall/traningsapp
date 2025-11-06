package com.postlundhall.TraningsregisterSpringH2.service;

import com.postlundhall.TraningsregisterSpringH2.model.Ovning;
import com.postlundhall.TraningsregisterSpringH2.repository.OvningRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Root;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import jakarta.persistence.criteria.Predicate;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of {@link OvningService}, providing CRUD-operations
 * and advanced filtering + sorting for {@link Ovning} entities.
 * Handles input sanitization, validation, logging, and error handling for invalid filter enum values.
 * @see OvningService
 * @see AbstractCrudService
 * @author postlundhall
 * @since 1.0
 */
@Service
public class OvningServiceImpl extends AbstractCrudService<Ovning, Long> implements OvningService {

    private static final Logger logger = LoggerFactory.getLogger(OvningServiceImpl.class);
    private final OvningRepository ovningRepository;

    /**
     * Constructs a new service with the provided OvningRepository.
     * @param repository Repository for {@link Ovning} entities
     */
    @Autowired
    public OvningServiceImpl(OvningRepository repository) {
        super(repository);
        this.ovningRepository = repository;
    }

    /**
     * Retrieves a paginated list of {@link Ovning} entities with optional filtering
     * and sorting.
     * Supports filtering by:
     * <ul>
     * <li><strong>Name</strong> - partial match (case-insensitive)</li>
     * <li><strong>Equipment</strong> - exact enum match</li>
     * <li><strong>Muscle group</strong> - exact enum match</li>
     * </ul>
     * Invalid enum values are logged and ignored.
     *
     * @param search     optional keyword for filtering by Exercise-name
     * @param equipment  optional filter by equipment type
     * @param muscle     optional filter by primary muscle group
     * @param sortBy     the field to sort by (e.g., "ovningsnamn" or "utrustning")
     * @param sortDir    the sort direction: "asc" or "desc"
     * @param page       the page number
     * @param size       the page size
     * @return a {@link Page} of filtered and sorted {@link Ovning} entities
     */
    @Override
    public Page<Ovning> findAllWithFiltersAndSort(
            String search, String equipment, String muscle,
            String sortBy, String sortDir, int page, int size) {

        // Specify JPA query conditions to root Ovning, with JPA query, and filtering criteria
        Specification<Ovning> spec = (root, query, cb)
                // Build AND-predicate comprising each filter to be applied in the query
                -> cb.and(
                nameLikePredicate(root, cb, search),
                equipmentEqualsPredicate(root, cb, equipment),
                muscleEqualsPredicate(root, cb, muscle)
        );

        // Sort according to provided sortDir and sortBy, or default to ascending direction by id
        String direction = (sortDir == null || sortDir.isBlank()) ? "asc" : sortDir.trim().toLowerCase();
        String field     = (sortBy == null || sortBy.isBlank()) ? "id" : sortBy.trim();
        Sort sort = "desc".equals(direction) ? Sort.by(field).descending() : Sort.by(field).ascending();

        // Build sortable Pageable object (clamped page/size preventing negative or zero values)
        Pageable pageable = PageRequest.of(Math.max(0, page), Math.max(1, size), sort);
        logger.debug("Finding Ovningar with filters: search={}, equipment={}, muscle={}, sortBy={}, sortDir={}, page={}, size={}",
                search, equipment, muscle, sortBy, sortDir, page, size);

        // Execute query including filters, pagination and sorting.
        return ovningRepository.findAll(spec, pageable);
    }

    // ────────────────────── Predicate Helpers ──────────────────────

    /**
     * Builds a predicate for filtering by exercise name, using case-insensitive partial match.
     *
     * @param root   JPA root for {@link Ovning}
     * @param cb     the {@link CriteriaBuilder} instance
     * @param search the search term (e.g. press)
     * @return LIKE predicate (e.g. {@code LOWER(ovningsnamn) LIKE '%press%'}),
     *         or {@code cb.conjunction()} (always true) if {@code search} is blank.
     */
    private static Predicate nameLikePredicate(Root<Ovning> root, CriteriaBuilder cb, String search) {
        return StringUtils.hasText(search)
                ? cb.like(cb.lower(root.get("ovningsnamn")), "%" + search.toLowerCase() + "%")
                : cb.conjunction();
    }

    /**
     * Builds a predicate for filtering by equipment, using exact enum match.
     * Invalid values are logged and ignored.
     *
     * @param root      JPA root for {@link Ovning}
     * @param cb        the {@link CriteriaBuilder} instance
     * @param equipment the equipment name (e.g. "HANTLAR")
     * @return Equals predicate (e.g. {@code utrustning = :value}),
     *         or {@code cb.conjunction()} (always true) if {@code equipment} is blank or invalid.
     */
    private static Predicate equipmentEqualsPredicate(Root<Ovning> root, CriteriaBuilder cb, String equipment) {
        if (!StringUtils.hasText(equipment)) return cb.conjunction();
        try {
            Ovning.Utrustning utrustningEnum = Ovning.Utrustning.valueOf(equipment.toUpperCase());
            return cb.equal(root.get("utrustning"), utrustningEnum);
        } catch (IllegalArgumentException e) {
            logger.warn("Invalid equipment filter: {}", equipment);
            return cb.conjunction();
        }
    }

    /**
     * Builds a predicate for filtering by equipment, using exact enum match
     * Invalid values are logged and ignored.
     *
     * @param root   JPA root for {@link Ovning}
     * @param cb     the {@link CriteriaBuilder} instance
     * @param muscle the muscle group (e.g. "LAR_BAKSIDA")
     * @return Equals predicate (e.g. {@code primarMuskel = :value}),
     * or {@code cb.conjunction()} (always true) if {@code muscle} is blank or invalid.
     */
    private static Predicate muscleEqualsPredicate(Root<Ovning> root, CriteriaBuilder cb, String muscle) {
        if (!StringUtils.hasText(muscle)) return cb.conjunction();
        try {
            Ovning.PrimarMuskel muscleEnum = Ovning.PrimarMuskel.valueOf(muscle.toUpperCase());
            return cb.equal(root.get("primarMuskel"), muscleEnum);
        } catch (IllegalArgumentException e) {
            logger.warn("Invalid muscle filter: {}", muscle);
            return cb.conjunction();
        }
    }

    /**
     * Saves or updates an entity.
     * Sanitizes the entity's Ovningsnamn using {@link SanitizationUtil}, ensures it's not blank, and logs the operation.
     * Throws {@link IllegalArgumentException} on validation failure.
     * @param entity the entity to save
     * @return a persisted entity
     */
    @Override
    @Transactional
    public Ovning save(Ovning entity) {
        if (entity.getOvningsnamn() != null) {
            try {
                String sanitizedName = SanitizationUtil.sanitize(entity.getOvningsnamn(), 255);
                logger.debug("Sanitizing ovningsnamn: original={}, sanitized={}", entity.getOvningsnamn(), sanitizedName);
                if (sanitizedName == null || sanitizedName.isBlank()) {
                    logger.error("Sanitized ovningsnamn is blank for Ovning ID {}", entity.getId());
                    throw new IllegalArgumentException("ovning.error.name.blank");
                }
                entity.setOvningsnamn(sanitizedName);
            } catch (ValidationException e) {
                logger.error("Validation failed for ovningsnamn: reason={}", e.getReason());
                throw new IllegalArgumentException("ovning.error.invalid." + e.getReason());
            }
        }
        logger.debug("Saving Ovning: ID={}, Name={}", entity.getId(), entity.getOvningsnamn());
        return super.save(entity);
    }

    /**
     * Retrieves one entity by its ID-identifier.
     * @param id the entity's identifier.
     * @return an {@link Optional} that contains the entity if it is found, or that is empty if the entity is not found.
     */
    @Override
    public Optional<Ovning> findById(Long id) {
        logger.debug("Finding Ovning by ID {}", id);
        return super.findById(id);
    }

    /**
     * Retrieves every entity for the entity type.
     * @return a list of all entities
     */
    @Override
    public List<Ovning> findAll() {
        logger.debug("Finding all Ovningar");
        return super.findAll();
    }

    /**
     * Deletes an entity by its ID-identifier.
     * Validates that the ID is not null before deletion.
     * @param id the entity's identifier.
     */
    @Override
    @Transactional
    public void deleteById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        repository.deleteById(id);  // Direct call — no super
    }
}