package com.example.thymeleaf.service;

public class StartService {
    public static void main(String[] args) {
      /*  MongoClient mongoClient = new MongoClient( "localhost" , 27017 );
        MongoDatabase database = mongoClient.getDatabase("runoob");
        MongoCollection<Document> r = database.getCollection("runoob1");
        FindIterable<Document> documents = r.find();
        MongoCursor<Document> iterator = documents.iterator();
        while (iterator.hasNext()){
            Document next = iterator.next();
            System.out.println(next);
        }*/
      /*  MongoCursor<String> iterator = mongoClient.listDatabaseNames().iterator();
        if (iterator.hasNext()){
            System.out.println("-----------------------------------------"+iterator.next());
        }
        mongoClient.close();
        //mongoClient.getDatabase("test").listCollectionNames();*/

       /* MongoCursor<String> iterator1 = mongoClient.getDatabase("runoob").listCollectionNames().iterator();
        while (iterator1.hasNext()){
            System.out.println("-----------------------------------------"+iterator1.next());
        }
        MongoCursor<String> iterator = mongoClient.listDatabaseNames().iterator();
        while (iterator.hasNext()){
            System.out.println("-----------------------------------------"+iterator.next());
        }*/
        /*ServerAddress address = new ServerAddress("localhost", 27017);
        MongoCredential credential = MongoCredential.createCredential(
                "root", "admin", "root".toCharArray());
        MongoClientOptions.Builder build = new MongoClientOptions.Builder();
        *//**与目标数据库能够建立的最大连接数为50*//*
        build.connectionsPerHost(50);

        *//**如果当前所有的连接都在使用中，则每个连接上可以有50个线程排队等待*//*
        build.threadsAllowedToBlockForConnectionMultiplier(50);

        *//**一个线程访问数据库的时候，在成功获取到一个可用数据库连接之前的最长等待时间为，此处为 2分钟
         * 如果超过 maxWaitTime 都没有获取到连接的话，该线程就会抛出 Exception
         * *//*
        build.maxWaitTime(1000 * 60 * 2);

        *//**设置与数据库建立连接时最长时间为1分钟*//*
        build.connectTimeout(1000 * 60 * 1);
        MongoClientOptions mongoClientOptions = build.build();

        MongoClient mongoClient = new MongoClient(address, credential,mongoClientOptions);
        int size = mongoClient.getMaxBsonObjectSize();
        MongoDatabase database = mongoClient.getDatabase("runoob");
        System.out.println(size);*/
    }
}
