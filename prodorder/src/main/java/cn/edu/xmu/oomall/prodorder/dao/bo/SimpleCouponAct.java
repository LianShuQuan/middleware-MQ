package cn.edu.xmu.oomall.prodorder.dao.bo;


import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.oomall.prodorder.mapper.openfeign.po.CouponAct;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@CopyFrom({CouponAct.class})
public class SimpleCouponAct {
    private Long id;
    private String name;
    private final Byte type = 0;  // 0 优惠 1 团购，由于没有API提供这一属性，此处写死为0

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Byte getType() {
        return type;
    }
}
