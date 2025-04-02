package com.jalian.online_store_order_management.domain;

import com.jalian.online_store_order_management.domain.key.ItemKey;
import jakarta.persistence.*;

import java.util.Objects;

/**
 * The Item class represents an individual item within an order.
 * <p>
 * It links a {@link Product} to an {@link Order} through a composite key defined in {@link ItemKey},
 * and stores additional details such as the count, price, and the present inventory at the time of ordering.
 * </p>
 *
 * @author amirhosein jalian
 */
@Entity
public class Item extends Auditable {

    /**
     * The composite primary key for the Item, consisting of product and order identifiers.
     */
    @EmbeddedId
    private ItemKey id;

    /**
     * The product associated with this item.
     * <p>
     * Uses eager fetching to load the product details along with the item.
     * </p>
     */
    @ManyToOne(targetEntity = Product.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", nullable = false)
    @MapsId("productId")
    private Product product;

    /**
     * The order to which this item belongs.
     * <p>
     * Uses eager fetching to load the order details along with the item.
     * </p>
     */
    @ManyToOne(targetEntity = Order.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id", nullable = false)
    @MapsId("orderId")
    private Order order;

    /**
     * The quantity of the product ordered.
     */
    private long count;

    /**
     * The price of the product at the time of ordering.
     */
    private double price;

    /**
     * The inventory count of the product at the time the order was placed.
     * <p>
     * This value is stored as a non-updatable column.
     * </p>
     */
    @Column(name = "present_inventory", nullable = false, updatable = false)
    private long presentInventory;

    /**
     * Default constructor for JPA.
     */
    public Item() {
    }

    /**
     * Constructs an Item with the specified details.
     *
     * @param primaryKey       the composite key linking the product and order.
     * @param product          the product associated with this item.
     * @param order            the order to which this item belongs.
     * @param count            the quantity of the product ordered.
     * @param presentInventory the product's inventory at the time of ordering.
     * @param price            the price of the product.
     */
    public Item(ItemKey primaryKey, Product product, Order order, long count, long presentInventory, double price) {
        this.id = primaryKey;
        this.product = product;
        this.order = order;
        this.count = count;
        this.presentInventory = presentInventory;
        this.price = price;
    }

    /**
     * Gets the composite primary key for the item.
     *
     * @return the primary key {@link ItemKey}.
     */
    public ItemKey getPrimaryKey() {
        return id;
    }

    /**
     * Sets the composite primary key for the item.
     *
     * @param pKey the primary key to set.
     */
    public void setPrimaryKey(ItemKey pKey) {
        this.id = pKey;
    }

    /**
     * Gets the product associated with this item.
     *
     * @return the {@link Product} of the item.
     */
    public Product getProduct() {
        return product;
    }

    /**
     * Sets the product associated with this item.
     *
     * @param product the product to set.
     */
    public void setProduct(Product product) {
        this.product = product;
    }

    /**
     * Gets the order to which this item belongs.
     *
     * @return the {@link Order} of the item.
     */
    public Order getOrder() {
        return order;
    }

    /**
     * Sets the order for this item.
     *
     * @param order the order to set.
     */
    public void setOrder(Order order) {
        this.order = order;
    }

    /**
     * Gets the quantity of the product ordered.
     *
     * @return the count of the product.
     */
    public long getCount() {
        return count;
    }

    /**
     * Sets the quantity of the product ordered.
     *
     * @param count the count to set.
     */
    public void setCount(long count) {
        this.count = count;
    }

    /**
     * Gets the price of the product at the time of ordering.
     *
     * @return the price of the product.
     */
    public double getPrice() {
        return price;
    }

    /**
     * Sets the price of the product.
     *
     * @param price the price to set.
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Gets the present inventory of the product when the order was placed.
     *
     * @return the present inventory count.
     */
    public long getPresentInventory() {
        return presentInventory;
    }

    /**
     * Sets the present inventory of the product.
     *
     * @param presentInventory the inventory count to set.
     */
    public void setPresentInventory(long presentInventory) {
        this.presentInventory = presentInventory;
    }

    /**
     * Determines whether another object is equal to this Item.
     * <p>
     * Two items are considered equal if they have the same composite primary key.
     * </p>
     *
     * @param o the object to compare with.
     * @return {@code true} if the objects are equal, {@code false} otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Item that)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(getPrimaryKey(), that.getPrimaryKey());
    }

    /**
     * Returns the hash code for this item, based on its composite primary key.
     *
     * @return the hash code.
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getPrimaryKey());
    }

    /**
     * Returns a string representation of the item, including its primary key, associated product, order,
     * count, and present inventory.
     *
     * @return a string representation of the item.
     */
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
