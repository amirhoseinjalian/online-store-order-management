package com.jalian.online_store_order_management.exception;

/**
 * The DuplicateUsername exception is thrown when an attempt is made to register a user with a username
 * that already exists in the system.
 * <p>
 * This exception provides a detailed message indicating the username that caused the conflict.
 * </p>
 *
 * @author amirhosein jalian
 */
public class DuplicateUsername extends Exception {

    /**
     * Constructs a new DuplicateUsername exception with a detail message based on the provided username.
     *
     * @param username the username that already exists.
     */
    public DuplicateUsername(String username) {
        super("Username " + username + " already exists");
    }
}
