package cn.edu.xmu.oomall.product.service.listener;

import cn.edu.xmu.javaee.core.util.JacksonUtil;
import cn.edu.xmu.oomall.product.dao.bo.Order;
import cn.edu.xmu.oomall.product.service.listener.vo.MessageWithPayload;
import cn.edu.xmu.oomall.product.service.OnsaleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
//@RabbitListener(queues = { "order-queue-1" })
public class NewOrderConsumer implements MessageListener {

    private static final Logger logger = LoggerFactory.getLogger(NewOrderConsumer.class);

    private OnsaleService onsaleService;

    @Autowired
    public NewOrderConsumer(OnsaleService onsaleService) {
        this.onsaleService = onsaleService;
    }

    @Override
    @RabbitHandler
    public void onMessage(Message message) {
        // 随机休眠，模拟分布式场景
//        long l = new Random(1000).nextLong();
//        Thread.sleep(l);

        MessageWithPayload messageWithPayload = JacksonUtil.toObj(
                new String(message.getBody(), StandardCharsets.UTF_8),
                MessageWithPayload.class
        );
        String payload = messageWithPayload.getPayload();
        logger.info("NewOrder: got message, order = {}", payload);

        Order order = JacksonUtil.toObj(payload, Order.class);
        if (order == null || order.getOrderItems() == null) {
            logger.error("NewOrder: message wrong format.... content = {}", order);
        } else {
//            order.getOrderItems().forEach(item -> {
//                onsaleService.incrQuantity(item.getId(), -1 * item.getQuantity());
//            });
        }
    }

}