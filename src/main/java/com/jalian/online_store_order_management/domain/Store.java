package com.jalian.online_store_order_management.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import java.util.Objects;

@Entity
public class Store extends BaseDomain {

    @Column(unique = true, nullable = false)
    private String name;

    public Store(String name) {
        this.name = name;
    }

    public Store() {}

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Store store)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(name, store.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getName());
    }

    @Override
    public String toString() {
        final var sb = new StringBuffer("Store{");
        appendFields(sb);
        sb.append(", name='").append(name).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
