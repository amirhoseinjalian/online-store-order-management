package com.jalian.online_store_order_management.service.impl;

import com.jalian.online_store_order_management.annotation.NotNull;
import com.jalian.online_store_order_management.annotation.Valid;
import com.jalian.online_store_order_management.dao.StoreDao;
import com.jalian.online_store_order_management.domain.Store;
import com.jalian.online_store_order_management.dto.AddStoreDto;
import com.jalian.online_store_order_management.exception.ConstraintViolationException;
import com.jalian.online_store_order_management.exception.EntityNotFoundException;
import com.jalian.online_store_order_management.service.StoreService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StoreServiceImpl implements StoreService {

    private final StoreDao storeDao;

    public StoreServiceImpl(StoreDao storeDao) {
        this.storeDao = storeDao;
    }

    @Override
    @Transactional
    @Valid
    public Long addStore(@NotNull AddStoreDto storeDto) {
        if (storeDao.existsByName(storeDto.name()))
            throw new ConstraintViolationException("Store name already exists");
        var store = Store.of(storeDto);
        return storeDao.save(store).getId();
    }

    @Override
    @Transactional
    @Valid
    public boolean existStore(@NotNull Long storeId) {
        return storeDao.existsById(storeId);
    }

    @Override
    @Transactional(readOnly = true)
    @Valid
    public Store findStore(@NotNull Long storeId) {
        return storeDao.findById(storeId)
                .orElseThrow(
                        () -> new EntityNotFoundException(Store.class.getSimpleName(), "id", storeId.toString())
                );
    }
}
