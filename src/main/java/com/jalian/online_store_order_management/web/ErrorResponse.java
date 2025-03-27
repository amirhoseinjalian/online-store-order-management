package com.jalian.online_store_order_management.web;

public class ErrorResponse extends BaseResponse<Long> {

    public ErrorResponse(Exception exception) {
        this(exception.getMessage());
    }

    public ErrorResponse(String message) {
        super(-1L, message);
    }

    public ErrorResponse(String message, Long code) {
        super(code, message);
    }
}
