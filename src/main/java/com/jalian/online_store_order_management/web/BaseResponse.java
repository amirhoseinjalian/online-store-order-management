package com.jalian.online_store_order_management.web;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * The BaseResponse class encapsulates a standard structure for API responses.
 * <p>
 * It contains a result of type {@code T}, a message detailing the response, and a timestamp indicating when
 * the response was generated. This structure helps maintain consistency in responses across the application.
 * </p>
 *
 * @param <T> the type of the result contained in the response.
 *
 * @author amirhosein jalian
 */
public class BaseResponse<T> {

    private final T result;
    private final String message;
    private final LocalDateTime timestamp;

    /**
     * Constructs a new BaseResponse with the specified result and message.
     * <p>
     * The timestamp is automatically set to the current time.
     * </p>
     *
     * @param result  the result to be returned in the response.
     * @param message a message describing the response.
     */
    public BaseResponse(T result, String message) {
        this.result = result;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    /**
     * Returns a string representation of this BaseResponse.
     *
     * @return a string representation of this BaseResponse.
     */
    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("BaseResponse{");
        sb.append("result=").append(result);
        sb.append(", message='").append(message).append('\'');
        sb.append(", timestamp=").append(timestamp);
        sb.append('}');
        return sb.toString();
    }

    /**
     * Checks if this BaseResponse is equal to another object.
     *
     * @param o the object to compare with.
     * @return {@code true} if this BaseResponse is equal to the given object, {@code false} otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof BaseResponse<?> that)) return false;
        return Objects.equals(getResult(), that.getResult()) &&
                Objects.equals(getMessage(), that.getMessage()) &&
                Objects.equals(getTimestamp(), that.getTimestamp());
    }

    /**
     * Returns the hash code value for this BaseResponse.
     *
     * @return the hash code value.
     */
    @Override
    public int hashCode() {
        return Objects.hash(getResult(), getMessage(), getTimestamp());
    }

    /**
     * Gets the result contained in the response.
     *
     * @return the result.
     */
    public T getResult() {
        return result;
    }

    /**
     * Gets the message associated with the response.
     *
     * @return the message.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Gets the timestamp when the response was created.
     *
     * @return the timestamp.
     */
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
