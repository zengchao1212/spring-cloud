package com.xiangyi.cloud.service1.controller;

import com.xiangyi.cloud.service1.service.RemoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by bobo on 2017/6/2.
 */
@RestController
@RequestMapping("test")
public class TestController {

    @Autowired
    private RemoteService remoteService;
    private Map<String,String> info;

    @RequestMapping(value = "info",method = RequestMethod.GET)
    Map<String,String> info(){
        synchronized (this){
            if(info==null){
                info=new ConcurrentHashMap<>();
                info.put("name","service1");
            }
        }
        return info;
    }

    @RequestMapping(value = "setinfo",method = RequestMethod.POST)
    Map<String,String> setInfo(@RequestBody Map<String,String> newInfo){
        synchronized (this){
            if(info==null){
                info=new ConcurrentHashMap<>();
                info.put("name","service1");
            }
        }
        newInfo.forEach((k,v)->info.put(k,v));
        return info;
    }

    @RequestMapping(value = "remote/info",method = RequestMethod.GET)
    Map<String,String> remoteInfo(){
        return remoteService.info();
    }

    @RequestMapping(value = "remote/setinfo",method = RequestMethod.GET)
    Map<String,String> remoteSetInfo(){
        Map<String,String> newInfo=new HashMap<>();
        newInfo.put("k1","v1");
        newInfo.put("k2","v2");
        return remoteService.setInfo(newInfo);
    }

}
