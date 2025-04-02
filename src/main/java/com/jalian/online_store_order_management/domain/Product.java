package com.jalian.online_store_order_management.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

/**
 * The Product class represents a product available in the online store.
 * <p>
 * It extends {@link BaseDomain} to inherit common identifier and auditing properties.
 * A product has a name, description, price, inventory, and is associated with a specific store.
 * </p>
 *
 * @author amirhosein jalian
 */
@Entity
public class Product extends BaseDomain {

    /**
     * The name of the product.
     * <p>
     * This field is mandatory.
     * </p>
     */
    @Column(nullable = false)
    private String name;

    /**
     * A brief description of the product.
     */
    private String description;

    /**
     * The price of the product.
     * <p>
     * This field is mandatory.
     * </p>
     */
    @Column(nullable = false)
    private double price;

    /**
     * The available inventory count of the product.
     * <p>
     * This field is mandatory.
     * </p>
     */
    @Column(nullable = false)
    private long inventory;

    /**
     * The store that offers this product.
     * <p>
     * This association is mandatory.
     * </p>
     */
    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    /**
     * Default constructor initializing inventory to zero.
     */
    public Product() {
        inventory = 0L;
    }

    /**
     * Constructs a new Product with the specified details.
     *
     * @param store       the store associated with the product.
     * @param price       the price of the product.
     * @param description a description of the product.
     * @param name        the name of the product.
     */
    public Product(Store store, double price, String description, String name) {
        this.store = store;
        this.price = price;
        this.description = description;
        this.name = name;
        this.inventory = 0;
    }

    /**
     * Determines whether another object is equal to this Product.
     * <p>
     * Equality is based on the identifier defined in {@link BaseDomain}.
     * </p>
     *
     * @param o the object to compare with.
     * @return {@code true} if the objects are equal; {@code false} otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Product)) return false;
        return super.equals(o);
    }

    /**
     * Returns a string representation of the Product.
     *
     * @return a string that includes the product's fields.
     */
    @Override
    public String toString() {
        final var sb = new StringBuffer("Product{");
        appendFields(sb);
        sb.append(", name='").append(name).append('\'');
        sb.append(", description='").append(description).append('\'');
        sb.append(", price=").append(price);
        sb.append(", inventory=").append(inventory);
        if (store != null)
            sb.append(", store=").append(store.getId());
        sb.append('}');
        return sb.toString();
    }

    /**
     * Gets the product description.
     *
     * @return the description of the product.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the product description.
     *
     * @param description the description to set.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the name of the product.
     *
     * @return the product name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the product.
     *
     * @param name the product name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the price of the product.
     *
     * @return the product price.
     */
    public double getPrice() {
        return price;
    }

    /**
     * Sets the price of the product.
     *
     * @param price the product price to set.
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Gets the store associated with the product.
     *
     * @return the {@link Store} that offers the product.
     */
    public Store getStore() {
        return store;
    }

    /**
     * Sets the store associated with the product.
     *
     * @param store the {@link Store} to set.
     */
    public void setStore(Store store) {
        this.store = store;
    }

    /**
     * Gets the available inventory of the product.
     *
     * @return the product inventory.
     */
    public long getInventory() {
        return inventory;
    }

    /**
     * Sets the available inventory of the product.
     *
     * @param inventory the inventory count to set.
     */
    public void setInventory(long inventory) {
        this.inventory = inventory;
    }
}
