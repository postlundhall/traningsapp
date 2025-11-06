package com.postlundhall.TraningsregisterSpringH2.service;

import com.postlundhall.TraningsregisterSpringH2.model.Ovning;
import org.springframework.data.domain.Page;

/**
 * Service interface for managing {@link Ovning} entities. Extends {@link CrudService} to provide basic CRUD operations,
 * and adds advanced filtering and sorting capabilities.
 * @author postlundhall
 * @since 1.0
 */
public interface OvningService extends CrudService<Ovning, Long> {

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
    Page<Ovning> findAllWithFiltersAndSort(
            String search,
            String equipment,
            String muscle,
            String sortBy,
            String sortDir,
            int page,
            int size);
}