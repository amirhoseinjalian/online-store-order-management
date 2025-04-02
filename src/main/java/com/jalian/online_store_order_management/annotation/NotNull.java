package com.jalian.online_store_order_management.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The NotNull annotation is used to validate that a given method parameter is not {@code null}.
 * <p>
 * When applied to a parameter, it ensures that the parameter value is not {@code null}. If the validation fails,
 * an appropriate validation exception should be thrown.
 * </p>
 *
 * <p><b>Note:</b> This annotation is specifically intended for checking that objects are not null.</p>
 *
 * @author amirhosein jalian
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface NotNull {

    /**
     * The custom message that will be used if the validation fails.
     * <p>
     * If not provided, a default message should be used by the validation logic.
     * </p>
     *
     * @return the error message to be used if the parameter is null.
     */
    String message() default "";
}
