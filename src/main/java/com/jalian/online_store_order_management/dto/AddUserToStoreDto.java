package com.jalian.online_store_order_management.dto;

/**
 * The AddUserToStoreDto record encapsulates the data required to add a user to a store.
 * <p>
 * It contains the unique identifiers of the user and the store.
 * </p>
 *
 * @param userId  the unique identifier of the user to be added to the store.
 * @param storeId the unique identifier of the store to which the user will be added.
 *
 * @author amirhosein jalian
 */
public record AddUserToStoreDto(Long userId, Long storeId) {
}
