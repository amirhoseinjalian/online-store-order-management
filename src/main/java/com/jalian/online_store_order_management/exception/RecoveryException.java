package com.jalian.online_store_order_management.exception;

/**
 * The RecoveryException is thrown when an error occurs during a recovery process.
 * <p>
 * This exception encapsulates a custom message that describes the specific recovery issue.
 * </p>
 *
 * @author amirhosein jalian
 */
public class RecoveryException extends RuntimeException {

    /**
     * Constructs a new RecoveryException with the specified detail message.
     *
     * @param message the detail message explaining the recovery error.
     */
    public RecoveryException(String message) {
        super(message);
    }
}
