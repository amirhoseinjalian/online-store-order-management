package com.jalian.online_store_order_management.dto;

import com.jalian.online_store_order_management.domain.User;

public record UserFetchDto(String firstName, String lastName, String email, String id, String username, double balance) {

    public static UserFetchDto of(User user) {
        return new UserFetchDto(
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getId().toString(),
                user.getUsername(),
                user.getBalance()
        );
    }
}
