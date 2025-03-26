package com.jalian.online_store_order_management.domain;

import com.jalian.online_store_order_management.constant.OrderStatus;
import jakarta.persistence.*;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "orders")
public class Order extends BaseDomain {

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @ManyToMany(targetEntity = Product.class, fetch = FetchType.EAGER)
    @JoinTable(
            name = "order_product",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private List<Product> products;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User owner;

    public Order() {
    }

    public Order(OrderStatus orderStatus, List<Product> products, User owner) {
        this.orderStatus = orderStatus;
        this.products = products;
        this.owner = owner;
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
        sb.append(", products=").append(products);
        sb.append(", owner=").append(owner);
        sb.append('}');
        return sb.toString();
    }
}
