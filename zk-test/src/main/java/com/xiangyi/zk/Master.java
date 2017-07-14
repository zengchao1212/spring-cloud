package com.xiangyi.zk;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.data.Stat;
import org.apache.zookeeper.server.auth.DigestAuthenticationProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * Created by bobo on 2017/7/12.
 */
public class Master {

    private static class Worker{
        private String id;
        private String status;
        private Worker(String id,String status){
            this.id=id;
            this.status=status;
        }
    }
    private final static Logger logger= LoggerFactory.getLogger(Master.class);
    private final String serverId;
    private final ZooKeeper zooKeeper;
    private boolean isLeader=false;
    private final String auth="zk-test:zk-test";
    private final List<ACL> acls=new ArrayList<>();
    private Map<String,String> workerMap=new LinkedHashMap<>();
    public Master() throws IOException {
        Random random=new Random();
        serverId=Integer.toHexString(random.nextInt());
        zooKeeper=new ZooKeeper("119.23.79.224:8000",30000,event -> {});
        zooKeeper.addAuthInfo("digest",auth.getBytes());
        try {
            acls.add(new ACL(31,new Id("digest", DigestAuthenticationProvider.generateDigest(auth))));
        } catch (NoSuchAlgorithmException e) {

        }
        final Boolean[] flag = {true};
        while (flag[0]){
            zooKeeper.create("/zk-test","".getBytes(),acls, CreateMode.PERSISTENT,(rc, path, ctx, name) -> {
                if(rc==0|| KeeperException.Code.get(rc)== KeeperException.Code.NODEEXISTS){
                    flag[0] =false;
                }else {
                    logger.warn("create node /zk-test error:{}",KeeperException.Code.get(rc).name());
                }
            },null);
        }
    }
    private void register(){
        final Boolean[] flag = {true};
        while (flag[0]){
            zooKeeper.create("/zk-test/master",serverId.getBytes(),acls, CreateMode.EPHEMERAL,(rc, path, ctx, name) -> {
                if(rc==0){
                    isLeader=true;
                    flag[0] =false;
                }else if(KeeperException.Code.get(rc)== KeeperException.Code.NODEEXISTS){
                    flag[0] =false;
                }else {
                    logger.warn("create node /zk-test/master error:{}",KeeperException.Code.get(rc).name());
                    checkRegisterStat();
                }
            },null);
        }
        if(isLeader){
            logger.debug("current process is registed as master");
            init();
        }else{
            logger.debug("other master-process already exist");
        }
    }
    private void checkRegisterStat() {
        zooKeeper.getData("/zk-test/master",false,(rc, path, ctx, data, stat) -> {
            if(rc==0){
                isLeader=serverId.equals(new String(data));
            }else if(KeeperException.Code.get(rc)== KeeperException.Code.NONODE){
                register();
            }else {
                logger.warn(KeeperException.Code.get(rc).name());
                register();
            }
        },null);
    }
    private void init(){
        AsyncCallback.StringCallback callback= (rc, path, ctx, name) -> {
            if(rc!=0|| KeeperException.Code.get(rc)!= KeeperException.Code.NODEEXISTS){
                init();
            }
        };
        zooKeeper.create("/zk-test/worker",new byte[]{0},acls, CreateMode.PERSISTENT,callback,null);
        zooKeeper.create("/zk-test/assign",new byte[]{0},acls, CreateMode.PERSISTENT,callback,null);
        zooKeeper.create("/zk-test/task",new byte[]{0},acls, CreateMode.PERSISTENT,callback,null);
        zooKeeper.create("/zk-test/status",new byte[]{0},acls, CreateMode.PERSISTENT,callback,null);
    }
    private void listenMaster(){
        zooKeeper.exists("/zk-test/master",event -> {
            listenMaster();
            if(event.getType()== Watcher.Event.EventType.NodeDeleted){
                register();
            }
        },(rc, path, ctx, stat) -> {
            if(rc!=0){
                listenMaster();
            }
        },null);
    }
    private void listenWorker(){
        zooKeeper.getChildren("/zk-test/worker",event -> {
            listenMaster();
            if(event.getType()== Watcher.Event.EventType.NodeDeleted){
                String worker=event.getPath();
                if(!"/zk-test/worker".equals(worker)){
                    String workerId=worker.split("/")[3];
                    reAssign(workerId);
                }
            }
        },(rc, path, ctx, children) -> {
            if(rc==0){
                children.forEach(cpath->{
                    listenSingleWorker(cpath);
                });
            }else {
                listenMaster();
            }
        },null);
    }
    private void reAssign(String workerId){
        zooKeeper.getChildren("/zk-test/assign/"+workerId,false,(rc, path, ctx, children) -> {
            if(rc==0){
                children.forEach(cpath->{
                    zooKeeper.getData(cpath,false,(rc1, path1, ctx1, data, stat) -> {
                        if(rc1==0){
                            String taskId=cpath.split("/")[4];
                            zooKeeper.create("/zk-test/task/"+taskId,data,acls,CreateMode.PERSISTENT,(rc2,path2,ctx2,name)->{
                                if(rc2==0|| KeeperException.Code.get(rc2)== KeeperException.Code.NODEEXISTS){
                                    zooKeeper.delete(cpath,-1,(rc3, path3, ctx3) -> {
                                        if(rc3!=0||KeeperException.Code.get(rc3)!= KeeperException.Code.NONODE){
                                            reAssign(workerId);
                                        }
                                    },null);
                                }
                            },null);
                        }else if(KeeperException.Code.get(rc)!= KeeperException.Code.NONODE){
                            reAssign(workerId);
                        }
                    },null);
                });
            }else {
                reAssign(workerId);
            }
        },null);
    }
    private void listenSingleWorker(String path){
        zooKeeper.getData(path,event -> {
            listenSingleWorker(path);
            if(event.getType()== Watcher.Event.EventType.NodeCreated){
                workerMap.put(path,"Idle");
            }else if(event.getType()== Watcher.Event.EventType.NodeDeleted){
                workerMap.remove(path);
            }
        },(rc, path1, ctx, data, stat) -> {
            if(rc==0){
                workerMap.put(path,new String(data));
            }else if(KeeperException.Code.get(rc)!= KeeperException.Code.NONODE){
                listenSingleWorker(path);
            }
        },null);
    }
    private void listenTask(){

    }
    private void listen() throws InterruptedException {
        listenMaster();
        listenWorker();
        listenTask();
        synchronized (this){
            wait();
        }
    }
    public void start() throws InterruptedException {
        register();
        listen();
    }
    public void stop() throws InterruptedException {
        zooKeeper.close();
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        Master master=new Master();
        master.start();
        master.stop();
    }
}
