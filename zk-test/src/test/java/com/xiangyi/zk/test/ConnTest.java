package com.xiangyi.zk.test;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by bobo on 2017/7/12.
 */
public class ConnTest {
    private final static Logger logger= LoggerFactory.getLogger(ConnTest.class);
    public static void main(String[] args){
        ZooKeeper zooKeeper;
        try {
            logger.debug("------");
            zooKeeper=new ZooKeeper("119.23.79.224:8000",30000,event -> {
                logger.debug(Thread.currentThread().getName()+":"+event.getState().name());
            });
            logger.debug("------");

        } catch (IOException e) {
            logger.debug(e.getMessage());
            return;
        }
        while (true){
            try {
                logger.debug(zooKeeper.getChildren("/",false).toString());
            } catch (KeeperException|InterruptedException e) {
                logger.debug(e.getMessage());
            }
            try {
                Thread.sleep(5*1000);
            } catch (InterruptedException e) {
                logger.debug(e.getMessage());
            }

        }

    }
}
