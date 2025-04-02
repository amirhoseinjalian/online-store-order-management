package com.jalian.online_store_order_management.domain;

import com.jalian.online_store_order_management.dto.AddStoreDto;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * The Store class represents a physical or virtual store within the online store order management system.
 * <p>
 * It extends {@link BaseDomain} to inherit common identifier and auditing properties. A store is uniquely identified
 * by its name and is associated with a list of users.
 * </p>
 *
 * @author amirhosein jalian
 */
@Entity
public class Store extends BaseDomain {

    /**
     * The unique name of the store.
     */
    @Column(unique = true, nullable = false)
    private String name;

    /**
     * The list of users associated with the store.
     * <p>
     * This is a many-to-many relationship, where the association is eagerly fetched.
     * </p>
     */
    @ManyToMany(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinTable(
            name = "store_user",
            joinColumns = @JoinColumn(name = "store_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> users;

    /**
     * Constructs a Store with the specified name.
     *
     * @param name the unique name of the store.
     */
    public Store(String name) {
        this.name = name;
    }

    /**
     * Default constructor required by JPA.
     */
    public Store() {}

    /**
     * Creates a new Store instance from the provided {@link AddStoreDto}.
     * <p>
     * This method maps the data transfer object to a Store entity.
     * </p>
     *
     * @param addStoreDto the DTO containing store details.
     * @return a new Store instance.
     */
    public static Store of(AddStoreDto addStoreDto) {
        var store = new Store();
        store.name = addStoreDto.name();
        store.users = new ArrayList<>();
        return store;
    }

    /**
     * Compares this Store with the specified object for equality.
     * <p>
     * Two stores are considered equal if they have the same base properties (from {@link BaseDomain})
     * and the same name.
     * </p>
     *
     * @param o the object to compare with.
     * @return {@code true} if the stores are equal; {@code false} otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Store store)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(name, store.getName());
    }

    /**
     * Returns the hash code value for this Store.
     *
     * @return the hash code based on the base properties and the store name.
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getName());
    }

    /**
     * Returns a string representation of the Store.
     *
     * @return a string that includes the store's fields, such as the name.
     */
    @Override
    public String toString() {
        final var sb = new StringBuffer("Store{");
        appendFields(sb);
        sb.append(", name='").append(name).append('\'');
        sb.append('}');
        return sb.toString();
    }

    /**
     * Gets the name of the store.
     *
     * @return the store name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the store.
     *
     * @param name the store name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the list of users associated with the store.
     *
     * @return the list of users.
     */
    public List<User> getUsers() {
        return users;
    }

    /**
     * Sets the list of users associated with the store.
     *
     * @param users the list of users to set.
     */
    public void setUsers(List<User> users) {
        this.users = users;
    }
}
