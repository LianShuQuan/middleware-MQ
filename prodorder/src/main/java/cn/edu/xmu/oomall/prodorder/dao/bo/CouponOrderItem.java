package cn.edu.xmu.oomall.prodorder.dao.bo;


import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.oomall.prodorder.controller.vo.CouponOrderItemVo;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.NoArgsConstructor;



@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@CopyFrom({CouponOrderItemVo.class})
public class CouponOrderItem {
    private Long couponId;

    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    private Long activityId;

    public String getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(String orderItemId) {
        this.orderItemId = orderItemId;
    }

    private String orderItemId;

    public Long getCouponId() {
        return couponId;
    }

    public void setCouponId(Long couponId) {
        this.couponId = couponId;
    }
}
