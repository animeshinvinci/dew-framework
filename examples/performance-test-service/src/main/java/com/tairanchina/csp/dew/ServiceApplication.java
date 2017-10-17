package com.tairanchina.csp.dew;

import com.tairanchina.csp.dew.core.DewCloudApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;


/**
 * ServiceApplication
 *
 * @author hzzjb
 * @date 2017/9/19
 */
public class ServiceApplication extends DewCloudApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder(ServiceApplication.class).run(args);
    }
}
