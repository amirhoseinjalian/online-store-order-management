package com.jalian.online_store_order_management.aspect;

import com.jalian.online_store_order_management.exception.DuplicateUsername;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * The UserServiceLoggingAspect class provides logging for methods in the UserService.
 * <p>
 * It defines pointcuts for user registration and user fetching operations, and logs
 * successful operations as well as exceptions that occur during these operations.
 * This is useful for debugging and monitoring the behavior of the UserService.
 * </p>
 *
 * @author amirhosein jalian
 */
@Component
@Aspect
public class UserServiceLoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceLoggingAspect.class);

    /**
     * Pointcut that matches the execution of the public registerUser method in the UserService.
     */
    @Pointcut("execution(public * com.jalian.online_store_order_management.service.UserService.registerUser(..))")
    private void registerUserLoggingPointcut() {
    }

    /**
     * Pointcut that matches the execution of the public findUser and findUserById methods in the UserService.
     */
    @Pointcut("execution(public * com.jalian.online_store_order_management.service.UserService.findUser(..)) || " +
            "execution(public * com.jalian.online_store_order_management.service.UserService.findUserById(..))")
    private void findUserLoggingPointcut() {
    }

    /**
     * Logs a message after the successful execution of the registerUser method.
     *
     * @param result the result returned by the registerUser method.
     * @return the result of the registerUser method.
     */
    @AfterReturning(value = "registerUserLoggingPointcut()", returning = "result")
    public Object logAfterRegisterUser(Object result) {
        logger.info("user saved successfully: {}", result);
        return result;
    }

    /**
     * Logs an error message when an exception of type DuplicateUsername is thrown
     * during the execution of the registerUser method.
     *
     * @param exception the DuplicateUsername exception thrown.
     */
    @AfterThrowing(pointcut = "registerUserLoggingPointcut()", throwing = "exception")
    public void logRegisterUserException(DuplicateUsername exception) {
        logger.error("failed to register user ", exception);
    }

    /**
     * Logs a message after the successful execution of the findUser or findUserById methods.
     *
     * @param joinPoint the join point representing the method execution.
     * @param result    the result returned by the findUser or findUserById method.
     * @return the result of the method execution.
     */
    @AfterReturning(value = "findUserLoggingPointcut()", returning = "result")
    public Object logAfterFetchUser(JoinPoint joinPoint, Object result) {
        var fetchElement = joinPoint.getArgs()[0];
        var fetchElementName = ((MethodSignature) joinPoint.getSignature()).getParameterNames()[0];
        logger.info("user found successfully for {}: {}: {}", fetchElementName, fetchElement, result);
        return result;
    }

    /**
     * Logs an error message when an exception of type DuplicateUsername is thrown
     * during the execution of the findUser or findUserById methods.
     *
     * @param exception the DuplicateUsername exception thrown.
     */
    @AfterThrowing(pointcut = "findUserLoggingPointcut()", throwing = "exception")
    public void logFetchUserException(DuplicateUsername exception) {
        logger.error("failed to fetch user", exception);
    }
}