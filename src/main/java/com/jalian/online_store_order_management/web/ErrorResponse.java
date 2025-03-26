package com.jalian.online_store_order_management.web;

public class ErrorResponse<T> extends BaseResponse<T> {

    public ErrorResponse(T result, String message) {
        super(result, message);
    }
}
