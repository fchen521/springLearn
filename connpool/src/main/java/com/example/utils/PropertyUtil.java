package com.example.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyUtil {
    private static final Logger logger = LoggerFactory.getLogger(PropertyUtil.class);
    private static Properties props;

    static { loadProps();}

    synchronized static void loadProps() {
        props = new Properties();
        //通过类加载器进行获取properties文件流
        InputStream in = PropertyUtil.class.getClassLoader().getResourceAsStream("application.properties");
        //通过类进行获取properties文件流
        //in = PropertyUtil.class.getResourceAsStream("application.properties");
        try {
            props.load(in);
        } catch (FileNotFoundException e){
            logger.error("文件未找到");
        } catch (IOException e) {
            logger.error("出现IOException");
        }finally {
            try {
                if (in == null) {
                    in.close();
                }
            } catch (IOException e) {
                logger.error("关闭文件流出现异常");
            }
        }
        logger.info("加载properties文件内容完成...........");
        logger.info("properties文件内容：" + props);
    }

    public static String getProperty(String key){
        if (props == null) {
            loadProps();
        }
        return props.getProperty(key);
    }

    public static String getProperty(String key, String defaultValue){
        if (props == null) {
            loadProps();
        }
        return props.getProperty(key, defaultValue);
    }
}
