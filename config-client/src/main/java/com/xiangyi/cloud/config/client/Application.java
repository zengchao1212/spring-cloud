package com.xiangyi.cloud.config.client;

import com.xiangyi.cloud.config.client.config.ApplicationConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * Created by bobo on 2017/5/27.
 */
@SpringBootApplication
@EnableConfigurationProperties(ApplicationConfig.class)
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
