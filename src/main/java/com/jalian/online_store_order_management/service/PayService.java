package com.jalian.online_store_order_management.service;

import com.jalian.online_store_order_management.domain.Item;
import com.jalian.online_store_order_management.domain.Order;
import com.jalian.online_store_order_management.domain.User;

import java.util.List;

/**
 * The PayService interface defines the operation for processing payments in the system.
 * <p>
 * It provides a method to process the payment for a given order placed by a user that includes
 * a list of purchased items.
 * </p>
 *
 * @author amirhosein jalian
 */
public interface PayService {

    /**
     * Processes the payment for an order.
     * <p>
     * This method handles the payment process for the specified order placed by the user,
     * considering the list of items in the order.
     * </p>
     *
     * @param user  the user who placed the order.
     * @param order the order for which payment is being processed.
     * @param items the list of items included in the order.
     */
    void pay(User user, Order order, List<Item> items);
}
