package com.example.kafka_demo.Configure;

public class KakfaConfigure {
    public interface kafkaProperties{
        public final static String ZK = "10.211.55.15:2181,10.211.55.17:2181,10.211.55.18:2181";
        public final static String GROUP_ID = "test_group1";
        public final static String TOPIC = "test2";
        public final static String BROKER_LIST = "10.211.55.15:9092,10.211.55.17:9092,10.211.55.18:9092";
        public final static int BUFFER_SIZE = 64 * 1024;
        public final static int TIMEOUT = 20000;
        public final static int INTERVAL = 10000;
    }
}
