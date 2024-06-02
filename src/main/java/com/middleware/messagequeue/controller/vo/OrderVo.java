package com.middleware.messagequeue.controller.vo;

import java.util.List;

public class OrderVo {
    private List<OrderItemVo>orderItemVos;
    private List<String> coupons;
    private Integer points;
    private String message;
    public List<OrderItemVo> getOrderItemVos() {
        return orderItemVos;
    }
    public Integer getPoints(){
        return points;
    }
    public List<String> getCoupons(){
        return coupons;
    }
    public void setOrderItemVos(List<OrderItemVo> orderItemVos) {
        this.orderItemVos = orderItemVos;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
