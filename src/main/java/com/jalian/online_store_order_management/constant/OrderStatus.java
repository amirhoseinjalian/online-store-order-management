package com.jalian.online_store_order_management.constant;

/**
 * The OrderStatus enum represents the various stages of an order's lifecycle within the system.
 * <p>
 * It includes statuses that reflect the order's progress from initialization to completion,
 * as well as potential outcomes such as cancellation or failure.
 * </p>
 * <ul>
 *   <li>{@code INITIALIZED} - The order has been created but not yet processed.</li>
 *   <li>{@code AWAITING_PAYMENT} - The order is pending payment confirmation.</li>
 *   <li>{@code FINISHED} - The order has been successfully completed.</li>
 *   <li>{@code CANCELLED} - The order has been cancelled.</li>
 *   <li>{@code FAILED} - The order processing has failed.</li>
 * </ul>
 *
 * @author amirhosein jalian
 */
public enum OrderStatus {

    INITIALIZED,
    AWAITING_PAYMENT,
    FINISHED,
    CANCELLED,
    FAILED
}
