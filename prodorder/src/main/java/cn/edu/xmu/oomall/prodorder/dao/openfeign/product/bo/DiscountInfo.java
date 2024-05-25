package cn.edu.xmu.oomall.prodorder.dao.openfeign.product.bo;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.oomall.prodorder.dao.bo.ItemInfo;
import cn.edu.xmu.oomall.prodorder.mapper.openfeign.po.DiscountInfoPo;

import java.util.List;

@CopyFrom({DiscountInfoPo.class})
public class DiscountInfo extends ItemInfo {

    private Integer originPrice;

    private List<DiscountItem> discountItems;

    public Integer getDiscountByOnsaleId(Long onsaleId) {
        return discountItems.stream().filter(discountItem -> onsaleId.equals(discountItem.getId())).toList().get(0).getDiscount();
    }

    public DiscountInfo() { }

    public DiscountInfo(Integer discountPrice, Integer originPrice, List<DiscountItem> discountItems){
        super(discountPrice);
        this.setOriginPrice(originPrice);
        this.setDiscountItems(discountItems);
    }

    public Integer getOriginPrice() {
        return originPrice;
    }

    public void setOriginPrice(Integer originPrice) {
        this.originPrice = originPrice;
    }

    public List<DiscountItem> getDiscountItems() {
        return discountItems;
    }

    public void setDiscountItems(List<DiscountItem> discountItems) {
        this.discountItems = discountItems;
    }
}
