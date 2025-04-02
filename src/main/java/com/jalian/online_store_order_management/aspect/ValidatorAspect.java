package com.jalian.online_store_order_management.aspect;

import com.jalian.online_store_order_management.annotation.*;
import com.jalian.online_store_order_management.exception.ValidationException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;

/**
 * The ValidatorAspect class intercepts methods annotated with {@code @Valid} and performs parameter validations.
 * <p>
 * It examines the method parameters and their annotations to ensure they adhere to the constraints defined by
 * custom annotations such as {@code @Full}, {@code @NotNull}, {@code @NotEmpty}, {@code @NotBlank}, and {@code @Pattern}.
 * If a validation fails, a {@link ValidationException} is thrown with an appropriate message.
 * </p>
 *
 * <p><b>Note:</b> Cleaner code refactoring is needed.</p>
 *
 * @author amirhosein jalian
 */
@Aspect
@Component
public class ValidatorAspect {

    /**
     * Intercepts the execution of any method annotated with {@code @Valid} to validate its parameters.
     * <p>
     * For each method parameter, it retrieves the associated annotations and performs validations based on:
     * <ul>
     *     <li>{@code @Full}: Ensures the argument is not null, empty, or blank.</li>
     *     <li>{@code @NotNull}: Ensures the argument is not null.</li>
     *     <li>{@code @NotEmpty}: Ensures the argument is not empty.</li>
     *     <li>{@code @NotBlank}: Ensures the argument is not blank.</li>
     *     <li>{@code @Pattern}: Ensures the argument matches a given regular expression.</li>
     * </ul>
     * </p>
     *
     * @param joinPoint the join point representing the method execution.
     */
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

    /**
     * Validates the parameter annotated with {@code @Full}.
     * <p>
     * This method checks that the argument is not {@code null}, not empty, and not a single blank space.
     * If any of these conditions fail, a {@link ValidationException} is thrown with a message indicating the problem.
     * </p>
     *
     * @param annotation the annotation instance (expected to be {@code @Full}).
     * @param arg the method argument to validate.
     * @param param the name of the parameter.
     */
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

    /**
     * Validates the parameter annotated with {@code @NotNull}.
     * <p>
     * Checks if the argument is {@code null}. If it is, throws a {@link ValidationException} with either the default
     * message or the message provided in the annotation.
     * </p>
     *
     * @param notNull the {@code @NotNull} annotation instance.
     * @param arg the method argument to validate.
     * @param param the name of the parameter.
     */
    private void handleNotNull(NotNull notNull, Object arg, String param) {
        if (arg == null) {
            if (notNull.message().isEmpty()) {
                throw new ValidationException(param + " cannot be null");
            } else {
                throw new ValidationException(notNull.message());
            }
        }
    }

    /**
     * Validates the parameter annotated with {@code @NotEmpty}.
     * <p>
     * Checks if the argument is empty. If it is, throws a {@link ValidationException} with either the default
     * message or the message provided in the annotation.
     * </p>
     *
     * @param notEmpty the {@code @NotEmpty} annotation instance.
     * @param arg the method argument to validate.
     * @param param the name of the parameter.
     */
    private void handleNotEmpty(NotEmpty notEmpty, Object arg, String param) {
        if ("".equals(String.valueOf(arg))) {
            if (notEmpty.message().isEmpty()) {
                throw new ValidationException(param + " cannot be empty");
            } else {
                throw new ValidationException(notEmpty.message());
            }
        }
    }

    /**
     * Validates the parameter annotated with {@code @NotBlank}.
     * <p>
     * Checks if the argument is a single blank space. If it is, throws a {@link ValidationException} with either the default
     * message or the message provided in the annotation.
     * </p>
     *
     * @param notBlank the {@code @NotBlank} annotation instance.
     * @param arg the method argument to validate.
     * @param param the name of the parameter.
     */
    private void handleNotBlank(NotBlank notBlank, Object arg, String param) {
        if (" ".equals(String.valueOf(arg))) {
            if (notBlank.message().isEmpty()) {
                throw new ValidationException(param + " cannot be blank");
            } else {
                throw new ValidationException(notBlank.message());
            }
        }
    }

    /**
     * Validates the parameter annotated with {@code @Pattern}.
     * <p>
     * Checks if the argument is not {@code null} and matches the regular expression specified by the annotation.
     * If the argument is {@code null} or does not match the pattern, a {@link ValidationException} is thrown with
     * an appropriate message.
     * </p>
     *
     * @param pattern the {@code @Pattern} annotation instance.
     * @param arg the method argument to validate.
     * @param param the name of the parameter.
     */
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