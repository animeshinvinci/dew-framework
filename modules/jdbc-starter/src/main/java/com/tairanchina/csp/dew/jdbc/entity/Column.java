package com.tairanchina.csp.dew.jdbc.entity;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Column {

    // 默认为字段名（驼峰转下划线）
    String columnName() default "";

    boolean notNull() default false;

}
