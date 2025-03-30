package com.jalian.online_store_order_management.exception;

public class IllegalBalanceException extends RuntimeException {
    public IllegalBalanceException() {
        super("There is not enough balance");
    }
}
