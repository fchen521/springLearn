import com.alibaba.fastjson.JSON;
import com.example.model.UserInfo;
import com.sun.javafx.scene.control.skin.ListCellSkin;
import lombok.Value;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.internal.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.file.Files;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IntSummaryStatistics;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.function.IntConsumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.maxBy;
import static java.util.stream.Collectors.toList;

public class test_main {

    private static Logger logger = LoggerFactory.getLogger(test_main.class);
   public static ThreadLocal<Student> threadLocal = new ThreadLocal<>();
    public static void main(String[] args) throws CloneNotSupportedException {
        System.out.println();
        /*Logger main = LoggerFactory.getLogger("test_main");
        logger.debug("hello {}","word");
        main.debug("你好，世界!!!");
        List list = new ArrayList();
        list.add(DateFormatUtils.format(new Date(),"yyyy-MM-dd")+"/"+ UUID.randomUUID().toString());
        list.forEach(x-> System.out.println(x));*/
       /* Properties properties = System.getProperties();
        System.out.println(properties);*/
       /* List<stu> stus = Arrays.asList(new stu("1", 11), new stu("2", 20));
        stu stu = stus.stream().min(Comparator.comparing(x -> x.getAge())).get();
        System.out.println(JSON.toJSONString(stu));*/
       /* UserInfo userInfos = new UserInfo();
        userInfos.setId(1);
        UserInfo userInfos2 = new UserInfo();
        userInfos2.setId(2);

        ArrayList<UserInfo> infos = new ArrayList<UserInfo>();
        infos.add(userInfos);

        ArrayList<UserInfo> infos2 = new ArrayList<UserInfo>();
        infos.add(userInfos2);
        List<stu> list = new ArrayList<>();
        list.add(new stu("11",infos));
        list.add(new stu("22",infos2));

        *//*list.stream().forEach(x->{
            x.getUserInfos().forEach(y->{
                System.out.println(y.getId());
            });
        });*//*

        list.stream().flatMap(x->x.getUserInfos().stream()).forEach(x-> System.out.println(x.getId()));*/
       /* Student stu = new Student("jack",10);
        Student stu01 = new Student("jack",20);
        Student stu1 = new Student("jack1",10);
        Student stu2 = new Student("jack2",45);
        List<Student> list = Arrays.asList(stu,stu1,stu2,stu01);
        Set<String> set = list.stream().filter(o -> o.getAge() > 10).map(m -> m.getName()).peek(log->logger.info(log)).collect(Collectors.toSet());
        System.out.println(set);*/


       // List<String> primes = Arrays.asList("A","C","B","G","D");
        //primes.stream().sorted(Comparator.reverseOrder()).forEach(System.out::println);
       /* List<String> li=Arrays.asList("a","b","c");
        List<String> li2=Arrays.asList("b","d","g");
        li.stream().flatMap(x->li2.stream()).forEach(x->System.out.println(x));
        System.out.println("a".equals("A"));
        Map<String,String> map = new HashMap<>();
        map.put("aaa","aa");
        map.get("a");*/
       /* Student student = new Student();
        student.setAge(24);
        student.setName("niesong");
        Student student2 = (Student) student.clone();
        //这个是调用下面的那个方法，然后把这个这个对象Clone到student
        System.out.println("Age:" + student2.getAge() + " " + "Name:" + student2.getName());

        System.out.println("---------------------");
        student2.setName("aa");
        //克隆后得到的是一个新的对象，所以重新写的是student2这个对象的值

        System.out.println(student.getName());
        System.out.println(student2.getName());*/

       //Incorrect datetime value: 'Mon Aug 05 16:59:27 CST 2019' for column 'end_date' at row 1



    }
}
class Student implements Cloneable{
    private String name;
    private int age;

    public Student() {
    }

    public Student(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        Object object = super.clone();
        return object;
    }
}
