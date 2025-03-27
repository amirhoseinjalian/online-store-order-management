package com.jalian.online_store_order_management.annotation;

public @interface Pattern {

    String regex();
    String message() default "";
}
