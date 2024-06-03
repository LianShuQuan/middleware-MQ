package com.middleware.messagequeue.aop;


import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Aspect
@Component
public class IdempotentKafkaAspect {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Pointcut("@annotation(com.middleware.messagequeue.aop.IdempotentKafka)")
    public void doPointcut(){}
    @Around("doPointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        ConsumerRecord<String, String> consumerRecord = (ConsumerRecord<String, String>) args[0];
        Optional<String> key = Optional.ofNullable(consumerRecord.key());
        Optional<String> value=Optional.ofNullable(consumerRecord.value());
        if (key.isPresent()&&value.isPresent()) {
            if (Boolean.TRUE.equals(stringRedisTemplate.hasKey(key.get()))) {
                System.out.println("重复消费");
                System.out.println("不执行消费逻辑");
                return null;  // 如果已经处理过，则不继续执行
            } else {
                stringRedisTemplate.opsForValue().set(key.get(),value.get());
                stringRedisTemplate.expire(key.get(),60, TimeUnit.SECONDS);
            }
        }
        return joinPoint.proceed();
    }
}
