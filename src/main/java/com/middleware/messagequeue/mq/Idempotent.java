package com.middleware.messagequeue.mq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class Idempotent {
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    public boolean consumed(String messageId)
    {
        if(!this.stringRedisTemplate.hasKey(messageId))
            return false;
        return this.stringRedisTemplate.opsForValue().get(messageId).equals("true");
    }

    public void addConsumed(String messageId) {
        this.stringRedisTemplate.opsForValue().set(messageId, "true", Duration.ofMinutes(5));
    }
}
