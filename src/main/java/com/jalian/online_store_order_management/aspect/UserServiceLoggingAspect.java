package com.jalian.online_store_order_management.aspect;

import com.jalian.online_store_order_management.exception.DuplicateUsername;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class UserServiceLoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceLoggingAspect.class);

    @Pointcut("execution(public * com.jalian.online_store_order_management.service.UserService.registerUser(..))")
    private void registerUserLoggingPointcut() {
    }

    @Pointcut("execution(public * com.jalian.online_store_order_management.service.UserService.findUser(..)) || " +
            "execution(public * com.jalian.online_store_order_management.service.UserService.findUserById(..))")
    private void findUserLoggingPointcut() {
    }

    @AfterReturning(value = "registerUserLoggingPointcut()", returning = "result")
    public Object logAfterRegisterUser(Object result) {
        logger.info("user saved successfully: {}", result);
        return result;
    }

    @AfterThrowing(pointcut = "registerUserLoggingPointcut()", throwing = "exception")
    public void logRegisterUserException(DuplicateUsername exception) {
        logger.error("failed to register user ", exception);
    }

    @AfterReturning(value = "findUserLoggingPointcut()", returning = "result")
    public Object logAfterFetchUser(JoinPoint joinPoint, Object result) {
        var fetchElement = joinPoint.getArgs()[0];
        var fetchElementName = ((MethodSignature) joinPoint.getSignature()).getParameterNames()[0];
        logger.info("user found successfully for {}: {}: {}", fetchElementName, fetchElement, result);
        return result;
    }

    @AfterThrowing(pointcut = "findUserLoggingPointcut()", throwing = "exception")
    public void logFetchUserException(DuplicateUsername exception) {
        logger.error("failed to fetch user", exception);
    }
}
