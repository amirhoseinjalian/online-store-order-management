package com.jalian.online_store_order_management.endpoint;

import com.jalian.online_store_order_management.dto.AddStoreDto;
import com.jalian.online_store_order_management.dto.AddUserToStoreDto;
import com.jalian.online_store_order_management.dto.UserFetchDto;
import com.jalian.online_store_order_management.service.StoreService;
import com.jalian.online_store_order_management.web.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * The StoreEndpoint class provides REST endpoints for managing stores in the system.
 * <p>
 * It supports creating a new store by accepting store details via a data transfer object.
 * </p>
 *
 * @author amirhosein jalian
 */
@RestController
@RequestMapping("/stores")
public class StoreEndpoint {

    private final StoreService storeService;

    /**
     * Constructs a new StoreEndpoint with the specified StoreService.
     *
     * @param storeService the service used for store operations.
     */
    public StoreEndpoint(StoreService storeService) {
        this.storeService = storeService;
    }

    /**
     * Adds a new store to the system.
     *
     * @param addStoreDto the data transfer object containing store details.
     * @return a ResponseEntity containing a BaseResponse with the created store ID and a success message.
     */
    @PostMapping("/add")
    public ResponseEntity<BaseResponse<Long>> addStore(@RequestBody AddStoreDto addStoreDto) {
        return new ResponseEntity<>(
                new BaseResponse<>(storeService.addStore(addStoreDto), "Store added successfully"),
                HttpStatus.CREATED
        );
    }

    /**
     * Endpoint for adding a user to a store.
     * <p>
     * This endpoint accepts an {@link com.jalian.online_store_order_management.dto.AddUserToStoreDto} in the request body,
     * which contains the unique identifiers of the user and the store. It delegates the operation to the storeService,
     * which adds the user to the specified store and returns an updated list of users represented as
     * {@link com.jalian.online_store_order_management.dto.UserFetchDto}.
     * </p>
     *
     * @param addUserToStoreDto the data transfer object containing the user ID and store ID.
     * @return a {@link org.springframework.http.ResponseEntity} containing a {@link com.jalian.online_store_order_management.web.BaseResponse}
     *         with the updated list of users and a success message, and an HTTP status of OK.
     */
    @PutMapping("/add-user")
    public ResponseEntity<BaseResponse<List<UserFetchDto>>> addStore(@RequestBody AddUserToStoreDto addUserToStoreDto) {
        return new ResponseEntity<>(
                new BaseResponse<>(storeService.addUserToStore(addUserToStoreDto), "User added to store successfully"),
                HttpStatus.OK
        );
    }
}
