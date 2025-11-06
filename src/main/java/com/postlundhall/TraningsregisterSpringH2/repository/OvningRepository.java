package com.postlundhall.TraningsregisterSpringH2.repository;

import com.postlundhall.TraningsregisterSpringH2.model.Ovning;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Repository interface for {@link Ovning} entities. The interface extends {@link JpaRepository} to provide standard
 * CRUD operations, and {@link JpaSpecificationExecutor} to support filtering via dynamic query creation.
 * Entities are identified by {@link Long} primary keys.
 * @author postlundhall
 * @since 1.0
 */
public interface OvningRepository extends JpaRepository<Ovning, Long>, JpaSpecificationExecutor<Ovning> {
}