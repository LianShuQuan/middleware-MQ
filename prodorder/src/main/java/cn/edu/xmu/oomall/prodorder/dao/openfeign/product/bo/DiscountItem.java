package cn.edu.xmu.oomall.prodorder.dao.openfeign.product.bo;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DiscountItem {
    private Long id;    // 销售id
    private Integer quantity;
    private Integer price;
    private Integer discount;  // 优惠后价格

    public Long getId() {
        return id;
    }

    public Integer getPrice() {
        return price;
    }

    public Integer getDiscount() {
        return discount;
    }
}
