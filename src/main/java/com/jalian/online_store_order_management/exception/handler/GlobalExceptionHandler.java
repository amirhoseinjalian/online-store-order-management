package com.jalian.online_store_order_management.exception.handler;

import com.jalian.online_store_order_management.exception.*;
import com.jalian.online_store_order_management.web.ErrorResponse;
import jakarta.persistence.OptimisticLockException;
import jakarta.persistence.PessimisticLockException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * The GlobalExceptionHandler class provides centralized exception handling for all controllers.
 * <p>
 * It catches and processes various exceptions thrown in the application, returning a corresponding
 * HTTP response with a custom error message encapsulated in an {@link ErrorResponse} object.
 * </p>
 *
 * @author amirhosein jalian
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Handles exceptions related to duplicate username, validation, constraint violations, and illegal balance.
     *
     * @param ex the exception that was thrown.
     * @return a ResponseEntity containing an ErrorResponse and an HTTP status code of BAD_REQUEST.
     */
    @ExceptionHandler({
            DuplicateUsername.class,
            ValidationException.class,
            ConstraintViolationException.class,
            IllegalBalanceException.class
    })
    public ResponseEntity<ErrorResponse> handleDuplicateUsernameAndValidationException(Exception ex) {
        var errorResponse = new ErrorResponse(ex);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles exceptions when an entity is not found.
     *
     * @param ex the EntityNotFoundException that was thrown.
     * @return a ResponseEntity containing an ErrorResponse and an HTTP status code of NOT_FOUND.
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFoundException(EntityNotFoundException ex) {
        var errorResponse = new ErrorResponse(ex);
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    /**
     * Handles exceptions when an HTTP request method is not supported.
     *
     * @return a ResponseEntity containing an ErrorResponse with a message indicating an illegal HTTP method,
     *         and an HTTP status code of METHOD_NOT_ALLOWED.
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException() {
        var errorResponse = new ErrorResponse("Illegal http method");
        return new ResponseEntity<>(errorResponse, HttpStatus.METHOD_NOT_ALLOWED);
    }

    /**
     * Handles exceptions occurring during a recovery process.
     *
     * @return a ResponseEntity containing an ErrorResponse with a message indicating that the transaction cannot be done,
     *         and an HTTP status code of INTERNAL_SERVER_ERROR.
     */
    @ExceptionHandler(RecoveryException.class)
    public ResponseEntity<ErrorResponse> handleRecoveryException() {
        var errorResponse = new ErrorResponse("Transaction can not be done");
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Handles pessimistic and optimistic locking exceptions.
     *
     * @return a ResponseEntity containing an ErrorResponse with a message indicating that the data is locked,
     *         and an HTTP status code of INTERNAL_SERVER_ERROR.
     */
    @ExceptionHandler({PessimisticLockException.class, OptimisticLockException.class})
    public ResponseEntity<ErrorResponse> handleLockException() {
        var errorResponse = new ErrorResponse("Data is locked");
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Handles all unknown exceptions that are not explicitly handled by other methods.
     *
     * @param ex the exception that was thrown.
     * @return a ResponseEntity containing an ErrorResponse with a generic error message,
     *         and an HTTP status code of INTERNAL_SERVER_ERROR.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnknownException(Exception ex) {
        log.error(ex.getMessage(), ex);
        var errorResponse = new ErrorResponse("Internal Server Error");
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
