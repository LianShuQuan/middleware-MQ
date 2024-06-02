package com.middleware.messagequeue.controller;

import ch.qos.logback.core.spi.ContextAware;
import com.middleware.messagequeue.mq.Idempotent;
import com.middleware.messagequeue.mq.MQOrderListener;
import com.middleware.messagequeue.mq.TopicDefinition;
import com.middleware.messagequeue.service.OrderService;
import com.middleware.messagequeue.util.CtxAware;
import com.middleware.messagequeue.util.SpringContextUtil;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class DebugController {

    @Autowired
    DefaultMQProducer rocketMQProducer;
    private final Logger logger = LoggerFactory.getLogger(DebugController.class);

    @GetMapping("/debug")
    public ResponseEntity<String> createOrder() throws Exception {
        MQOrderListener bean = SpringContextUtil.getBean(MQOrderListener.class);
        System.out.println(bean);
        System.out.println(SpringContextUtil.getBean(Idempotent.class));
        return ResponseEntity.ok("ok");
    }


    @GetMapping("/order/{orderId}")
    public ResponseEntity<String> createOrder2(@PathVariable("orderId") String orderId) throws Exception {
        // 构建消息内容
        String messageContent = "test send message with orderId:" + orderId;
        String topic = TopicDefinition.ROCKET_MQ_IDEMPOTENT_TOPIC;
        byte[] messageBody = messageContent.getBytes(RemotingHelper.DEFAULT_CHARSET);

        // 构建消息实例
        Message message = new Message(topic, messageBody);
        message.setKeys(orderId);

        // 发送消息
        SendResult sendResult = rocketMQProducer.send(message);

        // 根据发送结果判断是否发送成功
        if (sendResult.getSendStatus() == SendStatus.SEND_OK) {
            logger.info("消息发送成功");
        } else {
            logger.info("消息发送失败");
        }

        return ResponseEntity.ok("ok");
    }
}