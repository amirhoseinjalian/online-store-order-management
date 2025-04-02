package com.jalian.online_store_order_management.dto;

import com.jalian.online_store_order_management.constant.BalanceOperation;

/**
 * The UpdateBalanceDto record is a data transfer object that encapsulates
 * the information required to update a user's balance.
 * <p>
 * It includes the user's unique identifier, the amount by which the balance is to be updated,
 * and the operation to be performed (e.g., addition or subtraction).
 * </p>
 *
 * @param userId    the unique identifier of the user whose balance is to be updated.
 * @param amount    the amount by which the user's balance will be adjusted.
 * @param operation the type of balance operation to perform, represented by {@link BalanceOperation}.
 *
 * @author amirhosein jalian
 */
public record UpdateBalanceDto(Long userId, double amount, BalanceOperation operation) {
}
