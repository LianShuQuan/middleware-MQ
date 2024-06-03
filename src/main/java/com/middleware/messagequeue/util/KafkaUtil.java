package com.middleware.messagequeue.util;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.protocol.types.Field;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@Component
public class KafkaUtil {
    @Autowired
    KafkaTemplate<String,String> kafkaTemplate;

    private String bootstrapServers = "localhost:9092"; // Kafka服务器地址
    private String groupId = "group_id"; // 消费者组ID

    // 创建生产者
    public KafkaProducer<String, String> createProducer() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        return new KafkaProducer<>(props);
    }

    // 发送消息
    public void sendMessages(List<KafkaParameter> kafkaParameterList) {
        KafkaProducer<String, String> producer = createProducer();
        try {
            for(int i = 0;i<kafkaParameterList.size();i++){
                KafkaParameter kafkaParameter = kafkaParameterList.get(i);
                String id = "";
                if(i!= kafkaParameterList.size()-1)id = kafkaParameter.id+i+0;
                else id = kafkaParameter.id+i+1;
                producer.send(new ProducerRecord<>(kafkaParameter.topic,id , kafkaParameter.message));
            }

        } finally {
            producer.close();
        }
    }

    // 创建消费者
    public KafkaConsumer<String, String> createConsumer() {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        return new KafkaConsumer<>(props);
    }

    // 消费消息
    public void consumeMessages(String topic) {
        KafkaConsumer<String, String> consumer = createConsumer();
        consumer.subscribe(Collections.singletonList(topic));
        try {
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
                for (ConsumerRecord<String, String> record : records) {
                    System.out.printf("Consumed record with key %s and value %s%n", record.key(), record.value());
                }
            }
        } finally {
            consumer.close();
        }
    }

    public void consumeMessagesSequentially(String topic) {
        KafkaConsumer<String, String> consumer = createConsumer();
        consumer.subscribe(Collections.singletonList(topic));
        try {
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
                for (ConsumerRecord<String, String> record : records) {
                    System.out.printf("Consumed record with key %s and value %s%n", record.key(), record.value());
                }
            }
        } finally {
            consumer.close();
        }
    }
    public void send(String key, String val, String topic)  {
        kafkaTemplate.send(topic,key,val);
    }
}
