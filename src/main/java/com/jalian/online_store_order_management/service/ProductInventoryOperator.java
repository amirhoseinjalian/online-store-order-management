package com.jalian.online_store_order_management.service;

import com.jalian.online_store_order_management.domain.Product;

/**
 * The ProductInventoryOperator interface defines the operation to update a product's inventory.
 * <p>
 * It provides a method to perform an inventory update on a given {@link Product} using a specified amount.
 * Different implementations of this interface can represent different inventory operations, such as increasing
 * or decreasing the product inventory.
 * </p>
 *
 * @author amirhosein jalian
 */
public interface ProductInventoryOperator {

    /**
     * Performs an inventory operation on the specified product.
     * <p>
     * This method applies an inventory update (such as addition or subtraction) based on the provided amount and returns
     * the updated product.
     * </p>
     *
     * @param product the product to update.
     * @param amount  the amount by which the product's inventory is to be adjusted.
     * @return the updated {@link Product} after applying the inventory operation.
     */
    Product doOperation(Product product, long amount);
}
