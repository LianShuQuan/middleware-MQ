package cn.edu.xmu.oomall.prodorder.listener.vo;

import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.prodorder.dao.bo.Order;

import cn.edu.xmu.oomall.prodorder.dao.openfeign.customer.bo.Coupon;
import cn.edu.xmu.oomall.prodorder.dao.bo.PayInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class PayOrderLocalTransParam {
    Order order;
    List<Coupon> coupons;
    PayInfo payInfo;
    String outTradeNo;
    UserDto user;
}
