package com.jalian.online_store_order_management.domain;

import com.jalian.online_store_order_management.constant.OrderStatus;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "orders")
public class Order extends BaseDomain {

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User owner;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    public Order() {
    }

    public Order(OrderStatus orderStatus, User owner, Store store) {
        this.orderStatus = orderStatus;
        this.owner = owner;
        this.store = store;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Order order)) return false;
        if (!super.equals(o)) return false;
        return orderStatus == order.orderStatus;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), orderStatus);
    }

    @Override
    public String toString() {
        final var sb = new StringBuffer("Order{");
        appendFields(sb);
        sb.append(", orderStatus=").append(orderStatus);
        sb.append(", owner=").append(owner);
        sb.append('}');
        return sb.toString();
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }
}
