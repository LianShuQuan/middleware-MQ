package cn.edu.xmu.oomall.prodorder.listener;


import cn.edu.xmu.javaee.core.util.JacksonUtil;
import cn.edu.xmu.oomall.prodorder.listener.vo.PayOrderLocalTransParam;
import cn.edu.xmu.oomall.prodorder.service.OrderService;
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RocketMQTransactionListener
public class PayOrderLocalTransactionListener implements RocketMQLocalTransactionListener {

    private final Logger logger = LoggerFactory.getLogger(PayOrderLocalTransactionListener.class);

    private OrderService orderService;

    @Autowired
    public PayOrderLocalTransactionListener(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * 事务消息发送成功回调
     *
     * @param msg
     * @param arg
     * @return
     * @author Lian ShuQuan
     */
    @Override
    @Transactional
    public RocketMQLocalTransactionState executeLocalTransaction(Message msg, Object arg) {
        logger.info("arg:" + JacksonUtil.toJson(arg));
        if (arg instanceof PayOrderLocalTransParam) {

            PayOrderLocalTransParam param = (PayOrderLocalTransParam) arg;
            try {
                param.getOrder().createPayment(param.getCoupons(), param.getPayInfo(), param.getOutTradeNo(), param.getUser());
                logger.info("localTransExecuted");
            }
            catch (Exception e) {
                logger.error("localTransExecuted error", e);
                return RocketMQLocalTransactionState.ROLLBACK;
            }
            return RocketMQLocalTransactionState.COMMIT;

        }
        logger.info("arg is not an instance of PayOrderLocalTransParam!");
        return null;
    }

    @Override
    public RocketMQLocalTransactionState checkLocalTransaction(Message msg) {
        return null;
    }
}