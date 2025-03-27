package com.jalian.online_store_order_management.service;

import com.jalian.online_store_order_management.dto.UserFetchDto;
import com.jalian.online_store_order_management.dto.UserRegisterDto;
import com.jalian.online_store_order_management.exception.DuplicateUsername;
import com.jalian.online_store_order_management.exception.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;

public interface UserService {

    @Transactional
    long registerUser(UserRegisterDto userRegisterDto) throws DuplicateUsername;

    @Transactional(readOnly = true)
    UserFetchDto findUser(String username) throws EntityNotFoundException;

    @Transactional(readOnly = true)
    UserFetchDto findUserById(Long id) throws EntityNotFoundException;
}
