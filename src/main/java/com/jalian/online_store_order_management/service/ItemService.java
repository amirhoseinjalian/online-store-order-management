package com.jalian.online_store_order_management.service;

import com.jalian.online_store_order_management.domain.Item;
import com.jalian.online_store_order_management.domain.Order;
import com.jalian.online_store_order_management.dto.ItemDto;

import java.util.List;

public interface ItemService {

    List<Item> saveItems(List<ItemDto> items, Order order);
}
