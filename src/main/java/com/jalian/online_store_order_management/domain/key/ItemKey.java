package com.jalian.online_store_order_management.domain.key;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ItemKey implements Serializable {

    @Column(name = "order_id", nullable = false)
    private Long orderId;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    public ItemKey(Long orderId, Long productId) {
        this.orderId = orderId;
        this.productId = productId;
    }
    public ItemKey() {}

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ItemKey that)) return false;
        return Objects.equals(getOrderId(), that.getOrderId()) && Objects.equals(getProductId(), that.getProductId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getOrderId(), getProductId());
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("OrderProductKey{");
        sb.append("orderId=").append(orderId);
        sb.append(", productId=").append(productId);
        sb.append('}');
        return sb.toString();
    }
}
