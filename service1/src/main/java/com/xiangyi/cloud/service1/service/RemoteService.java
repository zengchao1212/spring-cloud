package com.xiangyi.cloud.service1.service;

import com.xiangyi.cloud.service1.config.RemoteServiceConfiguration;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

/**
 * Created by bobo on 2017/6/2.
 */
@FeignClient(value = "service1",configuration = RemoteServiceConfiguration.class)
public interface RemoteService {
    @RequestMapping(value = "test/info",method = RequestMethod.GET)
    Map<String,String> info();
    @RequestMapping(value = "test/setinfo",method = RequestMethod.POST)
    Map<String,String> setInfo(@RequestBody Map<String,String> newInfo);
}
