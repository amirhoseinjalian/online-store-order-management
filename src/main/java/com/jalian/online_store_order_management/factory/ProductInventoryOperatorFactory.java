package com.jalian.online_store_order_management.factory;

import com.jalian.online_store_order_management.constant.ProductOperationStrategy;
import com.jalian.online_store_order_management.service.ProductInventoryOperator;
import com.jalian.online_store_order_management.service.impl.MinusProductInventory;
import com.jalian.online_store_order_management.service.impl.PlusProductInventory;

/**
 * The ProductInventoryOperatorFactory class is a utility class that provides an instance of
 * {@link ProductInventoryOperator} based on the given {@link ProductOperationStrategy}.
 * <p>
 * This factory employs the strategy design pattern to determine the appropriate inventory operation:
 * <ul>
 *   <li>{@code PLUS} - Returns an instance of {@link PlusProductInventory} for adding to inventory.</li>
 *   <li>{@code MINUS} - Returns an instance of {@link MinusProductInventory} for subtracting from inventory.</li>
 * </ul>
 * The class is not intended to be instantiated.
 * </p>
 *
 * @author amirhosein jalian
 */
public final class ProductInventoryOperatorFactory {

    /**
     * Private constructor to prevent instantiation of this factory class.
     * <p>
     * This class should only be used in a static context.
     * </p>
     */
    private ProductInventoryOperatorFactory() {
        throw new RuntimeException("This class cannot be instantiated");
    }

    /**
     * Returns an instance of {@link ProductInventoryOperator} based on the specified product operation strategy.
     *
     * @param strategy the strategy that determines which inventory operator to return.
     * @return an instance of {@link ProductInventoryOperator} corresponding to the specified strategy.
     */
    public static ProductInventoryOperator getInstance(ProductOperationStrategy strategy) {
        return switch (strategy) {
            case PLUS -> PlusProductInventory.getInstance();
            case MINUS -> MinusProductInventory.getInstance();
        };
    }
}
