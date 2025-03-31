package com.jalian.online_store_order_management.unit.service;

import com.jalian.online_store_order_management.dao.StoreDao;
import com.jalian.online_store_order_management.domain.Store;
import com.jalian.online_store_order_management.domain.User;
import com.jalian.online_store_order_management.dto.AddStoreDto;
import com.jalian.online_store_order_management.exception.ConstraintViolationException;
import com.jalian.online_store_order_management.exception.EntityNotFoundException;
import com.jalian.online_store_order_management.service.impl.StoreServiceImpl;
import com.jalian.online_store_order_management.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StoreServiceTest {

    @Mock
    private StoreDao storeDao;

    @Mock
    private UserService userService;

    @InjectMocks
    private StoreServiceImpl storeService;

    private AddStoreDto addStoreDto;
    private Store store;

    @BeforeEach
    void setUp() {
        addStoreDto = new AddStoreDto("Test Store");
        store = new Store("Test Store");
        store.setUsers(new ArrayList<>());
        store.setId(1L);
    }

    @Test
    void addStore_success() {
        when(storeDao.findByNameSafe("Test Store")).thenReturn(Optional.empty());
        when(storeDao.save(any(Store.class))).thenAnswer(invocation -> {
            var s = invocation.getArgument(0, Store.class);
            s.setId(1L);
            return s;
        });
        var id = storeService.addStore(addStoreDto);
        assertThat(id).isEqualTo(1L);
        verify(storeDao).findByNameSafe("Test Store");
        verify(storeDao).save(any(Store.class));
    }

    @Test
    void addStore_duplicateName_throwsException() {
        when(storeDao.findByNameSafe("Test Store")).thenReturn(Optional.of(store));
        var ex = assertThrows(ConstraintViolationException.class, () -> storeService.addStore(addStoreDto));
        assertThat(ex.getMessage()).contains("Store name already exists");
        verify(storeDao).findByNameSafe("Test Store");
        verify(storeDao, never()).save(any());
    }

    @Test
    void existStore_returnsTrue() {
        when(storeDao.findByIdSafe(1L)).thenReturn(Optional.of(store));
        var exists = storeService.existStore(1L);
        assertThat(exists).isTrue();
        verify(storeDao).findByIdSafe(1L);
    }

    @Test
    void existStore_returnsFalse() {
        when(storeDao.findByIdSafe(2L)).thenReturn(Optional.empty());
        var exists = storeService.existStore(2L);
        assertThat(exists).isFalse();
        verify(storeDao).findByIdSafe(2L);
    }

    @Test
    void findStore_success() {
        when(storeDao.findByIdSafe(1L)).thenReturn(Optional.of(store));
        var result = storeService.findStore(1L);
        assertThat(result.getId()).isEqualTo(1L);
        verify(storeDao).findByIdSafe(1L);
    }

    @Test
    void findStore_notFound_throwsException() {
        when(storeDao.findByIdSafe(2L)).thenReturn(Optional.empty());
        var ex = assertThrows(EntityNotFoundException.class, () -> storeService.findStore(2L));
        assertThat(ex.getMessage()).contains("Store");
        verify(storeDao).findByIdSafe(2L);
    }

    @Test
    void belongToStore_emptyUsers_returnsFalse() {
        when(storeDao.findByIdSafe(1L)).thenReturn(Optional.of(store));
        var result = storeService.belongToStore(1L, 1L);
        assertThat(result).isFalse();
        verify(storeDao).findByIdSafe(1L);
        verify(userService, never()).findUserEntityById(any());
    }

    @Test
    void belongToStore_userNotInStore_returnsFalse() {
        store.getUsers().add(new User("other", "pass", "Other", "other@example.com", "User"));
        when(storeDao.findByIdSafe(1L)).thenReturn(Optional.of(store));
        var otherUser = new User("john", "pass", "John", "john@example.com", "Doe");
        when(userService.findUserEntityById(1L)).thenReturn(otherUser);
        var result = storeService.belongToStore(1L, 1L);
        assertThat(result).isFalse();
        verify(storeDao).findByIdSafe(1L);
        verify(userService).findUserEntityById(1L);
    }

    @Test
    void belongToStore_userInStore_returnsTrue() {
        var userInStore = new User("john", "pass", "John", "john@example.com", "Doe");
        store.getUsers().add(userInStore);
        when(storeDao.findByIdSafe(1L)).thenReturn(Optional.of(store));
        when(userService.findUserEntityById(1L)).thenReturn(userInStore);
        var result = storeService.belongToStore(1L, 1L);
        assertThat(result).isTrue();
        verify(storeDao).findByIdSafe(1L);
        verify(userService).findUserEntityById(1L);
    }
}