package com.jalian.online_store_order_management.service.impl;

import com.jalian.online_store_order_management.domain.Product;
import com.jalian.online_store_order_management.exception.LackOfProductException;
import com.jalian.online_store_order_management.service.ProductInventoryOperator;

public final class MinusProductInventory implements ProductInventoryOperator {

    private static final ProductInventoryOperator productInventoryOperator = new MinusProductInventory();

    private MinusProductInventory() {}

    public static ProductInventoryOperator getInstance() {
        return productInventoryOperator;
    }

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
