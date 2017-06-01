package com.xiangyi.cloud.eureka.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * Created by bobo on 2017/5/31.
 */
@SpringBootApplication
@EnableEurekaServer
public class Application {
    public static void main(String[] args){
        SpringApplication.run(Application.class,args);
    }
}
