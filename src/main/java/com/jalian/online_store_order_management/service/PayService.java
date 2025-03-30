package com.jalian.online_store_order_management.service;

import com.jalian.online_store_order_management.domain.Item;
import com.jalian.online_store_order_management.domain.User;

import java.util.List;

public interface PayService {

    void pay(User user, List<Item> items);
}
