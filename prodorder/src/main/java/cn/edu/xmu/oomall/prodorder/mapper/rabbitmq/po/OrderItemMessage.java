package cn.edu.xmu.oomall.prodorder.mapper.rabbitmq.po;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.oomall.prodorder.dao.bo.OrderItem;

@CopyFrom({OrderItem.class})
public class OrderItemMessage {
    private String objectId;  // CopyFrom没有处理
    private String name;
    private Long categoryId;
    private Long onsaleId;
    private Integer quantity;
    private Integer price;
    private Integer discount;  // CopyFrom没有处理
    private Long couponActivityId;  // CopyFrom没有处理

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getOnsaleId() {
        return onsaleId;
    }

    public void setOnsaleId(Long onsaleId) {
        this.onsaleId = onsaleId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }

    public Long getCouponActivityId() {
        return couponActivityId;
    }

    public void setCouponActivityId(Long couponActivityId) {
        this.couponActivityId = couponActivityId;
    }
}
