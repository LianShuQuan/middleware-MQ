package com.middleware.messagequeue.service;

import com.middleware.messagequeue.mq.TopicDefinition;
import com.middleware.messagequeue.service.bo.OrderItem;
import com.middleware.messagequeue.util.KafkaParameter;
import com.middleware.messagequeue.util.KafkaUtil;
import com.middleware.messagequeue.util.MQUtil;
import com.middleware.messagequeue.util.snowflake.SnowFlakeIdWorker;
import io.netty.channel.epoll.EpollMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {
    @Autowired
    private MQUtil mqUtil;
    @Autowired
    private KafkaUtil kafkaUtil;
    @Autowired
    private SnowFlakeIdWorker snowFlakeIdWorker;
    public void createOrder() throws Exception {
        System.out.println("createOrder in service");
        //mqUtil.sendMessageRocketMQWithIdempotent("message from RocketMQWithIdempotent", TopicDefinition.ROCKET_MQ_IDEMPOTENT_TOPIC);
    }

    public void createOrderKafka(List<OrderItem> orderItems, List<String> coupons, Integer point) throws Exception {
        String orderId = String.valueOf(snowFlakeIdWorker.nextId());
        System.out.println(orderId);
        List<KafkaParameter> kafkaParameters = new ArrayList<>();
        kafkaParameters.add(new KafkaParameter("products",orderId,orderItems.toString()));
        kafkaParameters.add(new KafkaParameter("coupon",orderId,orderItems.toString()));
        kafkaParameters.add(new KafkaParameter("point",orderId, point.toString()));
        kafkaUtil.sendMessages(kafkaParameters);
        System.out.println("createOrder in service");
    }
}
