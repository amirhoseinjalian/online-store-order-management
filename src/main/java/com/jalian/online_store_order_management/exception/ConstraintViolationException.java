package com.jalian.online_store_order_management.exception;

/**
 * The ConstraintViolationException is a custom runtime exception that is thrown when a constraint violation occurs,
 * such as when input data fails to meet predefined validation rules.
 * <p>
 * This exception encapsulates an error message that describes the nature of the constraint violation.
 * </p>
 *
 * @author amirhosein jalian
 */
public class ConstraintViolationException extends RuntimeException {

    /**
     * Constructs a new ConstraintViolationException with the specified detail message.
     *
     * @param message the detail message explaining the constraint violation.
     */
    public ConstraintViolationException(String message) {
        super(message);
    }
}
