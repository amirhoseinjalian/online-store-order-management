package com.jalian.online_store_order_management.service.impl;

import com.jalian.online_store_order_management.annotation.*;
import com.jalian.online_store_order_management.constant.BalanceOperation;
import com.jalian.online_store_order_management.dao.UserDao;
import com.jalian.online_store_order_management.domain.User;
import com.jalian.online_store_order_management.dto.UpdateBalanceDto;
import com.jalian.online_store_order_management.dto.UserFetchDto;
import com.jalian.online_store_order_management.dto.UserRegisterDto;
import com.jalian.online_store_order_management.exception.DuplicateUsername;
import com.jalian.online_store_order_management.exception.EntityNotFoundException;
import com.jalian.online_store_order_management.exception.IllegalBalanceException;
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
        if (userDao.findByUsername(userRegisterDto.username()).isPresent())
            throw new DuplicateUsername(userRegisterDto.username());
        return userDao.save(User.of(userRegisterDto)).getId();
    }

    @Override
    @Transactional(readOnly = true)
    @Valid
    public UserFetchDto findUser(@Full String username) throws EntityNotFoundException {
        if (userDao.findByUsername(username).isEmpty()) {
            throw new EntityNotFoundException(User.class.getSimpleName(), "username", username);
        }
        return UserFetchDto.of(userDao.findByUsername(username).get());
    }

    @Override
    @Transactional(readOnly = true)
    @Valid
    public UserFetchDto findUserById(@NotNull Long id) throws EntityNotFoundException {
        return UserFetchDto.of(findByIdInternal(id));
    }

    private User findByIdInternal(Long id) {
        var user = userDao.findUserById(id);
        if (user.isEmpty()) {
            throw new EntityNotFoundException(User.class.getSimpleName(), "id", id.toString());
        }
        return user.get();
    }

    @Override
    @Transactional(readOnly = true)
    public User findUserEntityById(Long id) throws EntityNotFoundException {
        return findByIdInternal(id);
    }

    @Override
    @Transactional
    public UserFetchDto updateBalance(UpdateBalanceDto updateBalanceDto) {
        var optionalUser = userDao.findUserByIdForUpdate(updateBalanceDto.userId());
        if (optionalUser.isEmpty()) {
            throw new EntityNotFoundException(User.class.getSimpleName(), "id", updateBalanceDto.userId().toString());
        }
        var user = optionalUser.get();
        if (updateBalanceDto.operation() == BalanceOperation.MINUS) {
            if (user.getBalance() - updateBalanceDto.amount() < 0.0) {
                throw new IllegalBalanceException();
            }
        }
        switch (updateBalanceDto.operation()) {
            case PLUS -> user.setBalance(user.getBalance() + updateBalanceDto.amount());
            case MINUS -> user.setBalance(user.getBalance() - updateBalanceDto.amount());
        }
        return UserFetchDto.of(userDao.save(user));
    }
}
