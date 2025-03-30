package com.jalian.online_store_order_management.service.impl;

import com.jalian.online_store_order_management.domain.Item;
import com.jalian.online_store_order_management.domain.User;
import com.jalian.online_store_order_management.service.UserService;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@EnableAsync
public non-sealed class ASyncPayServiceImpl extends AbstractPayService {

    public ASyncPayServiceImpl(UserService userService) {
        super(userService);
    }

    @Override
    @Async
    @Retryable
    @Transactional
    public void pay(User user, List<Item> items) {
        super.pay(user, items);
    }
}
