package com.jalian.online_store_order_management.exception;

public class LackOfProductException extends RuntimeException {

    public LackOfProductException() {
        super("there is not enough product in stock");
    }
}
