package com.xiangyi.zk.test;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Created by bobo on 2017/7/5.
 */
public class ChangeListeningTest {
    private BlockingQueue<WatchedEvent> events=new ArrayBlockingQueue<WatchedEvent>(1);
    public static void main(String[] args){
        new ChangeListeningTest().start();
    }
    private void start(){
        ZooKeeper zk;
        try {
            zk=new ZooKeeper("119.23.79.224:8000",3000,null);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        System.out.println("程序已启动");
        try {
            zk.exists("/zk-test", event -> {
                try {
                    events.put(event);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            boolean flag=true;
            while (flag){
                WatchedEvent event=events.take();
                if(event.getType()== Watcher.Event.EventType.NodeDeleted){
                    flag=false;
                }else{
                    byte[] datas=zk.getData("/zk-test",event2 -> {
                        try {
                            events.put(event2);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    },null);
                    System.out.println("节点内容为："+new String(datas));
                }
            }
            System.out.println("节点已删除，程序退出");
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
