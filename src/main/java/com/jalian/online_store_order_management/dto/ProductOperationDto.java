package com.jalian.online_store_order_management.dto;

/**
 * The ProductOperationDto record is a data transfer object that encapsulates
 * the details required to perform an operation on a product's inventory.
 * <p>
 * It includes the product's unique identifier and the amount by which
 * the product's inventory should be adjusted.
 * </p>
 *
 * @param productId the unique identifier of the product.
 * @param amount    the quantity by which the product's inventory is to be adjusted.
 *
 * @author amirhosein jalian
 */
public record ProductOperationDto(Long productId, long amount) {
}
