package com.example.process;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;

public class ProcessUtil {
    private Process process;

    public void run() throws IOException, IllegalAccessException, NoSuchFieldException {
        ProcessBuilder builder = new ProcessBuilder("cmd", "/c", "dir");
        builder.directory(new File("D:/"));
        builder.redirectErrorStream(true);
        process = builder.start();
        Field f = this.getClass().getDeclaredField("pid");
        f.setAccessible(true);
        int processId = f.getInt(process);
        System.out.println(processId);
        BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream(), "GBK"));
        String line;
        while ((line = br.readLine()) != null) {
                System.out.println(line);
        }

    }

    public static void main(String[] args) throws IllegalAccessException, NoSuchFieldException, IOException {
        new ProcessUtil().run();
    }

}
