package com.jalian.online_store_order_management.unit.service;

import com.jalian.online_store_order_management.dao.StoreDao;
import com.jalian.online_store_order_management.domain.Store;
import com.jalian.online_store_order_management.domain.User;
import com.jalian.online_store_order_management.dto.AddStoreDto;
import com.jalian.online_store_order_management.dto.AddUserToStoreDto;
import com.jalian.online_store_order_management.dto.UserFetchDto;
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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the {@link StoreServiceImpl} class.
 * <p>
 * This class contains test cases that verify the behavior of the store-related services,
 * including adding a store, checking store existence, retrieving store data, verifying store ownership,
 * and adding a user to a store.
 * </p>
 *
 * @author amirhosein jalian
 */
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

    /**
     * Sets up the initial state for the tests.
     * <p>
     * Initializes a store and an AddStoreDto object for use in the test cases.
     * </p>
     */
    @BeforeEach
    void setUp() {
        addStoreDto = new AddStoreDto("Test Store");
        store = new Store("Test Store");
        store.setUsers(new ArrayList<>());
        store.setId(1L);
    }

    /**
     * Test case for successfully adding a store.
     * <p>
     * Verifies that the store is added when the store name is unique.
     * </p>
     */
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

    /**
     * Test case for adding a store with a duplicate name.
     * <p>
     * Verifies that a {@link ConstraintViolationException} is thrown when the store name already exists.
     * </p>
     */
    @Test
    void addStore_duplicateName_throwsException() {
        when(storeDao.findByNameSafe("Test Store")).thenReturn(Optional.of(store));
        var ex = assertThrows(ConstraintViolationException.class, () -> storeService.addStore(addStoreDto));
        assertThat(ex.getMessage()).contains("Store name already exists");
        verify(storeDao).findByNameSafe("Test Store");
        verify(storeDao, never()).save(any());
    }

    /**
     * Test case for checking if a store exists by its ID.
     * <p>
     * Verifies that the method returns true when the store is found.
     * </p>
     */
    @Test
    void existStore_returnsTrue() {
        when(storeDao.findByIdSafe(1L)).thenReturn(Optional.of(store));
        var exists = storeService.existStore(1L);
        assertThat(exists).isTrue();
        verify(storeDao).findByIdSafe(1L);
    }

    /**
     * Test case for checking if a store exists by its ID.
     * <p>
     * Verifies that the method returns false when the store is not found.
     * </p>
     */
    @Test
    void existStore_returnsFalse() {
        when(storeDao.findByIdSafe(2L)).thenReturn(Optional.empty());
        var exists = storeService.existStore(2L);
        assertThat(exists).isFalse();
        verify(storeDao).findByIdSafe(2L);
    }

    /**
     * Test case for retrieving a store by its ID.
     * <p>
     * Verifies that the correct store is returned when the store is found.
     * </p>
     */
    @Test
    void findStore_success() {
        when(storeDao.findByIdSafe(1L)).thenReturn(Optional.of(store));
        var result = storeService.findStore(1L);
        assertThat(result.getId()).isEqualTo(1L);
        verify(storeDao).findByIdSafe(1L);
    }

    /**
     * Test case for retrieving a store by its ID when the store is not found.
     * <p>
     * Verifies that an {@link EntityNotFoundException} is thrown when the store is not found.
     * </p>
     */
    @Test
    void findStore_notFound_throwsException() {
        when(storeDao.findByIdSafe(2L)).thenReturn(Optional.empty());
        var ex = assertThrows(EntityNotFoundException.class, () -> storeService.findStore(2L));
        assertThat(ex.getMessage()).contains("Store");
        verify(storeDao).findByIdSafe(2L);
    }

    /**
     * Test case for checking if a user belongs to a store when the store has no users.
     * <p>
     * Verifies that the method returns false when there are no users in the store.
     * </p>
     */
    @Test
    void belongToStore_emptyUsers_returnsFalse() {
        when(storeDao.findByIdSafe(1L)).thenReturn(Optional.of(store));
        var result = storeService.belongToStore(1L, 1L);
        assertThat(result).isFalse();
        verify(storeDao).findByIdSafe(1L);
        verify(userService, never()).findUserEntityById(any());
    }

    /**
     * Test case for checking if a user belongs to a store when the user is not in the store.
     * <p>
     * Verifies that the method returns false when the user is not found in the store's list of users.
     * </p>
     */
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

    /**
     * Test case for checking if a user belongs to a store when the user is in the store.
     * <p>
     * Verifies that the method returns true when the user is in the store's list of users.
     * </p>
     */
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

    /**
     * Test case for successfully adding a user to a store.
     * <p>
     * This test verifies that the {@code addUserToStore} method correctly retrieves the store and user,
     * adds the user to the store's list of users, saves the updated store, and returns the updated list of
     * users as a list of {@link UserFetchDto} objects.
     * </p>
     */
    @Test
    void addUserToStore_success() {
        // Prepare a user and a store with an empty user list.
        var userToAdd = new User("john", "pass", "John", "john@example.com", "Doe");
        userToAdd.setId(1L);
        store.setUsers(new ArrayList<>());

        // Set up mocks to return the store and user.
        when(storeDao.findByIdSafe(1L)).thenReturn(Optional.of(store));
        when(userService.findUserEntityById(1L)).thenReturn(userToAdd);
        when(storeDao.save(any(Store.class))).thenAnswer(invocation -> invocation.getArgument(0, Store.class));

        // Create the DTO for adding a user to a store.
        var addUserToStoreDto = new com.jalian.online_store_order_management.dto.AddUserToStoreDto(1L, 1L);

        // Call the method and capture the result.
        List<UserFetchDto> userFetchDtos = storeService.addUserToStore(addUserToStoreDto);

        // Verify that the user was added.
        verify(storeDao).findByIdSafe(1L);
        verify(userService).findUserEntityById(1L);
        verify(storeDao).save(any(Store.class));

        // Assert that the returned list contains the added user.
        assertThat(userFetchDtos).isNotEmpty();
        assertThat(userFetchDtos.get(0).username()).isEqualTo("john");
    }

    /**
     * Test case for adding a user to a store when the user is already in the store.
     * <p>
     * This test verifies that if a user is already present in the store's user list, the method
     * will add the user again (based on the current implementation) and return a list containing duplicate entries.
     * If duplicate prevention is desired, the implementation should be updated.
     * </p>
     */
    @Test
    void addUserToStore_duplicateUser_returnsDuplicate() {
        // Prepare a user and add it to the store.
        var userToAdd = new User("john", "pass", "John", "john@example.com", "Doe");
        userToAdd.setId(1L);
        store.setUsers(new ArrayList<>());
        store.getUsers().add(userToAdd);

        // Set up mocks.
        when(storeDao.findByIdSafe(1L)).thenReturn(Optional.of(store));
        when(userService.findUserEntityById(1L)).thenReturn(userToAdd);
        when(storeDao.save(any(Store.class))).thenAnswer(invocation -> invocation.getArgument(0, Store.class));

        // Create the DTO.
        var addUserToStoreDto = new com.jalian.online_store_order_management.dto.AddUserToStoreDto(1L, 1L);

        // Call the method.
        List<UserFetchDto> userFetchDtos = storeService.addUserToStore(addUserToStoreDto);

        // Verify interactions.
        verify(storeDao).findByIdSafe(1L);
        verify(userService).findUserEntityById(1L);
        verify(storeDao).save(any(Store.class));

        // Assert that the returned list contains the user twice (since duplicate checking is not implemented).
        assertThat(userFetchDtos).hasSize(2);
        assertThat(userFetchDtos.get(0).username()).isEqualTo("john");
        assertThat(userFetchDtos.get(1).username()).isEqualTo("john");
    }
}
