package com.jalian.online_store_order_management.dto;

import com.jalian.online_store_order_management.constant.OrderStatus;
import com.jalian.online_store_order_management.domain.Item;
import com.jalian.online_store_order_management.domain.Order;

import java.util.List;

/**
 * The OrderFetchDto record is a data transfer object that represents detailed information
 * about an order, including its ID, owner, store, status, and the list of items in the order.
 * <p>
 * It is typically used when fetching order details to be returned to clients.
 * </p>
 *
 * @param orderId the unique identifier of the order.
 * @param owner   the user who placed the order.
 * @param store   the store associated with the order.
 * @param status  the current status of the order.
 * @param items   the list of items included in the order.
 *
 * @author amirhosein jalian
 */
public record OrderFetchDto(
        Long orderId,
        UserFetchDto owner,
        StoreFetchDto store,
        OrderStatus status,
        List<ItemFetchDto> items
) {

    /**
     * Creates an instance of {@code OrderFetchDto} from an {@link Order} entity and its associated items.
     *
     * @param order the order entity to map.
     * @param items the list of items belonging to the order.
     * @return a fully populated {@code OrderFetchDto}.
     */
    public static OrderFetchDto of(Order order, List<Item> items) {
        return new OrderFetchDto(
                order.getId(),
                UserFetchDto.of(order.getOwner()),
                new StoreFetchDto(order.getStore().getName()),
                order.getOrderStatus(),
                ItemFetchDto.of(items)
        );
    }
}
