package com.xiangyi.cloud.config.client.config.bean;

/**
 * Created by bobo on 2017/5/27.
 */
public class Nginx {
    public static class Server{
        private int port;
        private String name;

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    private Server server;

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }
}
