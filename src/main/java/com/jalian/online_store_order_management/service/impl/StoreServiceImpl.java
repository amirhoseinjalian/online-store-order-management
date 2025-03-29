package com.jalian.online_store_order_management.service.impl;

import com.jalian.online_store_order_management.annotation.NotNull;
import com.jalian.online_store_order_management.annotation.Valid;
import com.jalian.online_store_order_management.dao.StoreDao;
import com.jalian.online_store_order_management.domain.Store;
import com.jalian.online_store_order_management.dto.AddStoreDto;
import com.jalian.online_store_order_management.exception.ConstraintViolationException;
import com.jalian.online_store_order_management.exception.EntityNotFoundException;
import com.jalian.online_store_order_management.service.StoreService;
import com.jalian.online_store_order_management.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StoreServiceImpl implements StoreService {

    private final StoreDao storeDao;
    private final UserService userService;

    public StoreServiceImpl(StoreDao storeDao, UserService userService) {
        this.storeDao = storeDao;
        this.userService = userService;
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
    @Transactional
    @Valid
    public Store findStore(@NotNull Long storeId) {
        return findStoreInternal(storeId);
    }

    private Store findStoreInternal(Long storeId) {
        if (!storeDao.existsById(storeId)) {
            throw new EntityNotFoundException(Store.class.getSimpleName(), "id", storeId.toString());
        }
        return storeDao.findByIdSafe(storeId);
    }

    @Override
    @Transactional
    public boolean belongToStore(Long storeId, Long userId) {
        var store = findStoreInternal(storeId);
        if (store.getUsers() == null || store.getUsers().isEmpty()) {
            return false;
        }
        var user = userService.findUserEntityById(userId);
        return store.getUsers().contains(user);
    }
}
