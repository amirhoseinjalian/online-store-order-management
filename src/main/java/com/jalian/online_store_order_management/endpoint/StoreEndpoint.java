package com.jalian.online_store_order_management.endpoint;

import com.jalian.online_store_order_management.dto.AddStoreDto;
import com.jalian.online_store_order_management.service.StoreService;
import com.jalian.online_store_order_management.web.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stores")
public class StoreEndpoint {

    private final StoreService storeService;

    public StoreEndpoint(StoreService storeService) {
        this.storeService = storeService;
    }

    @PostMapping("/add")
    public ResponseEntity<BaseResponse<Long>> addStore(@RequestBody AddStoreDto addStoreDto) {
        return new ResponseEntity<>(
                new BaseResponse<>(storeService.addStore(addStoreDto), "Store added successfully"),
                HttpStatus.CREATED
        );
    }
}
