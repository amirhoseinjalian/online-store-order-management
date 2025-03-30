package com.jalian.online_store_order_management.domain;

import com.jalian.online_store_order_management.domain.key.ItemKey;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
public class Item extends Auditable {

    @EmbeddedId
    private ItemKey id;

    @ManyToOne(targetEntity = Product.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", nullable = false)
    @MapsId("productId")
    private Product product;

    @ManyToOne(targetEntity = Order.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id", nullable = false)
    @MapsId("orderId")
    private Order order;

    private long count;

    @Column(name = "present_inventory", nullable = false, updatable = false)
    private long presentInventory;

    public Item() {
    }

    public Item(ItemKey primaryKey, Product product, Order order, long count, long presentInventory) {
        this.id = primaryKey;
        this.product = product;
        this.order = order;
        this.count = count;
        this.presentInventory = presentInventory;
    }

    public ItemKey getPrimaryKey() {
        return id;
    }

    public void setPrimaryKey(ItemKey pKey) {
        this.id = pKey;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public long getPresentInventory() {
        return presentInventory;
    }

    public void setPresentInventory(long presentInventory) {
        this.presentInventory = presentInventory;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Item that)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(getPrimaryKey(), that.getPrimaryKey());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getPrimaryKey());
    }

    @Override
    public String toString() {
        final var sb = new StringBuffer("OrderProduct{");
        appendFields(sb);
        sb.append(", primaryKey=").append(id);
        sb.append(", product=").append(product);
        sb.append(", order=").append(order);
        sb.append(", count=").append(count);
        sb.append(", presentInventory=").append(presentInventory);
        sb.append('}');
        return sb.toString();
    }
}
