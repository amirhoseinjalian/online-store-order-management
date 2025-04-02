package com.jalian.online_store_order_management.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The NotEmpty annotation is used to validate that a given method parameter is not an empty string.
 * <p>
 * When applied to a parameter, it ensures that the parameter value is not an empty string (i.e., it must contain
 * at least one character). If the validation fails, an appropriate validation exception should be thrown.
 * </p>
 *
 * <p><b>Note:</b> This annotation is specifically intended to avoid empty strings.</p>
 *
 * @author amirhosein jalian
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface NotEmpty {

    /**
     * The custom message that will be used if the validation fails.
     * <p>
     * If not provided, a default message should be used by the validation logic.
     * </p>
     *
     * @return the error message to be used if the parameter is empty.
     */
    String message() default "";
}
