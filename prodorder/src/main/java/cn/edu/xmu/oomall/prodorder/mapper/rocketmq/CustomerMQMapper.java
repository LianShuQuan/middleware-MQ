package cn.edu.xmu.oomall.prodorder.mapper.rocketmq;

import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.javaee.core.util.JacksonUtil;
import cn.edu.xmu.oomall.prodorder.dao.bo.PayInfo;
import cn.edu.xmu.oomall.prodorder.dao.openfeign.customer.bo.Coupon;
import cn.edu.xmu.oomall.prodorder.listener.vo.PayOrderLocalTransParam;
import cn.edu.xmu.oomall.prodorder.mapper.rocketmq.po.PayOrderMsgPayload;
import cn.edu.xmu.oomall.prodorder.mapper.rocketmq.po.PointAndCouponMessage;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class CustomerMQMapper {

    private final static Logger logger = LoggerFactory.getLogger(CustomerMQMapper.class);

    private RocketMQTemplate rocketMQTemplate;

    @Autowired
    @Lazy
    public CustomerMQMapper(RocketMQTemplate rocketMQTemplate) {
        this.rocketMQTemplate = rocketMQTemplate;
    }


    public void useCouponCreatePayment(PayOrderLocalTransParam localTransParam) {
        List<Coupon> coupons = localTransParam.getCoupons();
        PayInfo payInfo = localTransParam.getPayInfo();

        PayOrderMsgPayload payload = PayOrderMsgPayload.builder().couponList(coupons)
                .couponIdList(coupons.stream().map(Coupon::getId).collect(Collectors.toList()))
                .point(payInfo.getPoint())
                .build();

        Message<PayOrderMsgPayload> msg = MessageBuilder.withPayload(payload).build();
        //this.rocketMQTemplate.sendMessageInTransaction("customer-use-topic", msg, localTransParam);
    }

    public void returnPointAndCoupon(PointAndCouponMessage pointAndCouponMessage, UserDto user) {
        String pointAndCouponStr = JacksonUtil.toJson(pointAndCouponMessage);
        assert pointAndCouponStr != null;
        Message<String> msg = MessageBuilder.withPayload(pointAndCouponStr).setHeader("user", user).build();
        this.rocketMQTemplate.asyncSend("customer-use-topic" + ":" + "oomall", msg, new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                logger.info("returnPointAndCoupon:Success");
            }

            @Override
            public void onException(Throwable throwable) {
                logger.error("returnPointAndCoupon:Fail");
            }
        });
    }
}
