package com.jalian.online_store_order_management.service.impl;

import com.jalian.online_store_order_management.annotation.NotNull;
import com.jalian.online_store_order_management.annotation.Valid;
import com.jalian.online_store_order_management.dao.StoreDao;
import com.jalian.online_store_order_management.dao.UserDao;
import com.jalian.online_store_order_management.domain.Store;
import com.jalian.online_store_order_management.dto.AddStoreDto;
import com.jalian.online_store_order_management.dto.AddUserToStoreDto;
import com.jalian.online_store_order_management.dto.UserFetchDto;
import com.jalian.online_store_order_management.exception.ConstraintViolationException;
import com.jalian.online_store_order_management.exception.EntityNotFoundException;
import com.jalian.online_store_order_management.service.StoreService;
import com.jalian.online_store_order_management.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
        if (storeDao.findByNameSafe(storeDto.name()).isPresent())
            throw new ConstraintViolationException("Store name already exists");
        var store = Store.of(storeDto);
        return storeDao.save(store).getId();
    }

    @Override
    @Transactional(readOnly = true)
    @Valid
    public boolean existStore(@NotNull Long storeId) {
        return storeDao.findByIdSafe(storeId).isPresent();
    }

    @Override
    @Transactional(readOnly = true)
    @Valid
    public Store findStore(@NotNull Long storeId) {
        return findStoreInternal(storeId);
    }

    private Store findStoreInternal(Long storeId) {
        var store = storeDao.findByIdSafe(storeId);
        if (store.isEmpty()) {
            throw new EntityNotFoundException(Store.class.getSimpleName(), "id", storeId.toString());
        }
        return store.get();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean belongToStore(Long storeId, Long userId) {
        var store = findStoreInternal(storeId);
        if (store.getUsers() == null || store.getUsers().isEmpty()) {
            return false;
        }
        var user = userService.findUserEntityById(userId);
        return store.getUsers().contains(user);
    }

    @Override
    @Transactional
    public List<UserFetchDto> addUserToStore(AddUserToStoreDto addUserToStoreDto) {
        var store = findStoreInternal(addUserToStoreDto.storeId());
        var user = userService.findUserEntityById(addUserToStoreDto.userId());
        var storeUsers = store.getUsers();
        storeUsers.add(user);
        store.setUsers(storeUsers);
        storeDao.save(store);
        return UserFetchDto.of(store.getUsers());
    }
}
