package com.jalian.online_store_order_management.service;

import com.jalian.online_store_order_management.dto.AddOrderDto;
import com.jalian.online_store_order_management.dto.OrderFetchDto;

public interface OrderService {

    Long addOrder(AddOrderDto dto, PayService payService);
    OrderFetchDto findOrderById(Long orderId);
}
