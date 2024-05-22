package com.middleware.messagequeue.controller;

import ch.qos.logback.core.spi.ContextAware;
import com.middleware.messagequeue.mq.Idempotent;
import com.middleware.messagequeue.mq.MQOrderListener;
import com.middleware.messagequeue.service.OrderService;
import com.middleware.messagequeue.util.CtxAware;
import com.middleware.messagequeue.util.SpringContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class DebugController {




    @GetMapping("/debug")
    public ResponseEntity<String> createOrder() throws Exception {
        MQOrderListener bean = SpringContextUtil.getBean(MQOrderListener.class);
        System.out.println(bean);
        System.out.println(SpringContextUtil.getBean(Idempotent.class));
        return ResponseEntity.ok("ok");
    }
}