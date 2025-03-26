package com.jalian.online_store_order_management.endpoint;

import com.jalian.online_store_order_management.dto.UserRegisterDto;
import com.jalian.online_store_order_management.exception.DuplicateUsername;
import com.jalian.online_store_order_management.service.UserService;
import com.jalian.online_store_order_management.web.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
