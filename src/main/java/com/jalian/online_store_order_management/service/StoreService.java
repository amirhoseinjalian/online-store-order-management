package com.jalian.online_store_order_management.service;

import com.jalian.online_store_order_management.domain.Store;
import com.jalian.online_store_order_management.dto.AddStoreDto;
import com.jalian.online_store_order_management.dto.AddUserToStoreDto;
import com.jalian.online_store_order_management.dto.UserFetchDto;

import java.util.List;

/**
 * The StoreService interface defines the operations for managing stores within the system.
 * <p>
 * It includes methods for adding a new store, checking store existence, retrieving a store by its unique identifier,
 * and verifying the relationship between a store and a user.
 * </p>
 *
 * @author amirhosein jalian
 */
public interface StoreService {

    /**
     * Adds a new store to the system.
     * <p>
     * This method creates a new store using the details provided in the {@link AddStoreDto} and returns the unique
     * identifier of the newly created store.
     * </p>
     *
     * @param storeDto the data transfer object containing the store details.
     * @return the unique identifier of the newly added store.
     */
    Long addStore(AddStoreDto storeDto);

    /**
     * Checks if a store exists in the system.
     * <p>
     * This method verifies the existence of a store by its unique identifier.
     * </p>
     *
     * @param storeId the unique identifier of the store.
     * @return {@code true} if the store exists; {@code false} otherwise.
     */
    boolean existStore(Long storeId);

    /**
     * Retrieves a store by its unique identifier.
     * <p>
     * This method returns the {@link Store} entity corresponding to the given storeId.
     * </p>
     *
     * @param storeId the unique identifier of the store.
     * @return the {@link Store} entity.
     */
    Store findStore(Long storeId);

    /**
     * Verifies whether a given user is associated with the specified store.
     * <p>
     * This method checks if the user identified by {@code userId} belongs to the store identified by {@code storeId}.
     * </p>
     *
     * @param storeId the unique identifier of the store.
     * @param userId  the unique identifier of the user.
     * @return {@code true} if the user belongs to the store; {@code false} otherwise.
     */
    boolean belongToStore(Long storeId, Long userId);

    /**
     * Adds a user to a store.
     * <p>
     * This method accepts an {@link com.jalian.online_store_order_management.dto.AddUserToStoreDto} which contains
     * the unique identifiers of the user and the store. The service will add the specified user to the store and
     * return an updated list of users associated with that store as {@link com.jalian.online_store_order_management.dto.UserFetchDto} objects.
     * </p>
     *
     * @param addUserToStoreDto the data transfer object containing the user ID and store ID.
     * @return a list of {@link com.jalian.online_store_order_management.dto.UserFetchDto} representing the updated set of users in the store.
     */
    List<UserFetchDto> addUserToStore(AddUserToStoreDto addUserToStoreDto);
}
