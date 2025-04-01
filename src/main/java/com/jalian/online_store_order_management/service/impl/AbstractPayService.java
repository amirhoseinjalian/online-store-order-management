package com.jalian.online_store_order_management.service.impl;

import com.jalian.online_store_order_management.constant.BalanceOperation;
import com.jalian.online_store_order_management.domain.Item;
import com.jalian.online_store_order_management.domain.Order;
import com.jalian.online_store_order_management.domain.User;
import com.jalian.online_store_order_management.dto.UpdateBalanceDto;
import com.jalian.online_store_order_management.service.PayService;
import com.jalian.online_store_order_management.service.UserService;

import java.util.List;

public sealed abstract class AbstractPayService implements PayService
        permits SyncPayServiceImpl, ASyncPayServiceImpl {

    private final UserService userService;

    public AbstractPayService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void pay(User user, Order order, List<Item> items) {
        var finalPrice = calculatePrice(items);
        userService.updateBalance(new UpdateBalanceDto(user.getId(), finalPrice, BalanceOperation.MINUS));
    }

    protected double calculatePrice(List<Item> items) {
        double[] sum = {0.0};
        items.forEach(item -> sum[0] += item.getCount() * item.getProduct().getPrice());
        return sum[0];
    }
}
