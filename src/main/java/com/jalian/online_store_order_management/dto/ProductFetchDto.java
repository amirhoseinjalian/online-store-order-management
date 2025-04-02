package com.jalian.online_store_order_management.dto;

import com.jalian.online_store_order_management.domain.Product;

/**
 * The ProductFetchDto record is a data transfer object that encapsulates detailed information
 * about a product for retrieval operations.
 * <p>
 * It includes the product's unique identifier, name, description, price, inventory count, and the name of the store
 * offering the product.
 * </p>
 *
 * @param id          the unique identifier of the product.
 * @param name        the name of the product.
 * @param description the description of the product.
 * @param price       the price of the product.
 * @param inventory   the current inventory count of the product.
 * @param storeName   the name of the store associated with the product.
 *
 * @author amirhosein jalian
 */
public record ProductFetchDto(Long id, String name, String description, double price, long inventory, String storeName) {

    /**
     * Creates an instance of {@code ProductFetchDto} from a {@link Product} entity.
     *
     * @param product the product entity to map.
     * @return a new {@code ProductFetchDto} instance populated with product details.
     */
    public static ProductFetchDto of(Product product) {
        return new ProductFetchDto(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getInventory(),
                product.getStore().getName()
        );
    }
}
