package com.xiangyi.cloud.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by bobo on 2017/5/26.
 */
@SpringBootApplication
public class Application {
    public static void main(String[] args){
        SpringApplication app=new SpringApplication(Application.class);
        app.run(args);
    }
}
