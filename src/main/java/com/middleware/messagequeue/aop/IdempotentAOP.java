package com.middleware.messagequeue.aop;

import com.middleware.messagequeue.mq.Idempotent;
import com.middleware.messagequeue.mq.MQOrderListener;
import com.middleware.messagequeue.util.SpringContextUtil;
import org.apache.rocketmq.common.message.MessageExt;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class IdempotentAOP {
    private final Logger logger = LoggerFactory.getLogger(IdempotentAOP.class);

    @Pointcut("@annotation(com.middleware.messagequeue.aop.OnMsgIdept)")
    public void idempotentAspect(){}

    @Around("com.middleware.messagequeue.aop.IdempotentAOP.idempotentAspect()")
    public void onMessageIdempotent(ProceedingJoinPoint jp) throws Throwable {
        MessageExt message = (MessageExt) jp.getArgs()[0];
        logger.info("消费者收到消息,keys:{}", message.getKeys());
        Idempotent idempotent = SpringContextUtil.getBean(Idempotent.class);
        // 获取消息key
        String keys = message.getKeys();

        // 判断消息是否已处理过
        if (idempotent.consumed(keys)) {
            // 如果已处理过，则直接返回
            logger.info("消息已处理过，直接返回");
            return;
        }

        jp.proceed(jp.getArgs());


        idempotent.addConsumed(keys);
        logger.info("消息处理成功");

    }
}
