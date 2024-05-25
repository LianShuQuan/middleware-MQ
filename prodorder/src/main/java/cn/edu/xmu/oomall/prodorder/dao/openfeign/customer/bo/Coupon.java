package cn.edu.xmu.oomall.prodorder.dao.openfeign.customer.bo;


import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.javaee.core.util.CloneFactory;
import cn.edu.xmu.oomall.prodorder.mapper.openfeign.po.CouponPo;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@CopyFrom({CouponPo.class})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Coupon {
    private Long id;
    private Activity activity;
    private String name;
    private String couponSn;

    public Coupon (CouponPo couponPo) {
        if (couponPo != null) {
            CloneFactory.copy(this, couponPo);
            this.setActivity(Activity.builder().id(couponPo.getId()).build());
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
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
