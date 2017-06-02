package com.xiangyi.cloud.service1.config;

import feign.Client;
import feign.Feign;
import feign.auth.BasicAuthRequestInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.netflix.feign.FeignClientsConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

/**
 * Created by bobo on 2017/6/2.
 * 此方式必须用在controller层或service层
 */
@Import(FeignClientsConfiguration.class)
public class BeanConfiguration {
    @Autowired
    private Client client;

    @Value("${auth.user}")
    private String user;
    @Value("${auth.password}")
    private String password;

    @Bean
    public RemoteFeignClient remoteFeignClient(){
        return Feign.builder().client(client)
                .requestInterceptor(new BasicAuthRequestInterceptor("admin","admin321"))
                .target(RemoteFeignClient.class,"http://service1");
    }
}
