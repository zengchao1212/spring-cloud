package com.xiangyi.cloud.service1.config;

import feign.auth.BasicAuthRequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

/**
 * Created by bobo on 2017/6/2.
 */
public class RemoteServiceConfiguration {

    @Value("${auth.user}")
    private String user;
    @Value("${auth.password}")
    private String password;

    @Bean
    public BasicAuthRequestInterceptor  basicAuthRequestInterceptor () {
        return new BasicAuthRequestInterceptor (user,password);
    }
}
