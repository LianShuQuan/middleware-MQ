package com.middleware.messagequeue.util;

import com.middleware.messagequeue.mq.Idempotent;
import com.middleware.messagequeue.mq.TopicDefinition;
import com.middleware.messagequeue.util.snowflake.Common;
import com.middleware.messagequeue.util.snowflake.SnowFlakeIdWorker;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

@Configuration
public class Config {

    private static Long dataCenterId = 0L;
    private static String NAMESRV_ADDR = "127.0.0.1:9876";
    @Bean
    public SnowFlakeIdWorker snowFlakeIdWorker(){
        if (this.dataCenterId > SnowFlakeIdWorker.maxDatacenterId){
            throw new IllegalArgumentException("oomall.datacenter大于最大值"+SnowFlakeIdWorker.maxDatacenterId);
        }

        InetAddress ip = null;
        try {
            ip = Inet4Address.getLocalHost();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
        String ipAddress = ip.getHostAddress();
        Long ipLong = Common.ipToLong(ipAddress);
        Long workerId = ipLong % SnowFlakeIdWorker.maxWorkerId;
        return new SnowFlakeIdWorker(workerId, this.dataCenterId);
    }

    @Bean
    public DefaultMQProducer rocketMQProducer() throws MQClientException {
        DefaultMQProducer defaultMQProducer = new DefaultMQProducer();
        defaultMQProducer.setNamesrvAddr(NAMESRV_ADDR);

        // 启动生产者
        defaultMQProducer.start();
        return defaultMQProducer;
    }



    @Bean(initMethod = "start", destroyMethod = "shutdown")
    public DefaultMQPushConsumer rocketMQConsumer() throws Exception {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("YourConsumerGroup");
        consumer.setNamesrvAddr(NAMESRV_ADDR);
        // 设置订阅的topic和tag
        consumer.subscribe(TopicDefinition.ROCKET_MQ_IDEMPOTENT_TOPIC, "");
        // 注册消息监听器
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> messages, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
                Idempotent idempotent = CtxAware.getApplicationContext().getBean(Idempotent.class);
                for (MessageExt message : messages) {
                    // 获取消息ID
                    String messageId = message.getMsgId();

                    // 判断消息是否已处理过
                    if (idempotent.consumed(messageId)) {
                        // 如果已处理过，则直接返回成功
                        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                    }

                    // 处理消息的业务逻辑
                    boolean success = processMessage(message);

                    // 根据处理结果更新记录
                    if (success) {
                        idempotent.addConsumed(messageId);
                    }
                }

                // 返回成功消费标识
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        return consumer;
    }

}
