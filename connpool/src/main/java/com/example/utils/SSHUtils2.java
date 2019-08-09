package com.example.utils;

import com.alibaba.fastjson.JSON;
import com.example.response.ClientOutput;
import com.example.utils.listener.MyRetryListener;
import com.github.rholder.retry.Retryer;
import com.github.rholder.retry.RetryerBuilder;
import com.github.rholder.retry.StopStrategies;
import com.github.rholder.retry.WaitStrategies;
import com.google.common.base.Charsets;
import com.google.common.base.Predicates;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * ssh连接加入重试机制
 *
 */
public class SSHUtils2 {
    protected static final Logger logger = LoggerFactory.getLogger(SSHUtils.class);

    private String hostname;
    private int port;
    private String username;
    private String password;
    private String identityPath;

    public SSHUtils2(String hostname, int port, String username, String password) {
        this.hostname = hostname;
        this.port = port;
        this.username = username;
        if (password != null && new File(password).exists()) {
            this.identityPath = new File(password).getAbsolutePath();
            this.password = null;
        } else {
            this.password = password;
            this.identityPath = null;
        }
        if(logger.isDebugEnabled()){
            logger.debug("hostname: " + hostname + "identityPath: " + identityPath + ", username : " + username + "," + password);
        }
    }

    @SuppressWarnings("Duplicates")
    public ClientOutput exe (final String command){
        logger.info("进入执行命令操作>>>>>>>>>");
        StringBuffer context = new StringBuffer();
        int returnCode=0;
        try {
            Session session = newJSchSession();
            logger.info("执行命令中>>>>>>>>>>");
            Channel channel = session.openChannel("exec");
            ((ChannelExec)channel).setCommand(command);
            channel.setInputStream(null);
            ((ChannelExec)channel).setErrStream(System.err);
            InputStream in = channel.getInputStream();
            InputStream err = ((ChannelExec) channel).getErrStream();
            channel.connect(1000);

            byte[] tmp = new byte[1024];
            while (true) {
                while (in.available() > 0) {
                    int i = in.read(tmp, 0, 1024);
                    if (i < 0)
                        break;

                    String line = new String(tmp, 0, i);
                    context.append(line);
                    logger.debug(line);
                }
                while (err.available() > 0) {
                    int i = err.read(tmp, 0, 1024);
                    if (i < 0) {
                        break;
                    }
                    String returnMsg = new String(tmp, 0, i, Charsets.UTF_8);
                    context.append(returnMsg);
                    logger.error(returnMsg);
                }
                if (channel.isClosed()) {
                    if (err.available() > 0) {
                        continue;
                    }
                    returnCode = channel.getExitStatus();
                    break;
                }

                Thread.sleep(100);
            }
            channel.disconnect();
            session.disconnect();
        } catch (JSchException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ClientOutput(returnCode,context.toString());
    }

    public Session newJSchSession() throws Exception {
        Retryer<Session> retryer = RetryerBuilder
                .<Session>newBuilder()
                //抛出runtime异常、checked异常时都会重试，但是抛出error不会重试。
                .retryIfException()
                //返回null也需要重试
                .retryIfResult(Predicates.equalTo(null))
                //重调策略
                .withWaitStrategy(WaitStrategies.fixedWait(10, TimeUnit.SECONDS))
                //设置重试的次数
                .withStopStrategy(StopStrategies.stopAfterAttempt(3))
                //添加重试监听类
                .withRetryListener(new MyRetryListener<Boolean>())
                .build();
        try {
            return retryer.call(new SessionCallable(hostname,port,username,password,identityPath));
        } catch (Exception e) {
            throw new Exception("SSH["+this.hostname+":"+this.port+"]连接重试3次后失败:"+ e.getMessage());
        }
    }

    public static class  SessionCallable implements Callable<Session>  {

        private String hostname;
        private int port;
        private String username;
        private String password;
        private String identityPath;

        public SessionCallable(String hostname, int port, String username, String password,String identityPath) {
            this.hostname = hostname;
            this.port = port;
            this.username = username;
            this.password = password;
            this.identityPath = identityPath;
        }

        @Override
        public Session call() throws Exception {
            logger.info("登录中......");
            JSch jsch = new JSch();
            Session session;
            if (identityPath != null){
                jsch.addIdentity(identityPath);
            }
            session= jsch.getSession(username, hostname, port);
            session.setTimeout(6000);
            if (password != null){
                session.setPassword(password);
            }
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect(1000);
            return session;
        }
    }

    public static void main(String[] args) {
        SSHUtils2 utils2 = new SSHUtils2("xxx.xxx.x.xxx",22,"root","root");
        ClientOutput output = utils2.exe("sh a.sh");
        logger.info(output.getText());
    }
}
