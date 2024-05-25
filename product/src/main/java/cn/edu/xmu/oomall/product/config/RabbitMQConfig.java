package cn.edu.xmu.oomall.product.config;

import cn.edu.xmu.oomall.product.service.listener.NewOrderConsumer;
import cn.edu.xmu.oomall.product.service.listener.RevokeOrderConsumer;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
//@Import(cn.hutool.extra.spring.SpringUtil.class)  //huTool添加，才能用getBean
public class RabbitMQConfig {
//
//    public static final String QUEUE_NAME = queueName();
//
//    private static String queueName;
//
//    private static String queueName() {
//        return queueName;
//    }
//
//    @Value("order-queue-${spring.rabbitmq.queue-id}")
//    private void setQueueName(String qn) {
//        queueName = qn;
//    }

    @Value("order-queue-${spring.rabbitmq.queue-id}")
    private String queueName;

    @Autowired
    private NewOrderConsumer newOrderConsumer;

    @Autowired
    private RevokeOrderConsumer revokeOrderConsumer;

    @Bean
    public SimpleMessageListenerContainer newOrderConsumerContainer(ConnectionFactory connectionFactory) {
        SimpleMessageListenerContainer container = initSimpleMessageListenerContainer(connectionFactory);
        container.setMessageListener(newOrderConsumer);
        return container;
    }

    @Bean
    public SimpleMessageListenerContainer revokeOrderConsumerContainer(ConnectionFactory connectionFactory) {
        SimpleMessageListenerContainer container = initSimpleMessageListenerContainer(connectionFactory);
        container.setMessageListener(revokeOrderConsumer);
        return container;
    }

    private SimpleMessageListenerContainer initSimpleMessageListenerContainer(ConnectionFactory connectionFactory) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(queueName);
        container.addQueueNames();
        return container;
    }
}