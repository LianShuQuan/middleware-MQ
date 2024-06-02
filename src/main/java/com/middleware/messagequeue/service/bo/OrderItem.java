package com.middleware.messagequeue.service.bo;


import com.middleware.messagequeue.controller.vo.OrderItemVo;

public class OrderItem {
    public Long onsaleId;
    public String orderId;
    public String name;
    public Integer quantity;
    public double price;
    public Integer point;
    public OrderItem(OrderItemVo orderItemVo){
        this.onsaleId=orderItemVo.getOnsaleId();
        this.quantity=orderItemVo.getQuantity();
    }

}