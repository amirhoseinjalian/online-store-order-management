package com.jalian.online_store_order_management.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Product extends BaseDomain {

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false)
    private double price;

    @Column(nullable = false)
    private long inventory;

    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    public Product() {
    }

    public Product(Store store, double price, String description, String name) {
        this.store = store;
        this.price = price;
        this.description = description;
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Product)) return false;
        return super.equals(o);
    }

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public long getInventory() {
        return inventory;
    }

    public void setInventory(long inventory) {
        this.inventory = inventory;
    }
}
