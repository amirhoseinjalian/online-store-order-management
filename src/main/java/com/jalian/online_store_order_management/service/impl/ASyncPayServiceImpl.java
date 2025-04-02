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

/**
 * The ASyncPayServiceImpl class provides an asynchronous implementation of the payment service.
 * <p>
 * It extends the {@link AbstractPayService} to process payments asynchronously with retry capabilities.
 * This class updates the order status to {@link OrderStatus#FINISHED} upon successful payment and sets it
 * to {@link OrderStatus#FAILED} if all retry attempts are exhausted.
 * </p>
 * <p>
 * Note: The retry template is currently instantiated within the constructor and should be injected
 * for improved testability and configuration.
 * </p>
 *
 * @author amirhosein jalian
 */
@Service
@EnableAsync
@EnableAspectJAutoProxy(exposeProxy = true)
public non-sealed class ASyncPayServiceImpl extends AbstractPayService {

    private final OrderDao orderDao;

    /**
     * The RetryTemplate used for retrying the payment operation.
     * <p>
     * TODO: This should be injected rather than instantiated directly.
     * </p>
     */
    private final RetryTemplate retryTemplate;

    private static final Logger log = LoggerFactory.getLogger(ASyncPayServiceImpl.class);

    /**
     * Constructs an ASyncPayServiceImpl instance with the specified {@link UserService} and {@link OrderDao}.
     *
     * @param userService the service used for user operations.
     * @param orderDao    the data access object for order entities.
     */
    public ASyncPayServiceImpl(UserService userService, OrderDao orderDao) {
        super(userService);
        this.orderDao = orderDao;
        this.retryTemplate = RetryTemplate.builder()
                .maxAttempts(7)
                .fixedBackoff(150)
                .build();
    }

    /**
     * Processes the payment asynchronously for a given order.
     * <p>
     * The payment is processed in a new transaction and retried up to a maximum number of attempts.
     * If the payment is successful, the order status is set to {@link OrderStatus#FINISHED}.
     * In case of failure after all retries, the order status is set to {@link OrderStatus#FAILED}.
     * </p>
     *
     * @param user  the user making the payment.
     * @param order the order for which the payment is being processed.
     * @param items the list of items included in the order.
     */
    @Override
    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void pay(User user, Order order, List<Item> items) {
        retryTemplate.execute(context -> {
            log.info("Retry number: {}", context.getRetryCount());
            super.pay(user, order, items);
            order.setOrderStatus(OrderStatus.FINISHED);
            orderDao.save(order);
            log.info("Payment done for order {}", order.getId());
            return null;
        }, context -> {
            order.setOrderStatus(OrderStatus.FAILED);
            orderDao.save(order);
            log.warn("Retry for payment for order {} failed", order.getId(), context.getLastThrowable());
            return null;
        });
    }
}
