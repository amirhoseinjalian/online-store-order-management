package com.jalian.online_store_order_management.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class StoreServiceLoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(StoreServiceLoggingAspect.class);

    @Pointcut("execution(public * com.jalian.online_store_order_management.service.StoreService.addStore(..))")
    private void addStoreLoggingPointcut() {}

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
