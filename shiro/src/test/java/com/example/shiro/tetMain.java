package com.example.shiro;


import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.IntSummaryStatistics;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class tetMain{
    public void test(Collection<Date> dates){

    }

    public static void main(String[] args) throws Exception {

        /*ByteSource salt = ByteSource.Util.bytes("user");
        System.out.println(salt);
        String newPs = new SimpleHash("MD5", "123456", salt, 2).toHex();
        System.out.println(newPs);*/
        /*SysRole role=new SysRole();
        role.setRoleName("11");

        SysRole role2=new SysRole();
        role2.setRoleName("121");

        List<SysRole> list=new ArrayList<>();

        list.add(role);
        list.add(role2);

        list.forEach(i->System.out.println(i.getRoleName()));
        List<String> collect = list.stream().map(x -> x.getRoleName()).collect(Collectors.toList());
        System.out.println(collect);*/
        //byte[] result = Base64.encodeBase64("123456".getBytes("GBK"));
       /* String s = Base64.encodeBase64String("123456".getBytes("GBK"));
        System.out.println(s);

        System.out.println("大家好");*/


        /**
         * PDF转图片
         */
      /*  String filePath = "E://Greenplum企业应用实战.pdf";
        List<String> imageList = pdfToImagePath(filePath);
        Iterator<String> iterator = imageList.iterator();
        while(iterator.hasNext()){

            System.out.println(iterator.next());
        }*/
    }



    /**
     * PDF转图片
     */
    /*public static List<String> pdfToImagePath(String filePath){
        List<String> list = new ArrayList<>();
        String fileDirectory = filePath.substring(0,filePath.lastIndexOf("."));

        String imagePath;
        File file = new File(filePath);
        try {
            File f = new File(fileDirectory);
            if(!f.exists()){
                f.mkdir();
            }
            PDDocument doc = PDDocument.load(file);
            PDFRenderer renderer = new PDFRenderer(doc);
            int pageCount = doc.getNumberOfPages();
            for(int i=0; i<pageCount; i++){
                BufferedImage image = renderer.renderImage(i, 1.25f);
                imagePath = fileDirectory + "/"+i + ".jpg";
                ImageIO.write(image, "PNG", new File(imagePath));
                list.add(imagePath);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }*/



}