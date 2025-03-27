package com.jalian.online_store_order_management.annotation;

import com.jalian.online_store_order_management.exception.ValidationException;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
public @interface Valid {

    //todo: must handle just supported ones not all of them
    Class<?>[] supported() default {Pattern.class, NotEmpty.class, NotEmpty.class, NotBlank.class};

    //todo: thrown exception must be customizable
    Class<? extends Exception> exception() default ValidationException.class;
}
