package com.jalian.online_store_order_management.exception;

/**
 * The EntityNotFoundException is thrown when a requested entity cannot be found in the system.
 * <p>
 * It provides constructors for creating an exception with a custom message or with details
 * about the entity that was not found.
 * </p>
 *
 * @author amirhosein jalian
 */
public class EntityNotFoundException extends RuntimeException {

    /**
     * Constructs a new EntityNotFoundException with a detailed message constructed from the specified parameters.
     *
     * @param entityName  the name of the entity that was not found.
     * @param findElement the element used to search for the entity.
     * @param elementType the type of the search element.
     */
    public EntityNotFoundException(String entityName, String findElement, String elementType) {
        super("Could not find " + entityName + " with " + findElement + " " + elementType);
    }

    /**
     * Constructs a new EntityNotFoundException with the specified detail message.
     *
     * @param message the detail message explaining the exception.
     */
    public EntityNotFoundException(String message) {
        super(message);
    }
}
