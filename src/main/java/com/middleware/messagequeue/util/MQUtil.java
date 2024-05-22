package com.middleware.messagequeue.util;

import com.middleware.messagequeue.util.snowflake.SnowFlakeIdWorker;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import com.middleware.messagequeue.mq.TopicDefinition;

import java.io.UnsupportedEncodingException;

@Component
public class MQUtil {


    @Autowired
    private SnowFlakeIdWorker snowFlakeIdWorker;

    @Autowired
    DefaultMQProducer rocketMQProducer;

    public void sendMessageRocketMQWithIdempotent(String messageContent, String topic) throws Exception {
        // 构建消息内容
        byte[] messageBody = messageContent.getBytes(RemotingHelper.DEFAULT_CHARSET);

        // 构建消息实例
        Message message = new Message(topic, messageBody);

        // 设置自定义的消息ID
        String messageId = String.valueOf(snowFlakeIdWorker.nextId());
        message.setKeys(messageId);

        // 发送消息
        SendResult sendResult = rocketMQProducer.send(message);

        // 根据发送结果判断是否发送成功
        if (sendResult.getSendStatus() == SendStatus.SEND_OK) {
            System.out.println("消息发送成功");
        } else {
            System.out.println("消息发送失败");
        }
    }


    //其他mq 幂等和顺序消费

}
