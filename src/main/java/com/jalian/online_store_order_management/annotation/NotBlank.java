package com.jalian.online_store_order_management.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The NotBlank annotation is used to validate that a given method parameter is not a blank string.
 * <p>
 * When applied to a parameter, it ensures that the parameter value is not a blank string (i.e., it does not contain
 * only whitespace). If the validation fails, an appropriate validation exception should be thrown.
 * </p>
 *
 * <p><b>Note:</b> This annotation is specifically intended to avoid blank strings.</p>
 *
 * @author amirhosein jalian
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface NotBlank {

    /**
     * The custom message that will be used if the validation fails.
     * <p>
     * If not provided, a default message should be used by the validation logic.
     * </p>
     *
     * @return the error message to be used if the parameter is blank.
     */
    String message() default "";
}
