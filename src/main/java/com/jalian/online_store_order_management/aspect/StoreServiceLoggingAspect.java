package com.jalian.online_store_order_management.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * The StoreServiceLoggingAspect class is an aspect that logs the execution of the addStore method in the StoreService.
 * <p>
 * It intercepts calls to the addStore method to log method entry, exit, and any errors that occur during execution.
 * This is useful for debugging and monitoring the behavior of the store service.
 * </p>
 *
 * @author amirhosein jalian
 */
@Component
@Aspect
public class StoreServiceLoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(StoreServiceLoggingAspect.class);

    /**
     * Pointcut that matches the execution of the public addStore method in the StoreService class.
     */
    @Pointcut("execution(public * com.jalian.online_store_order_management.service.StoreService.addStore(..))")
    private void addStoreLoggingPointcut() {}

    /**
     * Logs the execution of the addStore method.
     * <p>
     * This method logs the entry into the addStore method with its arguments, then proceeds with the execution,
     * and finally logs the exit with the result. In case of an exception, it logs the error and rethrows the exception.
     * </p>
     *
     * @param joinPoint the join point representing the method execution.
     * @return the result of the method execution.
     * @throws Throwable if an error occurs during the execution of the addStore method.
     */
    @Around("addStoreLoggingPointcut()")
    public Object logAddStore(ProceedingJoinPoint joinPoint) throws Throwable {
        logger.info("Entering method: {} with arguments: {}", joinPoint.getSignature().toShortString(), joinPoint.getArgs());
        try {
            Object result = joinPoint.proceed();
            logger.info("Exiting method {} with result: {}", joinPoint.getSignature().toShortString(), result);
            return result;
        } catch (Throwable ex) {
            logger.error("Error in method {}: {}", joinPoint.getSignature().toShortString(), ex.getMessage(), ex);
            throw ex;
        } finally {
            logger.info("Completed execution of method: {}", joinPoint.getSignature().toShortString());
        }
    }
}