package com.jalian.online_store_order_management.service.impl;

import com.jalian.online_store_order_management.domain.Product;
import com.jalian.online_store_order_management.exception.LackOfProductException;
import com.jalian.online_store_order_management.service.ProductInventoryOperator;

/**
 * The MinusProductInventory class implements the {@link ProductInventoryOperator} interface
 * to perform a subtraction operation on a product's inventory.
 * <p>
 * This implementation decreases the product's inventory by a specified amount.
 * If the resulting inventory is negative, a {@link LackOfProductException} is thrown.
 * The class follows the singleton pattern, providing a single instance via {@link #getInstance()}.
 * </p>
 *
 * @author amirhosein jalian
 */
public final class MinusProductInventory implements ProductInventoryOperator {

    /**
     * A singleton instance of {@code MinusProductInventory}.
     */
    private static final ProductInventoryOperator productInventoryOperator = new MinusProductInventory();

    /**
     * Private constructor to enforce singleton pattern.
     */
    private MinusProductInventory() {}

    /**
     * Returns the singleton instance of {@code MinusProductInventory}.
     *
     * @return the singleton instance of {@link ProductInventoryOperator}.
     */
    public static ProductInventoryOperator getInstance() {
        return productInventoryOperator;
    }

    /**
     * Decreases the inventory of the specified product by the given amount.
     * <p>
     * If the subtraction results in a negative inventory, a {@link LackOfProductException}
     * is thrown.
     * </p>
     *
     * @param product the product whose inventory is to be decreased.
     * @param amount  the amount by which to decrease the inventory.
     * @return the updated {@link Product} with the new inventory value.
     * @throws LackOfProductException if the resulting inventory is less than zero.
     */
    @Override
    public Product doOperation(Product product, long amount) {
        var presentInventory = product.getInventory() - amount;
        if (presentInventory < 0) {
            throw new LackOfProductException();
        }
        product.setInventory(presentInventory);
        return product;
    }
}
