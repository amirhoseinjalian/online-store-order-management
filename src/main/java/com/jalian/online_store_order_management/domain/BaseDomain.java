package com.jalian.online_store_order_management.domain;

import jakarta.persistence.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Objects;

/**
 * The BaseDomain class is a mapped superclass that provides a common identifier field for domain entities,
 * in addition to inheriting auditing properties from {@link Auditable}.
 * <p>
 * This class defines a primary key field {@code id} and implements common methods such as {@code equals},
 * {@code hashCode}, and {@code toString()} based on the {@code id}. It ensures that all domain entities extending
 * this class will have a consistent identifier and auditing information.
 * </p>
 *
 * @author amirhosein jalian
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseDomain extends Auditable {

    /**
     * The primary key identifier for the entity.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Compares this object with the specified object for equality based on the {@code id} field.
     *
     * @param o the object to be compared for equality.
     * @return {@code true} if the given object represents a BaseDomain with the same id, {@code false} otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof BaseDomain that)) return false;
        return Objects.equals(id, that.id);
    }

    /**
     * Returns the hash code value for the object, based on the {@code id} field.
     *
     * @return the hash code value.
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    /**
     * Returns a string representation of the object, including the id and auditing fields.
     *
     * @return a string representation of the BaseDomain.
     */
    @Override
    public String toString() {
        final var sb = new StringBuffer("BaseDomain{");
        appendFields(sb);
        sb.append('}');
        return sb.toString();
    }

    /**
     * Appends the fields of this class and its superclass to the provided {@link StringBuffer}.
     * <p>
     * This method is typically used to build the string representation of the entity.
     * </p>
     *
     * @param sb the {@link StringBuffer} to which the fields will be appended.
     */
    protected void appendFields(StringBuffer sb) {
        sb.append("id=").append(id);
        super.appendFields(sb);
    }

    /**
     * Gets the primary key identifier of the entity.
     *
     * @return the id of the entity.
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the primary key identifier of the entity.
     *
     * @param id the id to set.
     */
    public void setId(Long id) {
        this.id = id;
    }
}
