package com.jalian.online_store_order_management.dto;

import java.util.List;

/**
 * The AddOrderDto record is a data transfer object that encapsulates the information
 * required to create a new order.
 * <p>
 * It contains the identifier of the user placing the order, the identifier of the store where
 * the order is being placed, and a list of {@link ItemDto} objects representing the items
 * included in the order.
 * </p>
 *
 * @param userId the unique identifier of the user placing the order.
 * @param storeId the unique identifier of the store associated with the order.
 * @param items the list of items included in the order.
 *
 * @author amirhosein jalian
 */
public record AddOrderDto(Long userId, Long storeId, List<ItemDto> items) {
}
