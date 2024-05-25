package cn.edu.xmu.oomall.prodorder.dao.bo;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.javaee.core.model.bo.OOMallObject;
import cn.edu.xmu.oomall.prodorder.controller.vo.OrderItemVo;
import cn.edu.xmu.oomall.prodorder.dao.openfeign.customer.bo.Coupon;
import cn.edu.xmu.oomall.prodorder.dao.openfeign.product.bo.Product;
import cn.edu.xmu.oomall.prodorder.mapper.po.OrderItemPo;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@CopyFrom({OrderItemVo.class, OrderItemPo.class})
public class OrderItem extends OOMallObject implements Serializable {

    private String objectId;

    private Long onsaleId;

    private Product product;

    private String orderId;

    private String name;

    private Integer quantity;

    private Integer price;

    private Integer discountPrice;

    private Long actId;

    private Coupon coupon;  // 优惠券直接用在OrderItem上，且一个OrderItem至多用1张

    private Integer point;

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public Long getOnsaleId() {
        return onsaleId;
    }

    public void setOnsaleId(Long onsaleId) {
        this.onsaleId = onsaleId;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Integer getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(Integer discountPrice) {
        this.discountPrice = discountPrice;
    }

    public Long getActId() {
        return actId;
    }

    public void setActId(Long actId) {
        this.actId = actId;
    }

    public Coupon getCoupon() {
        return coupon;
    }

    @CopyFrom.Exclude({OrderItemPo.class})
    public void setCoupon(Coupon coupon) {
        this.coupon = coupon;
    }

    @Override
    public void setGmtCreate(LocalDateTime gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    @Override
    public void setGmtModified(LocalDateTime gmtModified) {
        this.gmtModified = gmtModified;
    }

    public Integer getPoint() {
        return point;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }
}
