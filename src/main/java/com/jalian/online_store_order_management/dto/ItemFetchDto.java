package com.jalian.online_store_order_management.dto;

import com.jalian.online_store_order_management.domain.Item;

import java.util.LinkedList;
import java.util.List;

public record ItemFetchDto(Long productId, String productName, String productDescription, long count, double price) {

    public static ItemFetchDto of(Item item) {
        var product = item.getProduct();
        return new ItemFetchDto(
                product.getId(),
                product.getName(),
                product.getDescription(),
                item.getCount(),
                item.getPrice()
        );
    }

    public static List<ItemFetchDto> of(List<Item> items) {
        var result = new LinkedList<ItemFetchDto>();
        items.forEach(item -> result.add(of(item)));
        return result;
    }
}
