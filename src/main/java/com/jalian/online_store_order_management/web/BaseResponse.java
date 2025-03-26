package com.jalian.online_store_order_management.web;

import java.time.LocalDateTime;
import java.util.Objects;

public class BaseResponse<T> {

    private final T result;
    private final String message;
    private final LocalDateTime timestamp;

    public BaseResponse(T result, String message) {
        this.result = result;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("BaseResponse{");
        sb.append("result=").append(result);
        sb.append(", message='").append(message).append('\'');
        sb.append(", timestamp=").append(timestamp);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof BaseResponse<?> that)) return false;
        return Objects.equals(getResult(), that.getResult()) && Objects.equals(getMessage(), that.getMessage()) && Objects.equals(getTimestamp(), that.getTimestamp());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getResult(), getMessage(), getTimestamp());
    }

    public T getResult() {
        return result;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
