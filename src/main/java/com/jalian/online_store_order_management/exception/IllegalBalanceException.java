package com.jalian.online_store_order_management.exception;

/**
 * The IllegalBalanceException is thrown when an operation is attempted that results in an insufficient balance.
 * <p>
 * This exception indicates that there is not enough balance available to perform the requested operation.
 * </p>
 *
 * @author amirhosein jalian
 */
public class IllegalBalanceException extends RuntimeException {

    /**
     * Constructs a new IllegalBalanceException with a default detail message.
     */
    public IllegalBalanceException() {
        super("There is not enough balance");
    }
}
