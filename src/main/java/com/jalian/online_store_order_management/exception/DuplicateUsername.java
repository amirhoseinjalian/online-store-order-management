package com.jalian.online_store_order_management.exception;

public class DuplicateUsername extends Exception {

    public DuplicateUsername(String username) {
        super("Username " + username + " already exists");
    }
}
