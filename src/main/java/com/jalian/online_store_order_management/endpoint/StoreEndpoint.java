package com.jalian.online_store_order_management.endpoint;

import com.jalian.online_store_order_management.dto.AddStoreDto;
import com.jalian.online_store_order_management.service.StoreService;
import com.jalian.online_store_order_management.web.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
