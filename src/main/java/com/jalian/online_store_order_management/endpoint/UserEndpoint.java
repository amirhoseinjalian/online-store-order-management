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

@RestController
@RequestMapping("/users")
public class UserEndpoint {

    private final UserService userService;

    public UserEndpoint(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<BaseResponse<Long>> register(@RequestBody UserRegisterDto userRegisterDto)
            throws DuplicateUsername {

        var savedUserId = userService.registerUser(userRegisterDto);
        return new ResponseEntity<>(
                new BaseResponse<>(savedUserId, "User registered successfully"),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<BaseResponse<UserFetchDto>> findUserByUsername(@PathVariable String username) {
        var user = userService.findUser(username);
        return new ResponseEntity<>(new BaseResponse<>(user, "User fetched successfully"), HttpStatus.OK);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<BaseResponse<UserFetchDto>> findUserById(@PathVariable Long id) {
        var user = userService.findUserById(id);
        return new ResponseEntity<>(new BaseResponse<>(user, "User fetched successfully"), HttpStatus.OK);
    }

    @PutMapping("/update-balance")
    public ResponseEntity<BaseResponse<UserFetchDto>> register(@RequestBody UpdateBalanceDto dto) {
        var newUser = userService.updateBalance(dto);
        return new ResponseEntity<>(
                new BaseResponse<>(newUser, "Balance updated successfully"),
                HttpStatus.OK
        );
    }
}
