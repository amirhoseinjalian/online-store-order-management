package com.jalian.online_store_order_management.dto;

/**
 * The StoreFetchDto record is a data transfer object that encapsulates
 * the store details for retrieval operations.
 * <p>
 * It contains the unique name of the store.
 * </p>
 *
 * @param name the unique name of the store.
 *
 * @author amirhosein jalian
 */
public record StoreFetchDto(String name) {
}
