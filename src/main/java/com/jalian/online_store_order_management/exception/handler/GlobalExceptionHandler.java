package com.jalian.online_store_order_management.exception.handler;

import com.jalian.online_store_order_management.exception.DuplicateUsername;
import com.jalian.online_store_order_management.exception.EntityNotFoundException;
import com.jalian.online_store_order_management.exception.ValidationException;
import com.jalian.online_store_order_management.web.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler({DuplicateUsername.class, ValidationException.class})
    public ResponseEntity<ErrorResponse> handleDuplicateUsernameAndValidationException(Exception ex) {
        var errorResponse = new ErrorResponse(ex);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFoundException(EntityNotFoundException ex) {
        var errorResponse = new ErrorResponse(ex);
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnknownException(Exception ex) {
        log.error(ex.getMessage(), ex);
        var errorResponse = new ErrorResponse("Internal Server Error");
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
