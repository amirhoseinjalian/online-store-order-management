package com.jalian.online_store_order_management.endpoint;

import com.jalian.online_store_order_management.dto.UpdateBalanceDto;
import com.jalian.online_store_order_management.dto.UserFetchDto;
import com.jalian.online_store_order_management.dto.UserRegisterDto;
import com.jalian.online_store_order_management.exception.DuplicateUsername;
import com.jalian.online_store_order_management.service.UserService;
import com.jalian.online_store_order_management.web.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * The UserEndpoint class provides REST endpoints for managing users in the system.
 * <p>
 * It supports operations such as user registration, fetching user details by username or ID,
 * and updating a user's balance.
 * </p>
 *
 * @author amirhosein jalian
 */
@RestController
@RequestMapping("/users")
public class UserEndpoint {

    private final UserService userService;

    /**
     * Constructs a new UserEndpoint with the specified UserService.
     *
     * @param userService the service used for user operations.
     */
    public UserEndpoint(UserService userService) {
        this.userService = userService;
    }

    /**
     * Registers a new user in the system.
     * <p>
     * This endpoint accepts user registration details via a {@link UserRegisterDto} and returns
     * the unique identifier of the newly created user. If a duplicate username is encountered,
     * a {@link DuplicateUsername} exception is thrown.
     * </p>
     *
     * @param userRegisterDto the data transfer object containing registration details.
     * @return a ResponseEntity containing a BaseResponse with the created user ID and a success message.
     * @throws DuplicateUsername if the username is already taken.
     */
    @PostMapping("/register")
    public ResponseEntity<BaseResponse<Long>> register(@RequestBody UserRegisterDto userRegisterDto)
            throws DuplicateUsername {
        var savedUserId = userService.registerUser(userRegisterDto);
        return new ResponseEntity<>(
                new BaseResponse<>(savedUserId, "User registered successfully"),
                HttpStatus.CREATED
        );
    }

    /**
     * Fetches a user by their username.
     * <p>
     * This endpoint retrieves user details for the specified username.
     * </p>
     *
     * @param username the username of the user to fetch.
     * @return a ResponseEntity containing a BaseResponse with the fetched user details.
     */
    @GetMapping("/username/{username}")
    public ResponseEntity<BaseResponse<UserFetchDto>> findUserByUsername(@PathVariable String username) {
        var user = userService.findUser(username);
        return new ResponseEntity<>(new BaseResponse<>(user, "User fetched successfully"), HttpStatus.OK);
    }

    /**
     * Fetches a user by their unique identifier.
     * <p>
     * This endpoint retrieves user details for the specified user ID.
     * </p>
     *
     * @param id the unique identifier of the user.
     * @return a ResponseEntity containing a BaseResponse with the fetched user details.
     */
    @GetMapping("/id/{id}")
    public ResponseEntity<BaseResponse<UserFetchDto>> findUserById(@PathVariable Long id) {
        var user = userService.findUserById(id);
        return new ResponseEntity<>(new BaseResponse<>(user, "User fetched successfully"), HttpStatus.OK);
    }

    /**
     * Updates the balance of a user.
     * <p>
     * This endpoint accepts a {@link UpdateBalanceDto} containing the user ID, the amount to update,
     * and the operation type (e.g., addition or subtraction). It returns the updated user details.
     * </p>
     *
     * @param dto the data transfer object containing balance update details.
     * @return a ResponseEntity containing a BaseResponse with the updated user details.
     */
    @PutMapping("/update-balance")
    public ResponseEntity<BaseResponse<UserFetchDto>> updateBalance(@RequestBody UpdateBalanceDto dto) {
        var newUser = userService.updateBalance(dto);
        return new ResponseEntity<>(
                new BaseResponse<>(newUser, "Balance updated successfully"),
                HttpStatus.OK
        );
    }
}
