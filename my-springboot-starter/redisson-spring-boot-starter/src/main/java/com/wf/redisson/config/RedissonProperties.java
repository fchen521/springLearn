package com.wf.redisson.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author chen fei
 * @version 1.0
 * @desc 厚德笃行 致用创新
 * @date 2021/2/23 10:24
 */
@ConfigurationProperties(prefix = "wf.redisson")
public class RedissonProperties {
    private String host = "localhost";
    private int prot = 6379;
    private int timeout;
    private boolean ssl;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getProt() {
        return prot;
    }

    public void setProt(int prot) {
        this.prot = prot;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public boolean isSsl() {
        return ssl;
    }

    public void setSsl(boolean ssl) {
        this.ssl = ssl;
    }
}
