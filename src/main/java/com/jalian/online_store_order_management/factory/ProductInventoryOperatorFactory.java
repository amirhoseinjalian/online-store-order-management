package com.jalian.online_store_order_management.factory;

import com.jalian.online_store_order_management.constant.ProductOperationStrategy;
import com.jalian.online_store_order_management.service.ProductInventoryOperator;
import com.jalian.online_store_order_management.service.impl.MinusProductInventory;
import com.jalian.online_store_order_management.service.impl.PlusProductInventory;

public final class ProductInventoryOperatorFactory {

    private ProductInventoryOperatorFactory() {
        throw new RuntimeException("This class cannot be instantiated");
    }

    public static ProductInventoryOperator getInstance(ProductOperationStrategy strategy) {
        return switch (strategy) {
            case PLUS -> PlusProductInventory.getInstance();
            case MINUS -> MinusProductInventory.getInstance();
        };
    }
}
