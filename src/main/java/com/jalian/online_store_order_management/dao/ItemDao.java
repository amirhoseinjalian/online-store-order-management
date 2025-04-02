package com.jalian.online_store_order_management.dao;

import com.jalian.online_store_order_management.domain.Item;
import com.jalian.online_store_order_management.domain.key.ItemKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * The ItemDao interface provides database access operations for {@link Item} entities.
 * <p>
 * It extends {@link JpaRepository} to inherit standard CRUD operations and defines a custom query method
 * to retrieve items associated with a specific order.
 * </p>
 *
 * @author amirhosein jalian
 */
@Repository
public interface ItemDao extends JpaRepository<Item, ItemKey> {

    /**
     * Retrieves a list of {@link Item} entities associated with a given order ID.
     * <p>
     * This method uses a custom JPQL query to find all items where the order's ID matches the specified parameter.
     * </p>
     *
     * @param id the ID of the order for which to fetch the items.
     * @return a list of items linked to the specified order.
     */
    @Query("select i from Item i where i.order.id = :id")
    List<Item> findAllByOrder(@Param("id") Long id);
}
