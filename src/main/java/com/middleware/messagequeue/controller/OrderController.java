package com.middleware.messagequeue.controller;

import com.middleware.messagequeue.controller.vo.OrderItemVo;
import com.middleware.messagequeue.controller.vo.OrderVo;
import com.middleware.messagequeue.service.OrderService;
import com.middleware.messagequeue.service.bo.OrderItem;
import com.middleware.messagequeue.util.MQUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class OrderController {


    @Autowired
    private OrderService service;

    @PostMapping("/")
    public ResponseEntity<String> createOrder() throws Exception {
        service.createOrder();
        return ResponseEntity.ok("ok");
    }

    @PostMapping("/createOrder")
    public ResponseEntity<String> createOrderKafka(@RequestBody @Validated OrderVo orderVo) throws Exception {
        List<OrderItem> orderItems=new ArrayList<>();
        List<String> coupons =orderVo.getCoupons();
        for(OrderItemVo orderItemVo:orderVo.getOrderItemVos()){
            OrderItem orderItem=new OrderItem(orderItemVo);
            orderItems.add(orderItem);
        }
        service.createOrderKafka(orderItems,coupons,orderVo.getPoints());
        return ResponseEntity.ok("ok");
    }
}