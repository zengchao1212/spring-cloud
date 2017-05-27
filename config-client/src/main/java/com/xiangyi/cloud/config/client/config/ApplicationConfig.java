package com.xiangyi.cloud.config.client.config;

import com.xiangyi.cloud.config.client.config.bean.Application;
import com.xiangyi.cloud.config.client.config.bean.Nginx;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by bobo on 2017/5/27.
 */
@ConfigurationProperties
public class ApplicationConfig {
    private Application application;
    private Nginx nginx;

    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    public Nginx getNginx() {
        return nginx;
    }

    public void setNginx(Nginx nginx) {
        this.nginx = nginx;
    }
}
