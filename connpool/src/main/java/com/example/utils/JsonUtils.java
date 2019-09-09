package com.example.utils;

import com.alibaba.fastjson.JSON;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class JsonUtils {
    private static String split(final String path) {
        return StringUtils.replace(path, "[", ".[");
    }

    private static List<String> split2List(final String path) {
        return Arrays.asList(StringUtils.split(split(path), "."));
    }

    private static boolean isPathList(final String path) {
        return path.contains("[") && path.contains("]");
    }

    private static boolean isPathMap(final String path) {
        return StringUtils.isNotBlank(path) && !isPathList(path);
    }

    /*
    * 在不知道JSON结构的情况下，根据给出的路径，获取值
    * */
    public static void main(String[] args) throws IOException {
        File file = new File("src\\main\\resources\\test\\test.json");
        String s = IOUtils.toString(new FileInputStream(file), "utf-8");
        Object root = JSON.parse(s);
        Object object = root;
        //需要获取信息的路径
        String path = "job.setting.keyVersion";
        for (final String each : split2List(path)) {
            if (isPathMap(each)) {
                boolean b = (object instanceof Map);
                if (!b){throw new RuntimeException("error");}
                Object result = ((Map<String, Object>) object).get(each);
                object= result;
                //root = findObjectInMap(root, each);
                continue;
            } else {
                boolean isList = (object instanceof List);
                System.out.println(isList);
                String index = each.replace("[", "").replace("]", "");
                Object o = ((List<Object>) object).get(Integer.valueOf(index));
                object = o;
                //root = findObjectInList(root, each);
                continue;
            }
        }
        System.out.println(object);
    }
}
