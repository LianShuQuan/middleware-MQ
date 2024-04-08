package com.middleware.messagequeue.service;

import com.middleware.messagequeue.mq.TopicDefinition;
import com.middleware.messagequeue.util.MQUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    @Autowired
    private MQUtil mqUtil;
    public void createOrder() throws Exception {
        System.out.println("createOrder in service");
        mqUtil.sendMessageRocketMQWithIdempotent("message from RocketMQWithIdempotent", TopicDefinition.ROCKET_MQ_IDEMPOTENT_TOPIC);
    }
}
