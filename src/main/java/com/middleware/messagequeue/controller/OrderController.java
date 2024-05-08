package com.middleware.messagequeue.controller;

import com.middleware.messagequeue.service.OrderService;
import com.middleware.messagequeue.util.MQUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {


    @Autowired
    private OrderService service;

    @PostMapping("/")
    public ResponseEntity<String> createOrder() throws Exception {
        service.createOrder();
        return ResponseEntity.ok("ok");
    }
}