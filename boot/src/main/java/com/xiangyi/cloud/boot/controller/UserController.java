package com.xiangyi.cloud.boot.controller;

import com.xiangyi.cloud.boot.bean.Gender;
import com.xiangyi.cloud.boot.bean.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * Created by bobo on 2017/5/26.
 */
@RestController
@RequestMapping("user")
public class UserController {
    private final Logger logger= LoggerFactory.getLogger(UserController.class);
    @RequestMapping(value = "{id}",method = RequestMethod.GET)
    Response<User> info(@PathVariable Integer id){
        User user=new User();
        user.setId(id);
        user.setName("张三");
        user.setAge(18);
        user.setGender(Gender.MALE);
        user.setCreateTime(LocalDateTime.now());
        return new Response<>(user);
    }

    @RequestMapping(value = "add",method = RequestMethod.POST)
    Response add(@RequestBody User user){
        logger.debug(user.getName());
        return new Response(null);
    }
}
