package com.jalian.online_store_order_management.dao;

import com.jalian.online_store_order_management.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The OrderDao interface provides database access operations for {@link Order} entities.
 * <p>
 * It extends {@link JpaRepository} to inherit standard CRUD operations for orders.
 * </p>
 *
 * @author amirhosein jalian
 */
@Repository
public interface OrderDao extends JpaRepository<Order, Long> {
}
