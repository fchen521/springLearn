package com.example.utils;

import com.google.common.base.Splitter;
import com.google.common.primitives.Chars;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;

/**
 * @author cf
 * @version 1.0
 * @date 2019/8/9 16:18
 * 可以根据txt文件做一些脚本初始化类问题
 */
public class readTxt {

    /**
     *
     * @param path 文件路径
     * @param rule 内容分割规则
     */
    public void analysisTxt(String path,String rule){
        if (rule.isEmpty()){
            rule = " ";
        }
        File file=new File(path);
        BufferedReader reader=null;
        String temp = null;
        try{
            reader=new BufferedReader(new FileReader(file));
            while((temp=reader.readLine()) != null){
                List<String> strings = Splitter.on(rule).omitEmptyStrings().trimResults().splitToList(temp);
                String[] array = strings.toArray(new String[strings.size()]);
                System.out.println(array[0]+"\t"+array[1]);
                //doSomething
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally{
            if(reader!=null){
                try{
                    reader.close();
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
    }
    public static void main(String[] args){
        readTxt txt = new readTxt();
        String path = Thread.currentThread().getContextClassLoader().getResource("test/a.txt").getPath();
        txt.analysisTxt(path," ");
    }
}
