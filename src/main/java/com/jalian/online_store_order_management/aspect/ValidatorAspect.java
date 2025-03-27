package com.jalian.online_store_order_management.aspect;

import com.jalian.online_store_order_management.annotation.*;
import com.jalian.online_store_order_management.exception.ValidationException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;

@Aspect
@Component
//todo: cleaner code is needed
public class ValidatorAspect {

    @Before("@annotation(com.jalian.online_store_order_management.annotation.Valid)")
    public void validateMethodParameters(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Annotation[][] parameterAnnotations = methodSignature.getMethod().getParameterAnnotations();
        for (int i = 0; i < args.length; i++) {
            for (Annotation annotation : parameterAnnotations[i]) {
                handleFull(annotation, args[i], methodSignature.getParameterNames()[i]);
                if (annotation instanceof NotNull notNull) {
                    handleNotNull(notNull, args[i], methodSignature.getParameterNames()[i]);
                }
                if (annotation instanceof NotEmpty notEmpty) {
                    handleNotEmpty(notEmpty, args[i], methodSignature.getParameterNames()[i]);
                }
                if (annotation instanceof NotBlank notBlank) {
                    handleNotBlank(notBlank, args[i], methodSignature.getParameterNames()[i]);
                }
                if (annotation instanceof Pattern pattern) {
                    handlePattern(pattern, args[i], methodSignature.getParameterNames()[i]);
                }
            }
        }
    }

    private void handleFull(Annotation annotation, Object arg, String param) {
        if (annotation instanceof Full) {
            if (arg == null) {
                throw new ValidationException(param + " cannot be null");
            }
            if ("".equals(String.valueOf(arg))) {
                throw new ValidationException(param + " cannot be empty");
            }
            if (" ".equals(String.valueOf(arg))) {
                throw new ValidationException(param + " cannot be blank");
            }
        }
    }

    private void handleNotNull(NotNull notNull, Object arg, String param) {
        if (arg == null) {
            if (notNull.message().isEmpty()) {
                throw new ValidationException(param + " cannot be null");
            } else {
                throw new ValidationException(notNull.message());
            }
        }
    }

    private void handleNotEmpty(NotEmpty notEmpty, Object arg, String param) {
        if ("".equals(String.valueOf(arg))) {
            if (notEmpty.message().isEmpty()) {
                throw new ValidationException(param + " cannot be empty");
            } else {
                throw new ValidationException(notEmpty.message());
            }
        }
    }

    private void handleNotBlank(NotBlank notEmpty, Object arg, String param) {
        if (" ".equals(String.valueOf(arg))) {
            if (notEmpty.message().isEmpty()) {
                throw new ValidationException(param + " cannot be blank");
            } else {
                throw new ValidationException(notEmpty.message());
            }
        }
    }

    private void handlePattern(Pattern pattern, Object arg, String param) {
        if (arg != null && !String.valueOf(arg).matches(pattern.regex())) {
            if (pattern.message().isEmpty()) {
                throw new ValidationException(param + " does not have valid pattern");
            } else {
                throw new ValidationException(pattern.message());
            }
        }
        if (arg == null) {
            throw new ValidationException(param + " cannot be null");
        }
    }
}
