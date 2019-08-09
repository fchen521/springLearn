package com.example.utils;

import com.example.response.ClientOutput;
import com.google.common.base.Charsets;
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

/**
 * ssh连接
 */
public class SSHUtils {
    protected static final Logger logger = LoggerFactory.getLogger(SSHUtils.class);

    private String hostname;
    private int port;
    private String username;
    private String password;
    private String identityPath;

    public SSHUtils(String hostname, int port, String username, String password) {
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

    public ClientOutput execute (final String command){
        StringBuffer context = new StringBuffer();
        int returnCode=0;
        JSch jsch = new JSch();
        try {
            if (identityPath != null){
                jsch.addIdentity(identityPath);
            }
            Session session = jsch.getSession(username, hostname, port);
            session.setTimeout(6000);
            if (password != null){
                session.setPassword(password);
            }
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect(1000);
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
        }
        return new ClientOutput(returnCode,context.toString());
    }



    public static void main(String[] args) {
        SSHUtils utils = new SSHUtils("xxx.xxx.x.xxx",22,"root","root");
        ClientOutput uptime = utils.execute("sh a.sh");
        System.out.println(uptime.getText());

    }
}
