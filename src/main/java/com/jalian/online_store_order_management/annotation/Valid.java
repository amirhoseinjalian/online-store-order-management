package com.jalian.online_store_order_management.annotation;

import com.jalian.online_store_order_management.exception.ValidationException;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The Valid annotation is used to mark methods whose arguments should be validated
 * according to the specified supported annotations.
 * <p>
 * When a method is annotated with {@code @Valid}, the parameters of the method will be
 * checked against the constraints defined by the supported annotations (e.g., {@code @Pattern},
 * {@code @NotEmpty}, {@code @NotBlank}). If a validation fails, the exception specified by
 * the {@code exception()} attribute is thrown.
 * </p>
 * <p>
 * <b>Note:</b> Only the supported annotations listed in the {@code supported()} attribute will be handled.
 * Additional validation annotations will be ignored.
 * </p>
 * <p>
 * <b>Todo:</b> Handle just supported ones not all of them, and allow customizable thrown exception.
 * </p>
 *
 * @author amirhosein jalian
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
public @interface Valid {

    /**
     * An array of annotation classes that are supported for validation.
     * <p>
     * Only the annotations specified in this array will be processed when validating
     * the method arguments.
     * </p>
     *
     * @return an array of supported annotation classes.
     */
    Class<?>[] supported() default {Pattern.class, NotEmpty.class, NotEmpty.class, NotBlank.class};

    /**
     * The exception class that will be thrown if validation fails.
     * <p>
     * The default is {@link ValidationException}. This allows the thrown exception to be customized.
     * </p>
     *
     * @return the exception class to be thrown on validation failure.
     */
    Class<? extends Exception> exception() default ValidationException.class;
}
