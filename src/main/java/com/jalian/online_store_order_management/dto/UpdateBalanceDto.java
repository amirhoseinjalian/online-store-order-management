package com.jalian.online_store_order_management.dto;

import com.jalian.online_store_order_management.constant.BalanceOperation;

public record UpdateBalanceDto(Long userId, double amount, BalanceOperation operation) {}
