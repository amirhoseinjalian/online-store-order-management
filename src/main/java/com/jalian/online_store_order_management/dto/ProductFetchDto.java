package com.jalian.online_store_order_management.dto;

import com.jalian.online_store_order_management.domain.Product;

public record ProductFetchDto(Long id, String name, String description, double price, long inventory, String storeName) {

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
