package com.jalian.online_store_order_management.service;

import com.jalian.online_store_order_management.domain.User;
import com.jalian.online_store_order_management.dto.UserFetchDto;
import com.jalian.online_store_order_management.dto.UserRegisterDto;
import com.jalian.online_store_order_management.exception.DuplicateUsername;
import com.jalian.online_store_order_management.exception.EntityNotFoundException;

public interface UserService {

    long registerUser(UserRegisterDto userRegisterDto) throws DuplicateUsername;
    UserFetchDto findUser(String username) throws EntityNotFoundException;
    UserFetchDto findUserById(Long id) throws EntityNotFoundException;
    User findUserEntityById(Long id) throws EntityNotFoundException;
}
