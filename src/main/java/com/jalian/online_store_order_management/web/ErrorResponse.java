package com.jalian.online_store_order_management.web;

/**
 * The ErrorResponse class represents a standard error response structure for the API.
 * <p>
 * It extends {@link BaseResponse} with a fixed result type of {@code Long} (typically used as an error code),
 * where a negative result (e.g., -1) indicates an error. It provides constructors for creating an error response
 * based on an exception or a custom message.
 * </p>
 *
 * @author amirhosein jalian
 */
public class ErrorResponse extends BaseResponse<Long> {

    /**
     * Constructs a new ErrorResponse using the message from the provided exception.
     *
     * @param exception the exception whose message will be used as the error message.
     */
    public ErrorResponse(Exception exception) {
        this(exception.getMessage());
    }

    /**
     * Constructs a new ErrorResponse with the specified error message.
     * <p>
     * The result code is set to -1 to indicate an error.
     * </p>
     *
     * @param message the error message.
     */
    public ErrorResponse(String message) {
        super(-1L, message);
    }

    /**
     * Constructs a new ErrorResponse with the specified error message and custom error code.
     *
     * @param message the error message.
     * @param code    the custom error code.
     */
    public ErrorResponse(String message, Long code) {
        super(code, message);
    }
}
