package com.jalian.online_store_order_management.exception;

/**
 * The ValidationException is thrown when a validation error occurs.
 * <p>
 * This exception is used to indicate that input data or parameters do not meet the required validation criteria.
 * </p>
 *
 * @author amirhosein jalian
 */
public class ValidationException extends RuntimeException {

    /**
     * Constructs a new ValidationException with the specified detail message.
     *
     * @param message the detail message explaining the validation error.
     */
    public ValidationException(String message) {
        super(message);
    }
}
