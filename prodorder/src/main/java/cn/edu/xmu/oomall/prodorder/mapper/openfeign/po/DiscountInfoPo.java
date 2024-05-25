package cn.edu.xmu.oomall.prodorder.mapper.openfeign.po;

import cn.edu.xmu.oomall.prodorder.dao.openfeign.product.bo.DiscountItem;
import lombok.AllArgsConstructor;

import java.util.List;


@AllArgsConstructor
public class DiscountInfoPo {
    private List<DiscountItem> discountItems;

    public List<DiscountItem> getDiscountItems() {
        return discountItems;
    }

    public void setDiscountItems(List<DiscountItem> discountItems) {
        this.discountItems = discountItems;
    }
}
