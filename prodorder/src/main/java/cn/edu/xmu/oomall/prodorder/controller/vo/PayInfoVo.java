package cn.edu.xmu.oomall.prodorder.controller.vo;

import lombok.NoArgsConstructor;

import java.util.List;


@NoArgsConstructor
public class PayInfoVo {
    private Integer point;

    public Long getShopChannelId() {
        return shopChannelId;
    }

    public void setShopChannelId(Long shopChannelId) {
        this.shopChannelId = shopChannelId;
    }

    private Long shopChannelId;

    public List<CouponOrderItemVo> getCouponOrderItemList() {
        return couponOrderItemList;
    }

    public void setCouponOrderItemList(List<CouponOrderItemVo> couponOrderItemList) {
        this.couponOrderItemList = couponOrderItemList;
    }

    private List<CouponOrderItemVo> couponOrderItemList;

    public Integer getPoint() {
        return point;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }
}
