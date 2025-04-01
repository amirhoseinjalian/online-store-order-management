package com.jalian.online_store_order_management.service.impl;

import com.jalian.online_store_order_management.constant.OrderStatus;
import com.jalian.online_store_order_management.domain.Item;
import com.jalian.online_store_order_management.domain.Order;
import com.jalian.online_store_order_management.domain.User;
import com.jalian.online_store_order_management.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public non-sealed class SyncPayServiceImpl extends AbstractPayService {

    private static final Logger log = LoggerFactory.getLogger(SyncPayServiceImpl.class);

    public SyncPayServiceImpl(UserService userService) {
        super(userService);
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public void pay(User user, Order order, List<Item> items) {
        try {
            log.info("Paying user {} with order {} and items {}", user, order, items);
            super.pay(user, order, items);
            order.setOrderStatus(OrderStatus.FINISHED);
            log.info("Payment done");
        } catch (Exception e) {
            order.setOrderStatus(OrderStatus.FAILED);
            log.error(e.getMessage(), e);
            throw e;
        }
    }
}
