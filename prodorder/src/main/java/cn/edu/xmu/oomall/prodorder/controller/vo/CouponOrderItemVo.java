package cn.edu.xmu.oomall.prodorder.controller.vo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


public class CouponOrderItemVo {

    @NotNull(message = "优惠券id不能为空")
    private Long couponId;

    public String getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(String orderItemId) {
        this.orderItemId = orderItemId;
    }

    @NotBlank(message = "使用对象orderItem不能为空")
    private String orderItemId;

    public Long getCouponId() {
        return couponId;
    }

    public void setCouponId(Long couponId) {
        this.couponId = couponId;
    }
}
