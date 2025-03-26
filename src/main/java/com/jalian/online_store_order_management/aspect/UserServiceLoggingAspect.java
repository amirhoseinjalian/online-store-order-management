package com.jalian.online_store_order_management.aspect;

import com.jalian.online_store_order_management.exception.DuplicateUsername;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class UserServiceLoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceLoggingAspect.class);

    @Pointcut("execution(public * com.jalian.online_store_order_management.service.UserService.registerUser(..))")
    private void registerUserLoggingPointcut() {}

    @Around(value = "registerUserLoggingPointcut()")
    public long logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        var savedUser = joinPoint.proceed();
        logger.info("user saved successfully: {}", savedUser);
        return (long) savedUser;
    }

    @AfterThrowing(pointcut = "registerUserLoggingPointcut()", throwing = "exception")
    public void logRegisterUserException(DuplicateUsername exception) {
        logger.error("failed to register user ", exception);
    }
}
