package com.jalian.online_store_order_management.domain;

import com.jalian.online_store_order_management.constant.OrderStatus;
import jakarta.persistence.*;

import java.util.Objects;

/**
 * The Order class represents an order in the system.
 * <p>
 * It extends {@link BaseDomain} to inherit common identifier and auditing properties.
 * An order is associated with a {@link User} as its owner and a {@link Store} where the order is placed.
 * It also maintains the current status of the order using the {@link OrderStatus} enum.
 * </p>
 *
 * @author amirhosein jalian
 */
@Entity
@Table(name = "orders")
public class Order extends BaseDomain {

    /**
     * The current status of the order.
     */
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    /**
     * The owner (user) of the order.
     * <p>
     * This association is eagerly fetched.
     * </p>
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User owner;

    /**
     * The store associated with the order.
     * <p>
     * This association is eagerly fetched.
     * </p>
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    /**
     * Default constructor for JPA.
     */
    public Order() {
    }

    /**
     * Constructs an Order with the specified status, owner, and store.
     *
     * @param orderStatus the status of the order.
     * @param owner       the user who owns the order.
     * @param store       the store where the order is placed.
     */
    public Order(OrderStatus orderStatus, User owner, Store store) {
        this.orderStatus = orderStatus;
        this.owner = owner;
        this.store = store;
    }

    /**
     * Determines whether another object is equal to this Order.
     * <p>
     * Two orders are considered equal if they have the same base properties (from {@link BaseDomain})
     * and the same {@code orderStatus}.
     * </p>
     *
     * @param o the object to compare with.
     * @return {@code true} if the objects are equal; {@code false} otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Order order)) return false;
        if (!super.equals(o)) return false;
        return orderStatus == order.orderStatus;
    }

    /**
     * Returns the hash code value for this Order.
     *
     * @return the hash code based on the base properties and the {@code orderStatus}.
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), orderStatus);
    }

    /**
     * Returns a string representation of the Order.
     *
     * @return a string that includes the order's fields, such as status and owner.
     */
    @Override
    public String toString() {
        final var sb = new StringBuffer("Order{");
        appendFields(sb);
        sb.append(", orderStatus=").append(orderStatus);
        sb.append(", owner=").append(owner);
        sb.append('}');
        return sb.toString();
    }

    /**
     * Gets the current status of the order.
     *
     * @return the order status.
     */
    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    /**
     * Sets the status of the order.
     *
     * @param orderStatus the order status to set.
     */
    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    /**
     * Gets the owner (user) of the order.
     *
     * @return the owner of the order.
     */
    public User getOwner() {
        return owner;
    }

    /**
     * Sets the owner (user) of the order.
     *
     * @param owner the user to set as the owner.
     */
    public void setOwner(User owner) {
        this.owner = owner;
    }

    /**
     * Gets the store associated with the order.
     *
     * @return the store of the order.
     */
    public Store getStore() {
        return store;
    }

    /**
     * Sets the store associated with the order.
     *
     * @param store the store to associate with the order.
     */
    public void setStore(Store store) {
        this.store = store;
    }
}
