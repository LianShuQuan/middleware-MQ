package com.middleware.messagequeue.mq;

import org.springframework.stereotype.Component;

@Component
public class Idempotent {
    public boolean consumed(String messageId)
    {
        return false;
    }

    public void addConsumed(String messageId) {
    }
}
