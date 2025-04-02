package com.jalian.online_store_order_management.dao;

import com.jalian.online_store_order_management.domain.Store;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * The StoreDao interface provides database access operations for {@link Store} entities.
 * <p>
 * It extends {@link JpaRepository} to inherit standard CRUD operations and includes custom methods
 * that apply optimistic locking with a force increment strategy when retrieving stores by their ID or name.
 * This ensures data integrity during concurrent modifications.
 * </p>
 *
 * @author amirhosein jalian
 */
@Repository
public interface StoreDao extends JpaRepository<Store, Long> {

    /**
     * Retrieves a {@link Store} entity by its ID using an optimistic lock with force increment.
     * <p>
     * The {@code @Lock(LockModeType.OPTIMISTIC_FORCE_INCREMENT)} annotation applies an optimistic locking
     * strategy that forces an increment of the version attribute, ensuring data consistency when concurrent
     * modifications occur.
     * </p>
     *
     * @param id the unique identifier of the store to retrieve.
     * @return an {@link Optional} containing the found store, or an empty {@link Optional} if no store with the
     * specified ID exists.
     */
    @Lock(LockModeType.OPTIMISTIC_FORCE_INCREMENT)
    @Query("select s from Store s where s.id = :id")
    Optional<Store> findByIdSafe(@Param("id") Long id);

    /**
     * Retrieves a {@link Store} entity by its name using an optimistic lock with force increment.
     * <p>
     * The {@code @Lock(LockModeType.OPTIMISTIC_FORCE_INCREMENT)} annotation applies an optimistic locking
     * strategy that forces an increment of the version attribute, ensuring data consistency during concurrent
     * access when searching for a store by name.
     * </p>
     *
     * @param name the name of the store to retrieve.
     * @return an {@link Optional} containing the found store, or an empty {@link Optional} if no store with the
     * specified name exists.
     */
    @Lock(LockModeType.OPTIMISTIC_FORCE_INCREMENT)
    @Query("select s from Store s where s.name = :name")
    Optional<Store> findByNameSafe(@Param("name") String name);
}
