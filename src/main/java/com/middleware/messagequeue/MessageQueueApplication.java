package com.middleware.messagequeue;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import com.middleware.messagequeue.util.SpringContextUtil;

@SpringBootApplication
public class MessageQueueApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext ac = SpringApplication.run(MessageQueueApplication.class, args);
        SpringContextUtil.setAc(ac);
    }

}



