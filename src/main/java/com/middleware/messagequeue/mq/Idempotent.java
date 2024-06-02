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
        return this.stringRedisTemplate.opsForValue().get(messageId).equals("true");
    }

    public void addConsumed(String messageId) {
        this.stringRedisTemplate.opsForValue().set(messageId, "true", Duration.ofMinutes(5));
    }
    public String getValueById(String id) {
        return stringRedisTemplate.opsForValue().get(id);
    }

    public void setValue(String id, String value) {
        stringRedisTemplate.opsForValue().set(id, value);
    }

    public void updateValue(String id, String newValue) {
        stringRedisTemplate.opsForValue().set(id, newValue);
    }
    public void deleteValue(String id) {
        stringRedisTemplate.delete(id);
    }
}
