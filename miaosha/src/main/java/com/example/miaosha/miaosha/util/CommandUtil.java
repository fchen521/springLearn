package com.example.miaosha.miaosha.util;

import com.example.miaosha.miaosha.model.UserInfoPO;
import com.example.miaosha.miaosha.model.UserInfoPO2;
import org.springframework.beans.BeanUtils;
import org.springframework.util.DigestUtils;
import sun.misc.BASE64Encoder;
import tk.mybatis.mapper.util.StringUtil;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class CommandUtil {

    /*public static void commandJx(String[] args) throws ParseException {
        Options options = new Options();
        options.addOption("a",false,"1");
        options.addOption("b",false,"2");
        options.addOption("c",false,"3");

        DefaultParser parser = new DefaultParser();
        CommandLine line = parser.parse(options, args);
        String c = line.getOptionValue("c");
        System.out.println(c);
    }

    private static final Pattern VARIABLE_PATTERN = Pattern
            .compile("(\\$)\\{?(\\w+)\\}?");
   public static String JsonToString(String path){
       try {
           String s = FileUtils.readFileToString(new File(path), Charset.defaultCharset());
           Object o = JSON.parse(s);
            getKeysRecursive(o,"",new HashSet<String>());
          *//* if (o instanceof Map){
               Object o1 = ((Map) o).get("job");
               Map<String, Object> o11 = (Map<String, Object>) o1;
           }else {
               System.out.println(0);
           }*//*
           return s;
       } catch (IOException e) {
           e.printStackTrace();
       }
       return null;
   }



   public static void getKeysRecursive(final Object current, String path, Set<String> collect) {
        boolean isRegularElement = !(current instanceof Map || current instanceof List);
        if (isRegularElement) {
            collect.add(path);
            return;
        }

        boolean isMap = current instanceof Map;
        if (isMap) {
            Map<String, Object> mapping = ((Map<String, Object>) current);
            for (final String key : mapping.keySet()) {
                if (StringUtils.isBlank(path)) {
                    getKeysRecursive(mapping.get(key), key.trim(), collect);
                } else {
                    getKeysRecursive(mapping.get(key), path + "." + key.trim(),
                            collect);
                }
            }
            return;
        }

        boolean isList = current instanceof List;
        if (isList) {
            List<Object> lists = (List<Object>) current;
            for (int i = 0; i < lists.size(); i++) {
                getKeysRecursive(lists.get(i), path + String.format("[%d]", i),
                        collect);
            }
            return;
        }

        return;
    }

    public static void main(String[] args) {
        System.out.println(System.getProperty("java.io.tmpdir"));
       String s = JsonToString("F:\\work\\miaosha\\src\\main\\resources\\test.json");


        //String s = StringUtils.replace("job[name]", "[", ".[");
        //String[] split = StringUtils.split(s, ".");
        //List<String> list = Arrays.asList(StringUtils.split(s, "."));
        //list.stream().filter(x->x.contains("[")).forEach(x-> System.out.println(x.contains("[")));

        //校验QQ号，要求：必须是5~15位数字，0不能开头。没有正则表达式之前

        *//*String content = "1";

        String pattern = "[{1}]";
       s


        boolean isMatch = content.matches(pattern);
        System.out.println(isMatch);*//*
    }*/

    public static void main(String[] args) throws Exception{
            Map<String,Integer> serverWeight = new HashMap<String,Integer>();
            serverWeight.put("192.168.0.111", 6);//ip,权重
            serverWeight.put("192.168.0.112", 3);
            serverWeight.put("192.168.0.113", 2);
            serverWeight.put("192.168.0.114", 1);
            for (int i = 0; i < 16; i++) {
                String ip = getServer(serverWeight);
                System.out.println(ip);
            }
    }

    public static String getServer(Map<String,Integer> serverWeight){
        Random random = new Random();
        ArrayList<String> serverList = new ArrayList<String>();
        for(Map.Entry<String, Integer> m : serverWeight.entrySet()){
            for(int i=0,len=m.getValue();i<len;i++){
                serverList.add(m.getKey());
            }
        }

        String[] servers = serverList.toArray(new String[serverList.size()]);
        int weight_idx = random.nextInt(servers.length);
        String ip = servers[weight_idx];
        return ip;
    }


    public static String getMD5(String pwd) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");

        BASE64Encoder encoder = new BASE64Encoder();
        String md5Pwd =  encoder.encode(md5.digest(pwd.getBytes("utf-8")));
        if (StringUtil.isEmpty(md5Pwd)){
            return null;
        }
        return md5Pwd;
    }
}
