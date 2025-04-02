package com.jalian.online_store_order_management.service.impl;

import com.jalian.online_store_order_management.constant.BalanceOperation;
import com.jalian.online_store_order_management.domain.Item;
import com.jalian.online_store_order_management.domain.Order;
import com.jalian.online_store_order_management.domain.User;
import com.jalian.online_store_order_management.dto.UpdateBalanceDto;
import com.jalian.online_store_order_management.service.PayService;
import com.jalian.online_store_order_management.service.UserService;

import java.util.List;

/**
 * The AbstractPayService class provides a base implementation of the {@link PayService} interface.
 * <p>
 * It is a sealed abstract class that permits only {@link SyncPayServiceImpl} and {@link ASyncPayServiceImpl}
 * as its subclasses. This class implements the common functionality for processing payments by calculating
 * the final price of the items in an order and updating the user's balance accordingly.
 * </p>
 * <p>
 * The payment process involves deducting the total cost (calculated as the sum of the product of each item's count
 * and price) from the user's balance using the {@link UserService#updateBalance(UpdateBalanceDto)} method.
 * </p>
 *
 * @author amirhosein jalian
 */
public sealed abstract class AbstractPayService implements PayService
        permits SyncPayServiceImpl, ASyncPayServiceImpl {

    private final UserService userService;

    /**
     * Constructs an AbstractPayService with the specified {@link UserService}.
     *
     * @param userService the service used for updating user balances.
     */
    public AbstractPayService(UserService userService) {
        this.userService = userService;
    }

    /**
     * Processes the payment for an order.
     * <p>
     * This method calculates the final price of the order by summing up the product of the count and price for each item,
     * then updates the user's balance by deducting the total amount using the {@link BalanceOperation#MINUS} operation.
     * </p>
     *
     * @param user  the user who is making the payment.
     * @param order the order for which the payment is being processed.
     * @param items the list of items included in the order.
     */
    @Override
    public void pay(User user, Order order, List<Item> items) {
        var finalPrice = calculatePrice(items);
        userService.updateBalance(new UpdateBalanceDto(user.getId(), finalPrice, BalanceOperation.MINUS));
    }

    /**
     * Calculates the total price of a list of items.
     * <p>
     * The price is computed as the sum of the product of each item's count and the price of its associated product.
     * </p>
     *
     * @param items the list of items to calculate the total price for.
     * @return the total calculated price.
     */
    protected double calculatePrice(List<Item> items) {
        double[] sum = {0.0};
        items.forEach(item -> sum[0] += item.getCount() * item.getProduct().getPrice());
        return sum[0];
    }
}
