package com.middleware.messagequeue.mq;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static java.lang.Thread.sleep;

@Component
public class KafkaListener {
   @Autowired
   Idempotent idempotent;

    @org.springframework.kafka.annotation.KafkaListener(topics = TopicDefinition.KAFKA_MQ_PRODUCT_TOPIC, groupId = "0")
    public void handleProductMessage(ConsumerRecord<String, String> record) {
        String messageId = record.key(); // 获取消息的 key，即消息的 ID
        String[] processedID = processID(messageId);
        String messageIndex = processedID[0];
        String endLabel = processedID[1];
        String orderId = processedID[2];
        String messageContent = record.value(); // 获取消息内
        if(idempotent.getValueById(orderId) == null){
            idempotent.setValue(orderId,"0");
        }
            try {

                while(idempotent.getValueById(orderId) == messageIndex){
                    sleep(10);
                }
                if(endLabel == "0"){
                    int nextMessageIndex = Integer.parseInt(messageIndex) + 1;
                    idempotent.updateValue(orderId,String.valueOf(nextMessageIndex));
                }
                else{
                    idempotent.deleteValue(orderId);
                }
                System.out.println("consume product");
                //模拟业务处理
                sleep(1000);
            } catch (Exception e) {
                System.out.println("处理订单商品消息时出现错误：" + e.getMessage());
            }


    }
    @org.springframework.kafka.annotation.KafkaListener(topics = TopicDefinition.KAKFA_MQ_COUPON_TOPIC, groupId = "0")
    public void handleCouponMessage(ConsumerRecord<String, String> record) {
        String messageId = record.key(); // 获取消息的 key，即消息的 ID
        String[] processedID = processID(messageId);
        String messageIndex = processedID[0];
        String endLabel = processedID[1];
        String orderId = processedID[2];
        String messageContent = record.value(); // 获取消息内
        if(idempotent.getValueById(orderId) == null){
            idempotent.setValue(orderId,"0");
        }
        try {

            while(idempotent.getValueById(orderId) == messageIndex){
                sleep(10);
            }
            if(endLabel == "0"){
                int nextMessageIndex = Integer.parseInt(messageIndex) + 1;
                idempotent.updateValue(orderId,String.valueOf(nextMessageIndex));
            }
            else{
                idempotent.deleteValue(orderId);
            }
            System.out.println("consume coupon");
            //模拟业务处理
            sleep(1000);
        } catch (Exception e) {
            System.out.println("处理订单商品消息时出现错误：" + e.getMessage());
        }


    }

    @org.springframework.kafka.annotation.KafkaListener(topics = TopicDefinition.KAFKA_MQ_POINT_TOPIC, groupId = "0")
    public void handlePointMessage(ConsumerRecord<String, String> record) {
        String messageId = record.key(); // 获取消息的 key，即消息的 ID
        String[] processedID = processID(messageId);
        String messageIndex = processedID[0];
        String endLabel = processedID[1];
        String orderId = processedID[2];
        String messageContent = record.value(); // 获取消息内
        if(idempotent.getValueById(orderId) == null){
            idempotent.setValue(orderId,"0");
        }
        try {

            while(idempotent.getValueById(orderId) == messageIndex){
                sleep(10);
            }
            if(endLabel == "0"){
                int nextMessageIndex = Integer.parseInt(messageIndex) + 1;
                idempotent.updateValue(orderId,String.valueOf(nextMessageIndex));
            }
            else{
                idempotent.deleteValue(orderId);
            }
            System.out.println("cosume point");
            //模拟业务处理
            sleep(1000);
        } catch (Exception e) {
            System.out.println("处理订单商品消息时出现错误：" + e.getMessage());
        }


    }
    public String[] processID(String id) {
        String[] result = new String[3];
        int length =id.length();
        result[0]=id.substring(length-3,length-2);
        result[1]=id.substring(length-2,length-1);
        result[2]=id.substring(0,length-2);
        return result;
    }
}

