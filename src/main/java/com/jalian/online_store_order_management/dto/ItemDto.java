package com.jalian.online_store_order_management.dto;

/**
 * The ItemDto record is a data transfer object that encapsulates the information
 * for an item within an order.
 * <p>
 * It includes the product identifier and the quantity of the product to be ordered.
 * </p>
 *
 * @param productId the unique identifier of the product.
 * @param count     the quantity of the product in the order.
 *
 * @author amirhosein jalian
 */
public record ItemDto(Long productId, long count) {
}
