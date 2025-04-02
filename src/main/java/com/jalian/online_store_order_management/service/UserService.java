package com.jalian.online_store_order_management.service;

import com.jalian.online_store_order_management.domain.User;
import com.jalian.online_store_order_management.dto.UpdateBalanceDto;
import com.jalian.online_store_order_management.dto.UserFetchDto;
import com.jalian.online_store_order_management.dto.UserRegisterDto;
import com.jalian.online_store_order_management.exception.DuplicateUsername;
import com.jalian.online_store_order_management.exception.EntityNotFoundException;
import com.jalian.online_store_order_management.exception.IllegalBalanceException;

/**
 * The UserService interface defines operations for managing users in the system.
 * <p>
 * It includes methods for registering users, fetching user details by username or ID,
 * retrieving the user entity, and updating a user's balance.
 * </p>
 *
 * @author amirhosein jalian
 */
public interface UserService {

    /**
     * Registers a new user in the system.
     * <p>
     * This method takes a {@link UserRegisterDto} containing user registration details and
     * creates a new user. If a user with the same username already exists, a {@link DuplicateUsername}
     * exception is thrown.
     * </p>
     *
     * @param userRegisterDto the data transfer object containing the registration details.
     * @return the unique identifier of the newly registered user.
     * @throws DuplicateUsername if the username already exists.
     */
    long registerUser(UserRegisterDto userRegisterDto) throws DuplicateUsername;

    /**
     * Retrieves user details by username.
     * <p>
     * This method fetches a user based on the provided username and returns a {@link UserFetchDto}
     * containing the user's details.
     * </p>
     *
     * @param username the username of the user to fetch.
     * @return a {@link UserFetchDto} with the user's details.
     * @throws EntityNotFoundException if no user is found with the specified username.
     */
    UserFetchDto findUser(String username) throws EntityNotFoundException;

    /**
     * Retrieves user details by user ID.
     * <p>
     * This method fetches a user based on the provided unique identifier and returns a {@link UserFetchDto}
     * containing the user's details.
     * </p>
     *
     * @param id the unique identifier of the user.
     * @return a {@link UserFetchDto} with the user's details.
     * @throws EntityNotFoundException if no user is found with the specified ID.
     */
    UserFetchDto findUserById(Long id) throws EntityNotFoundException;

    /**
     * Retrieves the {@link User} entity by its unique identifier.
     * <p>
     * This method returns the user entity corresponding to the given ID.
     * </p>
     *
     * @param id the unique identifier of the user.
     * @return the {@link User} entity.
     * @throws EntityNotFoundException if no user is found with the specified ID.
     */
    User findUserEntityById(Long id) throws EntityNotFoundException;

    /**
     * Updates the balance of a user's account.
     * <p>
     * This method adjusts the user's balance based on the details provided in the {@link UpdateBalanceDto}.
     * If the balance update would result in an illegal balance (e.g., negative balance), an
     * {@link IllegalBalanceException} is thrown.
     * </p>
     *
     * @param updateBalanceDto the data transfer object containing balance update details.
     * @return a {@link UserFetchDto} with the updated user details.
     * @throws IllegalBalanceException if the balance update is not permitted.
     */
    UserFetchDto updateBalance(UpdateBalanceDto updateBalanceDto) throws IllegalBalanceException;
}
