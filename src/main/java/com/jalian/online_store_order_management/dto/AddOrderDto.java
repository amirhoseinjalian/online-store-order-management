package com.jalian.online_store_order_management.dto;

import java.util.List;

public record AddOrderDto(Long userId, Long storeId, List<ItemDto> items) {
}
