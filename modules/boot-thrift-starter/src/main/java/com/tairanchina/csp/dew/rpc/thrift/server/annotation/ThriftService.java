package com.tairanchina.csp.dew.rpc.thrift.server.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * Created by 迹_Jason on 2017/08/09.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Component
public @interface ThriftService {

    String[] value() default {};
}