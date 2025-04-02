package com.jalian.online_store_order_management.dto;

/**
 * The ProductDto record is a data transfer object that encapsulates the product details
 * for creation or update operations.
 * <p>
 * It includes the product's name, description, price, and the identifier of the store offering the product.
 * </p>
 *
 * @param name        the name of the product.
 * @param description the description of the product.
 * @param price       the price of the product.
 * @param storeId     the unique identifier of the store associated with the product.
 *
 * @author amirhosein jalian
 */
public record ProductDto(String name, String description, double price, long storeId) {
}
