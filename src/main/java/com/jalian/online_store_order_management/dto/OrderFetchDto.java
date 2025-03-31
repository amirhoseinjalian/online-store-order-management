package com.jalian.online_store_order_management.dto;

import com.jalian.online_store_order_management.constant.OrderStatus;
import com.jalian.online_store_order_management.domain.Item;
import com.jalian.online_store_order_management.domain.Order;

import java.util.List;

public record OrderFetchDto(
        Long orderId,
        UserFetchDto owner,
        StoreFetchDto store,
        OrderStatus status,
        List<ItemFetchDto> items
) {

    public static OrderFetchDto of(Order order, List<Item> items) {
        return new OrderFetchDto(
                order.getId(),
                UserFetchDto.of(order.getOwner()),
                new StoreFetchDto(order.getStore().getName()),
                order.getOrderStatus(),
                ItemFetchDto.of(items)
        );
    }
}
