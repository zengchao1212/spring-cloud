package com.xiangyi.cloud.eureka.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * Created by bobo on 2017/6/2.
 */
@SpringBootApplication
@EnableEurekaClient
public class Application {
    public static void main(String[] args){
        SpringApplication.run(Application.class,args);
    }
}
