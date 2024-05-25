package cn.edu.xmu.oomall.prodorder.mapper.rabbitmq;

import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.javaee.core.util.JacksonUtil;
import cn.edu.xmu.oomall.prodorder.dao.bo.IdName;
import cn.edu.xmu.oomall.prodorder.mapper.rabbitmq.po.OrderMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
public class ProductMQMapper {

    private final static Logger logger = LoggerFactory.getLogger(ProductMQMapper.class);

    private RabbitTemplate rabbitTemplate;

    @Autowired
    @Lazy
    public ProductMQMapper(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     *  创建订单时通知产品模块减库存
     */
    public void decrInventory(OrderMessage orderMessage, IdName user) {
        String orderStr = JacksonUtil.toJson(orderMessage);
        assert orderStr != null;
        Message msg = MessageBuilder.withPayload(orderStr).setHeader("user", user).build();

        Long queueId = (user.getId() + 1) % 2 + 1;  // 由于同一订单id的订单其顾客id也必然相同，所以可由顾客id决定送往哪一个消息队列
        String queueName = "order-queue-" + queueId;
        logger.info("order({}) decrement inventory through {}", orderStr, queueName);
        rabbitTemplate.convertAndSend("order-exchange", queueName, msg);
    }

    /**
     *  取消订单时通知产品模块增加库存
     */
    public void incrInventory(OrderMessage orderMessage, UserDto user) {
        String orderStr = JacksonUtil.toJson(orderMessage);
        assert orderStr != null;
        Message msg = MessageBuilder.withPayload(orderStr).setHeader("user", user).build();

        Long queueId = (user.getId() + 1) % 2 + 1;  // 由于同一订单id的订单其顾客id也必然相同，所以可由顾客id决定送往哪一个消息队列
        String queueName = "order-queue-" + queueId;
        logger.info("order({}) increment inventory through {}", orderStr, queueName);
        rabbitTemplate.convertAndSend("order-exchange", queueName, msg);
    }
}
