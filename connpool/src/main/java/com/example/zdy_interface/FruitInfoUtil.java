package com.example.zdy_interface;

import java.lang.reflect.Field;

/**
 * 注解处理器
 */
public class FruitInfoUtil {
    public static void getFruitInfo(Class<?> clazz) {
        String namec = "名字：";
        Field[] fields = clazz.getDeclaredFields();
        //通过反射获取注解
        for (Field field : fields) {
            if (field.isAnnotationPresent(ZdyInterface.class)) {
                ZdyInterface zdyInterface = (ZdyInterface) field.getAnnotation(ZdyInterface.class);
                //注解信息的处理地方
                namec = " 编号：" + zdyInterface.id() + "名称："+ zdyInterface.name() ;
                System.out.println(namec);
            }
        }
    }

    public static void main(String[] args) {
        //FruitInfoUtil.getFruitInfo(UserModel.class);

    }
}
