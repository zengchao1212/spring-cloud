package com.xiangyi.zk.test;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.server.auth.DigestAuthenticationProvider;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bobo on 2017/7/6.
 */
public class ACLCaseTest {
    public static void main(String[] args){
        ZooKeeper zk;
        try {
            zk=new ZooKeeper("119.23.79.224:8000",3000,null);
            zk.addAuthInfo("digest","test:test".getBytes());
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        List<ACL> aclList=new ArrayList<>();
        /**
         * READ=1，WRITE=2，CREATE=4，DELETE=8，ADMIN=16
         */
        try {
            aclList.add(new ACL(15,new Id("world","anyone")));
            zk.create("/zk-test","".getBytes(),aclList, CreateMode.PERSISTENT);
            aclList.clear();
            aclList.add(new ACL(1,new Id("digest", DigestAuthenticationProvider.generateDigest("test:test"))));
            zk.create("/zk-test/acl-test","test string".getBytes(),aclList, CreateMode.PERSISTENT);
            System.out.println("获取数据成功："+new String(zk.getData("/zk-test/acl-test",false,null)));
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

    }
}
