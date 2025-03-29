package com.jalian.online_store_order_management.service;

import com.jalian.online_store_order_management.domain.Store;
import com.jalian.online_store_order_management.dto.AddStoreDto;

public interface StoreService {

    Long addStore(AddStoreDto storeDto);

    boolean existStore(Long storeId);

    Store findStore(Long storeId);
}
