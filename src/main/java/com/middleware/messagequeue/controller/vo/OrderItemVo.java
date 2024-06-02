package com.middleware.messagequeue.controller.vo;
public class OrderItemVo {

    private Long onsaleId;

    private Integer quantity;
    public Long getOnsaleId() {
        return onsaleId;
    }

    public void setOnsaleId(Long onsaleId) {
        this.onsaleId = onsaleId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }


}