package com.jalian.online_store_order_management.domain.key;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

/**
 * The ItemKey class represents a composite primary key for the {@code Item} entity.
 * <p>
 * It combines the order identifier and product identifier to uniquely identify an item within an order.
 * This key is marked as {@code @Embeddable} to be embedded in the {@code Item} entity.
 * </p>
 *
 * @author amirhosein jalian
 */
@Embeddable
public class ItemKey implements Serializable {

    /**
     * The identifier of the order.
     */
    @Column(name = "order_id", nullable = false)
    private Long orderId;

    /**
     * The identifier of the product.
     */
    @Column(name = "product_id", nullable = false)
    private Long productId;

    /**
     * Constructs an ItemKey with the specified order and product identifiers.
     *
     * @param orderId   the identifier of the order.
     * @param productId the identifier of the product.
     */
    public ItemKey(Long orderId, Long productId) {
        this.orderId = orderId;
        this.productId = productId;
    }

    /**
     * Default constructor required by JPA.
     */
    public ItemKey() {}

    /**
     * Gets the order identifier.
     *
     * @return the orderId.
     */
    public Long getOrderId() {
        return orderId;
    }

    /**
     * Sets the order identifier.
     *
     * @param orderId the orderId to set.
     */
    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    /**
     * Gets the product identifier.
     *
     * @return the productId.
     */
    public Long getProductId() {
        return productId;
    }

    /**
     * Sets the product identifier.
     *
     * @param productId the productId to set.
     */
    public void setProductId(Long productId) {
        this.productId = productId;
    }

    /**
     * Compares this ItemKey with the specified object for equality.
     * <p>
     * Two ItemKey objects are considered equal if their orderId and productId values are equal.
     * </p>
     *
     * @param o the object to compare with.
     * @return {@code true} if the objects are equal; {@code false} otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ItemKey that)) return false;
        return Objects.equals(getOrderId(), that.getOrderId()) && Objects.equals(getProductId(), that.getProductId());
    }

    /**
     * Returns the hash code value for this ItemKey.
     *
     * @return the hash code based on orderId and productId.
     */
    @Override
    public int hashCode() {
        return Objects.hash(getOrderId(), getProductId());
    }

    /**
     * Returns a string representation of the ItemKey.
     *
     * @return a string containing the orderId and productId.
     */
    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("ItemKey{");
        sb.append("orderId=").append(orderId);
        sb.append(", productId=").append(productId);
        sb.append('}');
        return sb.toString();
    }
}
