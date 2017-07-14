package com.xiangyi.zk;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.server.auth.DigestAuthenticationProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by bobo on 2017/7/14.
 */
public class Worker {
    private final static Logger logger= LoggerFactory.getLogger(Master.class);
    private String serverId;
    private final ZooKeeper zooKeeper;
    private final String auth="zk-test:zk-test";
    private final List<ACL> acls=new ArrayList<>();
    private String status="Idle";
    public Worker() throws IOException {
        Random random=new Random();
        serverId=Integer.toHexString(random.nextInt());
        zooKeeper=new ZooKeeper("119.23.79.224:8000",30000,event -> {});
        zooKeeper.addAuthInfo("digest",auth.getBytes());
        try {
            acls.add(new ACL(31,new Id("digest", DigestAuthenticationProvider.generateDigest(auth))));
        } catch (NoSuchAlgorithmException e) {

        }
    }
    private void init(){
        AsyncCallback.StringCallback callback= (rc, path, ctx, name) -> {
            if(rc!=0|| KeeperException.Code.get(rc)!= KeeperException.Code.NODEEXISTS){
                init();
            }
        };
        zooKeeper.create("/zk-test/worker/"+serverId,status.getBytes(),acls, CreateMode.EPHEMERAL,callback,null);
        zooKeeper.create("/zk-test/assign/"+serverId,status.getBytes(),acls, CreateMode.PERSISTENT,callback,null);
    }
    private void changeStatus(String status){
        this.status=status;
        zooKeeper.setData("/zk-test/worker/"+serverId,status.getBytes(),-1,(rc, path, ctx, stat) -> {
            if(rc!=0){
                changeStatus(this.status);//此次必须使用this.status,其他地方可能已修改status
            }
        },null);
    }
    public void start() throws InterruptedException {
        init();
        synchronized (this){
            wait();
        }
    }
}
