package com.jalian.online_store_order_management.service;

import com.jalian.online_store_order_management.dto.AddOrderDto;
import com.jalian.online_store_order_management.dto.OrderFetchDto;

/**
 * The OrderService interface defines the operations for managing orders within the system.
 * <p>
 * It provides methods for adding a new order and fetching order details by its unique identifier.
 * </p>
 *
 * @author amirhosein jalian
 */
public interface OrderService {

    /**
     * Adds a new order to the system.
     * <p>
     * This method accepts an {@link AddOrderDto} containing order details and uses the provided {@link PayService}
     * to process the payment associated with the order. It returns the unique identifier of the created order.
     * </p>
     *
     * @param dto        the data transfer object containing order creation details.
     * @param payService the payment service to be used for processing the order payment.
     * @return the unique identifier of the newly created order.
     */
    Long addOrder(AddOrderDto dto, PayService payService);

    /**
     * Retrieves the details of an order by its unique identifier.
     * <p>
     * This method returns an {@link OrderFetchDto} containing the complete details of the specified order,
     * including its associated items, owner, and store.
     * </p>
     *
     * @param orderId the unique identifier of the order to fetch.
     * @return an {@link OrderFetchDto} representing the order details.
     */
    OrderFetchDto findOrderById(Long orderId);
}
