package com.middleware.messagequeue.mq;

import com.middleware.messagequeue.aop.OnMsgIdept;
import com.middleware.messagequeue.util.*;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@RocketMQMessageListener(topic = TopicDefinition.ROCKET_MQ_IDEMPOTENT_TOPIC,consumerGroup = "order")
public class MQOrderListener implements RocketMQListener<MessageExt> {
    private final Logger logger = LoggerFactory.getLogger(MQOrderListener.class);
    @OnMsgIdept
    @Override
    public void onMessage(MessageExt message) {
//        System.out.println("消费者收到消息,keys:"+message.getKeys());
//        Idempotent idempotent = SpringContextUtil.getBean(Idempotent.class);
//        // 获取消息key
//        String keys = message.getKeys();
//
//        // 判断消息是否已处理过
//        if (idempotent.consumed(keys)) {
//            // 如果已处理过，则直接返回成功
//            System.out.println("already consumed");
//            return;
//        }
//
//        // 处理消息的业务逻辑
//        boolean success = true;
//
//        // 根据处理结果更新记录
//        if (success) {
//            idempotent.addConsumed(keys);
//            System.out.println("consumed successfully");
//        }else{
//            System.out.println("consume fail");
//        }

        logger.info("onMessage 执行业务逻辑");
    }
}
