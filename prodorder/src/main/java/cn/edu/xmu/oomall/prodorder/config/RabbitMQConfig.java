package cn.edu.xmu.oomall.prodorder.config;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.HashMap;

@Configuration
public class RabbitMQConfig {

    public static String getOrderMqExchange() {
        return "order-exchange";
    }

    public static String getOrderMqPrefix(Long queueId) {
        return "order-queue-" + queueId.toString();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(CachingConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jackson2JsonMessageConverter());
        return rabbitTemplate;
    }

    @Bean
    public MessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    /**
     * 创建两个队列，处理顺序消息
     */
    @Bean
    public Queue orderQueue1() {
        return createQueue("order-queue-1");
    }

    @Bean
    public Queue orderQueue2() {
        return createQueue("order-queue-2");
    }

    // 交换机
    @Bean
    public DirectExchange seqDirectExchange() {
        return new DirectExchange("order-exchange");
    }

    // 队列绑定交换机，执行路由key
    @Bean
    public Binding orderQueueBinding1() {
        return BindingBuilder.bind(orderQueue1()).to(seqDirectExchange()).with("order-queue-1");
    }

    @Bean
    public Binding orderQueueBinding2() {
        return BindingBuilder.bind(orderQueue2()).to(seqDirectExchange()).with("order-queue-2");
    }

    /**
     * 辅助函数
     * 创建一个 单活模式的队列
     * @param name
     * @return queue
     */
    private Queue createQueue(String name) {
        HashMap<String, Object> args = new HashMap<>();
        // x-single-active-consumer 单活模式 队列
        // 表示是否最多只允许一个消费者消费，如果有多个消费者同时绑定，则只会激活第一个，
        // 除非第一个消费者被取消或者死亡，才会自动转到下一个消费者。
        args.put("x-single-active-consumer", true);
        return new Queue(name, true, false, false, args);
    }
}
