package com.xiangyi.zk;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.server.auth.DigestAuthenticationProvider;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bobo on 2017/7/14.
 */
public class Client {
    private final ZooKeeper zooKeeper;
    private final String auth="zk-test:zk-test";
    private final List<ACL> acls=new ArrayList<>();
    public Client() throws IOException {
        zooKeeper=new ZooKeeper("119.23.79.224:8000",30000,event -> {});
        zooKeeper.addAuthInfo("digest",auth.getBytes());
        try {
            acls.add(new ACL(31,new Id("digest", DigestAuthenticationProvider.generateDigest(auth))));
        } catch (NoSuchAlgorithmException e) {

        }
    }
    private void addTask(String id){
        zooKeeper.create("/zk-test/task/",id.getBytes(),acls, CreateMode.PERSISTENT_SEQUENTIAL,(rc, path, ctx, name) -> {
            if(rc!=0){
                addTask(id);
            }
        },null);
    }
    public void start() throws InterruptedException {
        while (true){
            addTask(""+System.currentTimeMillis());
            Thread.sleep(10*1000);
        }
    }
    public static void main(String[] atgs) throws IOException, InterruptedException {
        Client client=new Client();
        client.start();
    }
}
