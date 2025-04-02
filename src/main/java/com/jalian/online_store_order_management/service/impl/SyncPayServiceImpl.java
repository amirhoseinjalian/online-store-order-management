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

/**
 * The SyncPayServiceImpl class provides a synchronous implementation of the payment service.
 * <p>
 * It extends {@link AbstractPayService} to leverage common payment functionality and processes payments
 * within a mandatory transaction. Upon successful payment, the order status is updated to {@link OrderStatus#FINISHED}.
 * In case of any exceptions during the payment process, the order status is set to {@link OrderStatus#FAILED},
 * and the exception is propagated.
 * </p>
 *
 * @author amirhosein jalian
 */
@Service
public non-sealed class SyncPayServiceImpl extends AbstractPayService {

    private static final Logger log = LoggerFactory.getLogger(SyncPayServiceImpl.class);

    /**
     * Constructs a new SyncPayServiceImpl with the specified {@link UserService}.
     *
     * @param userService the service used for user operations and balance updates.
     */
    public SyncPayServiceImpl(UserService userService) {
        super(userService);
    }

    /**
     * Processes the payment synchronously for a given order.
     * <p>
     * The payment is executed within a transaction marked with {@link Propagation#MANDATORY}. This method logs
     * the payment process, invokes the common payment operation defined in the superclass, and updates the order status
     * accordingly. If an exception occurs during the payment, the order status is set to {@link OrderStatus#FAILED},
     * the exception is logged, and rethrown.
     * </p>
     *
     * @param user  the user making the payment.
     * @param order the order for which the payment is being processed.
     * @param items the list of items included in the order.
     * @throws RuntimeException if any error occurs during payment processing.
     */
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
