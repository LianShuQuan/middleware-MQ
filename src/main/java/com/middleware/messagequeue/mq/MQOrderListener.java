package com.middleware.messagequeue.mq;

import com.middleware.messagequeue.util.CtxAware;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Service;

@Service
@RocketMQMessageListener(topic = TopicDefinition.ROCKET_MQ_IDEMPOTENT_TOPIC,consumerGroup = "order")
public class MQOrderListener implements RocketMQListener<MessageExt> {

    @Override
    public void onMessage(MessageExt message) {
        System.out.println("消费者收到消息");
        Idempotent idempotent = CtxAware.getApplicationContext().getBean(Idempotent.class);
        // 获取消息ID
        String messageId = message.getMsgId();

        // 判断消息是否已处理过
        if (idempotent.consumed(messageId)) {
            // 如果已处理过，则直接返回成功
            System.out.println("already consumed");
            return;
        }

        // 处理消息的业务逻辑
        boolean success = true;

        // 根据处理结果更新记录
        if (success) {
            idempotent.addConsumed(messageId);
            System.out.println("consumed successfully");
        }else{
            System.out.println("consume fail");
        }
    }
}
