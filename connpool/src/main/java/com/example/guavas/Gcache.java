package com.example.guavas;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

public class Gcache {
   public void testGuavaCahce() throws ExecutionException {
       List<Person> list = new ArrayList<>();
       list.add(new Person("1","a"));
       list.add(new Person("2","a2"));
       list.add(new Person("3","a3"));

       final String key = "1";
       Cache<String, Person> cache2 = CacheBuilder.newBuilder().maximumSize(1000).build();

       /**
        * 用缓存中的get方法，当缓存命中时直接返回结果;否则，通过给定的Callable类call方法获取结果并将结果缓存。<br/>
        * 可以用一个cache对象缓存多种不同的数据，只需创建不同的Callable对象即可。
        */
       Person person = cache2.get(key, new Callable<Person>() {
           public Person call() throws ExecutionException {
               System.out.println(key + " load in cache");
               return getPerson(key);
           }

           // 此时一般我们会进行相关处理，如到数据库去查询
           private Person getPerson(String key) throws ExecutionException {
               System.out.println(key + " query");
               for (Person p : list) {
                   if (p.getId().equals(key))
                       return p;
               }
               return null;
           }
       });
       System.out.println("======= sencond time  ==========");
       person = cache2.getIfPresent(key);
       person = cache2.getIfPresent(key);
   }

    public static void main(String[] args) throws ExecutionException {
        new Gcache().testGuavaCahce();
    }

}

class Person{
    private String id;
    private String name;

    public String getId() {
        return id;
    }

    public Person setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Person setName(String name) {
        this.name = name;
        return this;
    }

    public Person(String id, String name) {
        this.id = id;
        this.name = name;
    }
}
