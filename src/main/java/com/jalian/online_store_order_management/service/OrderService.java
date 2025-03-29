package com.jalian.online_store_order_management.service;

import com.jalian.online_store_order_management.dto.AddOrderDto;

public interface OrderService {

    Long addOrder(AddOrderDto dto);
}
