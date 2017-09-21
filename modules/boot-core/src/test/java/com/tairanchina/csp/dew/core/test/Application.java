package com.tairanchina.csp.dew.core.test;

import com.tairanchina.csp.dew.core.DewBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Date;

@EnableTransactionManagement
public class Application extends DewBootApplication {

    public static void main(String[] args) {
        long start = new Date().getTime();
        new SpringApplicationBuilder(Application.class).web(true).run(args);
        System.out.println(">>>>>>>>>>>>"+(new Date().getTime() - start));
    }

}