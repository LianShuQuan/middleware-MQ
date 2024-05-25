//School of Informatics Xiamen University, GPL-3.0 license

package cn.edu.xmu.oomall.product.service.listener;

import cn.edu.xmu.javaee.core.util.JacksonUtil;
import cn.edu.xmu.oomall.product.dao.bo.Order;
import cn.edu.xmu.oomall.product.service.OnsaleService;
import cn.edu.xmu.oomall.product.service.listener.vo.MessageWithPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

/**
 * 取消订单消息
 */
@Service
//@RabbitListener(queues = { "order-queue-1" })
public class RevokeOrderConsumer implements MessageListener {

    private static final Logger logger = LoggerFactory.getLogger(RevokeOrderConsumer.class);

    private OnsaleService onsaleService;

    @Autowired
    public RevokeOrderConsumer(OnsaleService onsaleService) {
        this.onsaleService = onsaleService;
    }

    @RabbitHandler
    public void onMessage(Message message) {
//        long l = new Random(1000).nextLong();
//        Thread.sleep(l);
        MessageWithPayload messageWithPayload = JacksonUtil.toObj(
                new String(message.getBody(), StandardCharsets.UTF_8),
                MessageWithPayload.class
        );
        String payload = messageWithPayload.getPayload();
        logger.info("RevokeOrder: got message, order = {}", payload);

        Order order = JacksonUtil.toObj(payload, Order.class);
        if (null == order || null == order.getOrderItems()){
            logger.error("RevokeOrder: wrong format.... content = {}",order);
        } else {
//                order.getOrderItems().stream().forEach(item -> {
//                    this.onsaleService.incrQuantity(item.getId(), item.getQuantity());
//                });
        }
    }

}
