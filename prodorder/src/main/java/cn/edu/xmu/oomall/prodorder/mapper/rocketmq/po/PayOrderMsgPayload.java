package cn.edu.xmu.oomall.prodorder.mapper.rocketmq.po;

import cn.edu.xmu.oomall.prodorder.dao.openfeign.customer.bo.Coupon;
import lombok.Builder;

import java.io.Serializable;
import java.util.List;

@Builder
public class PayOrderMsgPayload implements Serializable {
    List<Long> couponIdList;
    Integer point;
    List<Coupon> couponList;
}
