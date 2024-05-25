package cn.edu.xmu.oomall.prodorder.dao.openfeign.product.bo;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.oomall.prodorder.dao.bo.OrderItem;
import cn.edu.xmu.oomall.prodorder.mapper.openfeign.po.ProductItemPo;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@CopyFrom({ProductItemPo.class})
public class ProductItem {

    private String orderItemId;
    private Long productId;
    private Integer quantity;
    private Integer weight;

    public ProductItem(OrderItem oi) {
        this.setProductId(oi.getProduct().getId());
        this.setOrderItemId(oi.getObjectId());
        this.setQuantity(oi.getQuantity());
        this.setWeight(oi.getProduct().getWeight());
    }

    public String getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(String orderItemId) {
        this.orderItemId = orderItemId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }
}
