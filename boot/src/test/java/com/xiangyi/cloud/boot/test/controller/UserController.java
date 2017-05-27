package com.xiangyi.cloud.boot.test.controller;

import com.xiangyi.cloud.boot.bean.Gender;
import com.xiangyi.cloud.boot.bean.User;
import com.xiangyi.cloud.boot.controller.Response;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

/**
 * Created by bobo on 2017/5/26.
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class UserController {
    private RestTemplate restTemplate=new RestTemplate();

    @Test
    public void info(){
        HttpHeaders headers=new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        ResponseEntity<Response<User>> responseEntity=restTemplate.exchange("http://localhost:8080/user/1", HttpMethod.GET,new HttpEntity<>(headers), new ParameterizedTypeReference<Response<User>>(){});
        System.out.println(responseEntity.getBody().getResult().getName());
    }

    @Test
    public void add(){
        HttpHeaders headers=new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        User user=new User();
        user.setName("张三");
        user.setAge(18);
        user.setGender(Gender.MALE);
        Response response=restTemplate.postForObject("http://localhost:8080/user/add",new HttpEntity<>(user,headers), Response.class);
        System.out.println(response.getCode());
    }
}
