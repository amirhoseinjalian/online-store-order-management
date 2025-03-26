package com.jalian.online_store_order_management.service;

import com.jalian.online_store_order_management.dto.UserRegisterDto;
import com.jalian.online_store_order_management.exception.DuplicateUsername;
import org.springframework.transaction.annotation.Transactional;

public interface UserService {

    @Transactional
    long registerUser(UserRegisterDto userRegisterDto) throws DuplicateUsername;
}
