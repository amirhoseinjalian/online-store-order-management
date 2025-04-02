package com.jalian.online_store_order_management.service.impl;

import com.jalian.online_store_order_management.domain.Product;
import com.jalian.online_store_order_management.service.ProductInventoryOperator;

/**
 * The PlusProductInventory class implements the {@link ProductInventoryOperator} interface
 * to perform an addition operation on a product's inventory.
 * <p>
 * This implementation increases the product's inventory by a specified amount.
 * The class follows the singleton pattern, providing a single instance via {@link #getInstance()}.
 * </p>
 *
 * @author amirhosein jalian
 */
public final class PlusProductInventory implements ProductInventoryOperator {

    /**
     * A singleton instance of {@code PlusProductInventory}.
     */
    private static final ProductInventoryOperator INSTANCE = new PlusProductInventory();

    /**
     * Private constructor to enforce the singleton pattern.
     */
    private PlusProductInventory() {}

    /**
     * Returns the singleton instance of {@code PlusProductInventory}.
     *
     * @return the singleton instance of {@link ProductInventoryOperator}.
     */
    public static ProductInventoryOperator getInstance() {
        return INSTANCE;
    }

    /**
     * Increases the inventory of the specified product by the given amount.
     * <p>
     * The updated product with the increased inventory is returned.
     * </p>
     *
     * @param product the product whose inventory is to be increased.
     * @param amount  the amount by which to increase the inventory.
     * @return the updated {@link Product} with the new inventory value.
     */
    @Override
    public Product doOperation(Product product, long amount) {
        product.setInventory(product.getInventory() + amount);
        return product;
    }
}
