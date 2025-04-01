package com.jalian.online_store_order_management.service.impl;

import com.jalian.online_store_order_management.constant.OrderStatus;
import com.jalian.online_store_order_management.dao.OrderDao;
import com.jalian.online_store_order_management.domain.Item;
import com.jalian.online_store_order_management.domain.Order;
import com.jalian.online_store_order_management.domain.User;
import com.jalian.online_store_order_management.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@EnableAsync
@EnableAspectJAutoProxy(exposeProxy = true)
public non-sealed class ASyncPayServiceImpl extends AbstractPayService {

    private final OrderDao orderDao;

    //todo: must be injected
    private final RetryTemplate retryTemplate;

    private static final Logger log = LoggerFactory.getLogger(ASyncPayServiceImpl.class);

    public ASyncPayServiceImpl(UserService userService, OrderDao orderDao) {
        super(userService);
        this.orderDao = orderDao;
        this.retryTemplate = RetryTemplate.builder()
                .maxAttempts(7)
                .fixedBackoff(150)
                .build();
    }

    @Override
    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void pay(User user, Order order, List<Item> items) {
        retryTemplate.execute(context -> {
            log.info("retry number: {}", context.getRetryCount());
            super.pay(user, order, items);
            order.setOrderStatus(OrderStatus.FINISHED);
            orderDao.save(order);
            log.info("Payment done");
            return null;
        }, context -> {
            order.setOrderStatus(OrderStatus.FAILED);
            orderDao.save(order);
            log.warn("Retry for payment for order {} failed", order.getId(), context.getLastThrowable());            return null;
        });
    }
}
