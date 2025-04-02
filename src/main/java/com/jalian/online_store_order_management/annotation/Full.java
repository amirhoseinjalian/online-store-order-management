package com.jalian.online_store_order_management.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The Full annotation is used to validate that a given method parameter is not null, not an empty string,
 * and not a blank string.
 * <p>
 * When applied to a parameter, it ensures that the parameter contains a meaningful value and is not merely
 * null, an empty sequence of characters, or a string consisting solely of whitespace. If the validation fails,
 * an appropriate validation exception should be thrown.
 * </p>
 *
 * <p><b>Note:</b> This annotation combines the validations of not null, not empty, and not blank.</p>
 *
 * @author amirhosein jalian
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface Full {
}
