package com.jalian.online_store_order_management.exception;

public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(String entityName, String findElement, String elementType) {
        super("Could not find " + entityName + " with " + findElement + " " + elementType);
    }

    public EntityNotFoundException(String message) {
        super(message);
    }
}
