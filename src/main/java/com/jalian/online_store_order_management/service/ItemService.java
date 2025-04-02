package com.jalian.online_store_order_management.service;

import com.jalian.online_store_order_management.domain.Item;
import com.jalian.online_store_order_management.domain.Order;
import com.jalian.online_store_order_management.dto.ItemDto;

import java.util.List;

/**
 * The ItemService interface defines the operations for managing items within an order.
 * <p>
 * It provides methods to save items associated with an order and to retrieve items based on the order ID.
 * </p>
 *
 * @author amirhosein jalian
 */
public interface ItemService {

    /**
     * Saves a list of items for a given order.
     *
     * @param items the list of item data transfer objects to be saved.
     * @param order the order entity to which the items belong.
     * @return a list of {@link Item} entities that have been persisted.
     */
    List<Item> saveItems(List<ItemDto> items, Order order);

    /**
     * Retrieves all items associated with a specific order.
     *
     * @param orderId the unique identifier of the order.
     * @return a list of {@link Item} entities associated with the given order ID.
     */
    List<Item> getProductsByOrderId(Long orderId);
}
