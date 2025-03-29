package com.jalian.online_store_order_management.service.impl;

import com.jalian.online_store_order_management.annotation.*;
import com.jalian.online_store_order_management.dao.UserDao;
import com.jalian.online_store_order_management.domain.User;
import com.jalian.online_store_order_management.dto.UserFetchDto;
import com.jalian.online_store_order_management.dto.UserRegisterDto;
import com.jalian.online_store_order_management.exception.DuplicateUsername;
import com.jalian.online_store_order_management.exception.EntityNotFoundException;
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

    @Override
    @Transactional(readOnly = true)
    @Valid
    public UserFetchDto findUser(@Full String username) throws EntityNotFoundException {
        if (!userDao.existsByUsername(username)) {
            throw new EntityNotFoundException(User.class.getSimpleName(), "username", username);
        }
        return UserFetchDto.of(userDao.findByUsername(username));
    }

    @Override
    @Transactional(readOnly = true)
    @Valid
    public UserFetchDto findUserById(@NotNull Long id) throws EntityNotFoundException {
        return UserFetchDto.of(findByIdInternal(id));
    }

    private User findByIdInternal(Long id) {
        if (!userDao.existsById(id)) {
            throw new EntityNotFoundException(User.class.getSimpleName(), "id", id.toString());
        }
        return userDao.findUserById(id);
    }

    @Override
    @Transactional
    public User findUserEntityById(Long id) throws EntityNotFoundException {
        return findByIdInternal(id);
    }
}
