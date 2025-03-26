package com.jalian.online_store_order_management.exception.handler;

import com.jalian.online_store_order_management.exception.DuplicateUsername;
import com.jalian.online_store_order_management.web.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DuplicateUsername.class)
    public ResponseEntity<ErrorResponse<?>> handleDuplicateUsername(DuplicateUsername ex) {
        var errorResponse = new ErrorResponse<>(-1, ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
