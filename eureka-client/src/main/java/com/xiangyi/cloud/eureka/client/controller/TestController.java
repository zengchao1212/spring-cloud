package com.xiangyi.cloud.eureka.client.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by bobo on 2017/6/2.
 */
@RestController
public class TestController {
    @Autowired
    private DiscoveryClient discoveryClient;

    @RequestMapping(value = "discover/info",method = RequestMethod.GET)
    List<Map<String,String>> discoverInfo(){
        return info("EUREKA-SERVER");
    }

    @RequestMapping(value = "info",method = RequestMethod.GET)
    List<Map<String,String>> info(){
        return info("EUREKA-CLIENT");
    }
    private List<Map<String,String>> info(String serviceId){
        List<Map<String,String>> infos=new ArrayList<>();
        discoveryClient.getInstances(serviceId).forEach(serviceInstance -> {
            Map<String,String> item=new HashMap<>();
            item.put("serviceId",serviceInstance.getServiceId());
            item.put("host",serviceInstance.getHost());
            item.put("port",""+serviceInstance.getPort());
            item.put("uri",serviceInstance.getUri().toString());
            infos.add(item);
        });
        return infos;
    }
}
