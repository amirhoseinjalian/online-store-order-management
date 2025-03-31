package com.jalian.online_store_order_management.unit.service;

import com.jalian.online_store_order_management.constant.BalanceOperation;
import com.jalian.online_store_order_management.dao.UserDao;
import com.jalian.online_store_order_management.domain.User;
import com.jalian.online_store_order_management.dto.UpdateBalanceDto;
import com.jalian.online_store_order_management.dto.UserRegisterDto;
import com.jalian.online_store_order_management.exception.DuplicateUsername;
import com.jalian.online_store_order_management.exception.EntityNotFoundException;
import com.jalian.online_store_order_management.exception.IllegalBalanceException;
import com.jalian.online_store_order_management.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserDao userDao;

    @InjectMocks
    private UserServiceImpl userService;

    private UserRegisterDto userRegisterDto;
    private User user;

    @BeforeEach
    void setUp() {
        userRegisterDto = new UserRegisterDto("John", "Doe", "john@example.com", "password", "john");
        user = new User("john", "password", "John", "john@example.com", "Doe");
        user.setId(1L);
    }

    @Test
    void registerUser_success() throws DuplicateUsername {
        when(userDao.findByUsername("john")).thenReturn(Optional.empty());
        when(userDao.save(any(User.class))).thenReturn(user);
        var id = userService.registerUser(userRegisterDto);
        assertThat(id).isEqualTo(1L);
        verify(userDao).findByUsername("john");
        verify(userDao).save(any(User.class));
    }

    @Test
    void registerUser_duplicateUsername() {
        when(userDao.findByUsername("john")).thenReturn(Optional.of(user));
        var ex = assertThrows(DuplicateUsername.class, () -> userService.registerUser(userRegisterDto));
        assertThat(ex.getMessage()).contains("john");
        verify(userDao).findByUsername("john");
        verify(userDao, never()).save(any());
    }

    @Test
    void findUser_success() throws EntityNotFoundException {
        when(userDao.findByUsername("john")).thenReturn(Optional.of(user));
        var result = userService.findUser("john");
        assertThat(result.username()).isEqualTo("john");
        verify(userDao, times(2)).findByUsername("john");
    }

    @Test
    void findUser_notFound() {
        when(userDao.findByUsername("john")).thenReturn(Optional.empty());
        var ex = assertThrows(EntityNotFoundException.class, () -> userService.findUser("john"));
        assertThat(ex.getMessage()).contains("User");
        verify(userDao).findByUsername("john");
    }

    @Test
    void findUserById_success() throws EntityNotFoundException {
        when(userDao.findUserById(1L)).thenReturn(Optional.of(user));
        var result = userService.findUserById(1L);
        assertThat(result.id()).isEqualTo("1");
        verify(userDao).findUserById(1L);
    }

    @Test
    void findUserById_notFound() {
        when(userDao.findUserById(2L)).thenReturn(Optional.empty());
        var ex = assertThrows(EntityNotFoundException.class, () -> userService.findUserById(2L));
        assertThat(ex.getMessage()).contains("User");
        verify(userDao).findUserById(2L);
    }

    @Test
    void updateBalance_plus_success() {
        user.setBalance(100.0);
        var updateBalanceDto = new UpdateBalanceDto(1L, 50.0, BalanceOperation.PLUS);
        when(userDao.findUserByIdForUpdate(1L)).thenReturn(Optional.of(user));
        when(userDao.save(any(User.class))).thenReturn(user);
        var result = userService.updateBalance(updateBalanceDto);
        assertThat(result.balance()).isEqualTo(150.0);
        verify(userDao).findUserByIdForUpdate(1L);
        verify(userDao).save(any(User.class));
    }

    @Test
    void updateBalance_minus_success() {
        user.setBalance(100.0);
        var updateBalanceDto = new UpdateBalanceDto(1L, 50.0, BalanceOperation.MINUS);
        when(userDao.findUserByIdForUpdate(1L)).thenReturn(Optional.of(user));
        when(userDao.save(any(User.class))).thenReturn(user);
        var result = userService.updateBalance(updateBalanceDto);
        assertThat(result.balance()).isEqualTo(50.0);
        verify(userDao).findUserByIdForUpdate(1L);
        verify(userDao).save(any(User.class));
    }

    @Test
    void updateBalance_minus_insufficientFunds() {
        user.setBalance(30.0);
        var updateBalanceDto = new UpdateBalanceDto(1L, 50.0, BalanceOperation.MINUS);
        when(userDao.findUserByIdForUpdate(1L)).thenReturn(Optional.of(user));
        var ex = assertThrows(IllegalBalanceException.class, () -> userService.updateBalance(updateBalanceDto));
        assertThat(ex).isInstanceOf(IllegalBalanceException.class);
        verify(userDao).findUserByIdForUpdate(1L);
        verify(userDao, never()).save(any(User.class));
    }

    @Test
    void updateBalance_userNotFound() {
        var updateBalanceDto = new UpdateBalanceDto(2L, 50.0, BalanceOperation.PLUS);
        when(userDao.findUserByIdForUpdate(2L)).thenReturn(Optional.empty());
        var ex = assertThrows(EntityNotFoundException.class, () -> userService.updateBalance(updateBalanceDto));
        assertThat(ex.getMessage()).contains("User");
        verify(userDao).findUserByIdForUpdate(2L);
    }
}