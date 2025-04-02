package com.jalian.online_store_order_management.domain;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * The Auditable class is a mapped superclass that provides common auditing properties for entity classes.
 * <p>
 * It includes fields to track the creation and last modification timestamps as well as a version field for
 * optimistic locking. The {@code @CreatedDate} and {@code @LastModifiedDate} annotations automatically manage
 * these fields, while the {@code @Version} annotation supports versioning of entities.
 * </p>
 *
 * <p>
 * Subclasses of Auditable inherit these properties and are automatically monitored for audit changes.
 * </p>
 *
 * @author amirhosein jalian
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class Auditable {

    /**
     * The timestamp when the entity was created.
     * <p>
     * This field is set automatically on entity creation and is not updatable.
     * </p>
     */
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    /**
     * The timestamp when the entity was last updated.
     * <p>
     * This field is updated automatically when the entity is modified.
     * </p>
     */
    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime updatedAt;

    /**
     * The version field used for optimistic locking.
     * <p>
     * It is automatically incremented with each update to the entity.
     * </p>
     */
    @Version
    private Long version;

    /**
     * Gets the creation timestamp of the entity.
     *
     * @return the creation time.
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Sets the creation timestamp of the entity.
     *
     * @param createdAt the creation time to set.
     */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Gets the last modification timestamp of the entity.
     *
     * @return the last updated time.
     */
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    /**
     * Sets the last modification timestamp of the entity.
     *
     * @param updatedAt the updated time to set.
     */
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     * Gets the version number of the entity used for optimistic locking.
     *
     * @return the version number.
     */
    public Long getVersion() {
        return version;
    }

    /**
     * Sets the version number of the entity used for optimistic locking.
     *
     * @param version the version number to set.
     */
    public void setVersion(Long version) {
        this.version = version;
    }

    /**
     * Appends the auditing fields to the provided {@link StringBuffer}.
     * <p>
     * This helper method is typically used to include audit information in the {@code toString()} implementation
     * of entity classes.
     * </p>
     *
     * @param sb the {@link StringBuffer} to which the fields will be appended.
     */
    protected void appendFields(StringBuffer sb) {
        sb.append(", createdAt=").append(getCreatedAt());
        sb.append(", updatedAt=").append(getUpdatedAt());
    }
}