package com.jalian.online_store_order_management.dto;

import com.jalian.online_store_order_management.constant.ProductOperationStrategy;

public record ProductOperationDto(Long productId, ProductOperationStrategy strategy, long amount) {
}
