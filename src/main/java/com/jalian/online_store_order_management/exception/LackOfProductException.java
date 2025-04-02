package com.jalian.online_store_order_management.exception;

/**
 * The LackOfProductException is thrown when there is insufficient product stock to fulfill a requested operation.
 * <p>
 * This exception indicates that the product inventory is inadequate to meet the demand.
 * </p>
 *
 * @author amirhosein jalian
 */
public class LackOfProductException extends RuntimeException {

    /**
     * Constructs a new LackOfProductException with a default detail message.
     */
    public LackOfProductException() {
        super("there is not enough product in stock");
    }
}
