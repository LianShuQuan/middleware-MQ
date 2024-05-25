package cn.edu.xmu.oomall.prodorder.dao.bo;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.oomall.prodorder.controller.vo.PayInfoVo;

import java.util.List;
@CopyFrom({PayInfoVo.class})
public class PayInfo {
    private Integer point;
    private Long shopChannelId;
    private List<CouponOrderItem> couponOrderItemList;

    public Integer getPoint() {
        return point;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }

    public Long getShopChannelId() {
        return shopChannelId;
    }

    public void setShopChannelId(Long shopChannelId) {
        this.shopChannelId = shopChannelId;
    }

    public List<CouponOrderItem> getCouponOrderItemList() {
        return couponOrderItemList;
    }

    @CopyFrom.Exclude({PayInfoVo.class})
    public void setCouponOrderItemList(List<CouponOrderItem> couponOrderItemList) {
        this.couponOrderItemList = couponOrderItemList;
    }
}
