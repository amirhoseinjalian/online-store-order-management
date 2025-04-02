package com.jalian.online_store_order_management.dao;

import com.jalian.online_store_order_management.domain.Product;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * The ProductDao interface provides database access operations for {@link Product} entities.
 * <p>
 * It extends {@link JpaRepository} to inherit standard CRUD operations and includes a custom method
 * that applies a pessimistic read lock when retrieving a product by its ID to ensure data consistency
 * during concurrent access.
 * </p>
 *
 * @author amirhosein jalian
 */
@Repository
public interface ProductDao extends JpaRepository<Product, Long> {

    /**
     * Retrieves a {@link Product} entity by its ID using a pessimistic read lock.
     * <p>
     * The {@code @Lock(LockModeType.PESSIMISTIC_READ)} annotation ensures that a database-level read lock is applied
     * when the product is retrieved, preventing other transactions from modifying the product data until the current
     * transaction completes. This is useful in scenarios where concurrent modifications could lead to data inconsistency.
     * </p>
     *
     * @param id the unique identifier of the product to retrieve.
     * @return an {@link Optional} containing the found product, or an empty {@link Optional} if no product with the
     * specified ID exists.
     */
    @Lock(LockModeType.PESSIMISTIC_READ)
    @Query("select p from Product p where p.id = :id")
    Optional<Product> findByIdSafe(@Param("id") Long id);
}
