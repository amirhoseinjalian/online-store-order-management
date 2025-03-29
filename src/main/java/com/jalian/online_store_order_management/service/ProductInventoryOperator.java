package com.jalian.online_store_order_management.service;

import com.jalian.online_store_order_management.domain.Product;

public interface ProductInventoryOperator {

    Product doOperation(Product product, long amount);
}
