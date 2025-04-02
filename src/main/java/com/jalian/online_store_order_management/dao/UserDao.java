package com.jalian.online_store_order_management.dao;

import com.jalian.online_store_order_management.domain.User;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * The UserDao interface provides database access operations for {@link User} entities.
 * <p>
 * It extends {@link JpaRepository} to inherit standard CRUD operations and defines custom query methods
 * for retrieving users by username or ID with various locking strategies to ensure data consistency
 * in concurrent environments.
 * </p>
 *
 * @author amirhosein jalian
 */
@Repository
public interface UserDao extends JpaRepository<User, Long> {

    /**
     * Retrieves a {@link User} entity by its username using an optimistic locking strategy.
     * <p>
     * The {@code @Lock(LockModeType.OPTIMISTIC)} annotation is used to apply an optimistic lock to
     * the retrieved entity, ensuring data consistency in concurrent access scenarios.
     * </p>
     *
     * @param username the username of the user to retrieve.
     * @return an {@link Optional} containing the found user, or an empty {@link Optional} if no user with
     * the specified username exists.
     */
    @Lock(LockModeType.OPTIMISTIC)
    @Query("select u from User u where u.username = :username")
    Optional<User> findByUsername(@Param("username") String username);

    /**
     * Retrieves a {@link User} entity by its ID using an optimistic locking strategy.
     * <p>
     * The {@code @Lock(LockModeType.OPTIMISTIC)} annotation is used to apply an optimistic lock to the
     * retrieved entity, ensuring that the data remains consistent during concurrent transactions.
     * </p>
     *
     * @param id the unique identifier of the user to retrieve.
     * @return an {@link Optional} containing the found user, or an empty {@link Optional} if no user with
     * the specified ID exists.
     */
    @Lock(LockModeType.OPTIMISTIC)
    @Query("select u from User u where u.id = :id")
    Optional<User> findUserById(@Param("id") Long id);

    /**
     * Retrieves a {@link User} entity by its ID using a pessimistic write lock.
     * <p>
     * The {@code @Lock(LockModeType.PESSIMISTIC_WRITE)} annotation is used to acquire a database-level
     * write lock on the retrieved entity, preventing other transactions from modifying it until the current
     * transaction completes.
     * </p>
     *
     * @param idt the unique identifier of the user to retrieve.
     * @return an {@link Optional} containing the found user, or an empty {@link Optional} if no user with
     * the specified ID exists.
     */
    @Query("select u from User u where u.id = :id")
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<User> findUserByIdForUpdate(@Param("id") Long idt);
}
