package com.xiangyi.cloud.config.client.controller;

import com.xiangyi.cloud.config.client.config.ApplicationConfig;
import com.xiangyi.cloud.config.client.config.bean.Application;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by bobo on 2017/5/27.
 */
@RestController
public class ConfigController {
    @Autowired
    private ApplicationConfig applicationConfig;
    @RequestMapping(value = "config",method = RequestMethod.GET)
    ApplicationConfig config(){
        return applicationConfig;
    }
}
