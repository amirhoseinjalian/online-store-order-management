package com.jalian.online_store_order_management.annotation;

/**
 * The Pattern annotation is used to validate that a given method parameter matches a specified regular expression.
 * <p>
 * When applied to a parameter, it ensures that the parameter's string representation matches the regular expression
 * provided via the {@code regex()} attribute. If the validation fails, an appropriate validation exception should be thrown.
 * </p>
 *
 * <p><b>Note:</b> This annotation is particularly useful for validating formats such as email addresses, phone numbers, etc.</p>
 *
 * @author amirhosein jalian
 */
public @interface Pattern {

    /**
     * The regular expression that the annotated parameter must match.
     *
     * @return the regular expression string.
     */
    String regex();

    /**
     * The custom message that will be used if the validation fails.
     * <p>
     * If not provided, a default message should be used by the validation logic.
     * </p>
     *
     * @return the error message to be used if the parameter does not match the pattern.
     */
    String message() default "";
}
