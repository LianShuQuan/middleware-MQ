package cn.edu.xmu.oomall.prodorder.mapper.openfeign.po;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CouponPo {
    private Long id;
    private Long activityId;
    private String name;
    private String couponSn;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCouponSn() {
        return couponSn;
    }

    public void setCouponSn(String couponSn) {
        this.couponSn = couponSn;
    }
}
