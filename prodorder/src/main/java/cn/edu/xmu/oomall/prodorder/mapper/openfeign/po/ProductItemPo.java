package cn.edu.xmu.oomall.prodorder.mapper.openfeign.po;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ProductItemPo {
    private String orderItemId;
    private Long productId;
    private Integer quantity;

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
}
