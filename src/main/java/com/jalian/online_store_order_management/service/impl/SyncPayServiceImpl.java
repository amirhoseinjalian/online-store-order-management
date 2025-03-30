package com.jalian.online_store_order_management.service.impl;

import com.jalian.online_store_order_management.domain.Item;
import com.jalian.online_store_order_management.domain.User;
import com.jalian.online_store_order_management.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public non-sealed class SyncPayServiceImpl extends AbstractPayService {

    public SyncPayServiceImpl(UserService userService) {
        super(userService);
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public void pay(User user, List<Item> items) {
        super.pay(user, items);
    }
}
