package com.jalian.online_store_order_management.service.impl;

import com.jalian.online_store_order_management.dao.UserDao;
import com.jalian.online_store_order_management.domain.User;
import com.jalian.online_store_order_management.dto.UserRegisterDto;
import com.jalian.online_store_order_management.exception.DuplicateUsername;
import com.jalian.online_store_order_management.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    @Transactional
    public long registerUser(UserRegisterDto userRegisterDto) throws DuplicateUsername {
        if (userDao.existsByUsername(userRegisterDto.username()))
            throw new DuplicateUsername(userRegisterDto.username());
        return userDao.save(User.of(userRegisterDto)).getId();
    }
}
