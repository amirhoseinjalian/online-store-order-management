package com.jalian.online_store_order_management.service.impl;

import com.jalian.online_store_order_management.domain.Product;
import com.jalian.online_store_order_management.service.ProductInventoryOperator;

public final class PlusProductInventory implements ProductInventoryOperator {

    private static final ProductInventoryOperator INSTANCE = new PlusProductInventory();

    private PlusProductInventory() {}

    public static ProductInventoryOperator getInstance() {
        return INSTANCE;
    }

    @Override
    public Product doOperation(Product product, long amount) {
        product.setInventory(product.getInventory() + amount);
        return product;
    }
}
