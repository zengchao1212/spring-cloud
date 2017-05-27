package com.xiangyi.cloud.boot;

import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * Created by bobo on 2017/5/26.
 */
@Configuration
public class BeanConfiguration {
    @Bean
    public RestTemplate restTemplate(){
        RestTemplateBuilder builder=new RestTemplateBuilder();
        try {
            SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(
                    new SSLContextBuilder()
                            .loadTrustMaterial((TrustStrategy) (chain, authType) -> true)
                            .build(), NoopHostnameVerifier.INSTANCE);

            return builder
                    .requestFactory(new HttpComponentsClientHttpRequestFactory(HttpClients.custom()
                            .setSSLSocketFactory(socketFactory)
                            .build()))
                    .build();
        } catch (Exception e) {
            throw new IllegalStateException("Unable to create SSL HttpClient", e);
        }
    }
}
