package com.jalian.online_store_order_management.dto;

/**
 * The AddStoreDto record is a data transfer object that encapsulates the information
 * required to create a new store.
 * <p>
 * It contains the unique name of the store.
 * </p>
 *
 * @param name the unique name of the store.
 *
 * @author amirhosein jalian
 */
public record AddStoreDto(String name) {
}
