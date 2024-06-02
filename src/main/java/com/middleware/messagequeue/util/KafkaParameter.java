package com.middleware.messagequeue.util;

public class KafkaParameter {
    public String topic;
    public String message;
    public String id;

    public KafkaParameter(String topic,String id ,String message ){
        this.topic = topic;
        this.message = message;
        this.id = id;
    }
}
