package cn.edu.xmu.oomall.prodorder.dao.openfeign.payment.bo;

import lombok.AllArgsConstructor;


@AllArgsConstructor
public class PayOrderInfo {
    Long id;
    String outTradeNo;
    String prepayId;
    Integer totalAmount;
    String sellerId;
    String merchantOrderNo;

    public Long getId() {
        return id;
    }

}
