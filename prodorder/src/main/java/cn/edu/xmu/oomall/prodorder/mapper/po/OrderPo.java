//School of Informatics Xiamen University, GPL-3.0 license

package cn.edu.xmu.oomall.prodorder.mapper.po;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.oomall.prodorder.dao.bo.IdName;
import cn.edu.xmu.oomall.prodorder.dao.bo.Order;
import cn.edu.xmu.oomall.prodorder.dao.openfeign.customer.bo.Consignee;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.LocalDateTime;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "prodorder_order")
@CopyFrom({Order.class})
public class OrderPo {

    @MongoId
    private String objectId;

    private String orderSn;

    private IdName customer;

    private IdName shop;

    private Integer originPrice;

    private Integer discountPrice;

    private Integer expressFee;

    private String message;

    private Consignee consignee;

    private List<Long> payTransIdList;

    private int status;

    /**
     * 创建者id
     */
    private Long creatorId;

    /**
     * 创建者
     */
    private String creatorName;

    /**
     * 创建时间
     */
    private LocalDateTime gmtCreate;

    /**
     * 修改者id
     */
    private Long modifierId;

    /**
     * 修改者
     */
    private String modifierName;

    /**
     * 修改时间
     */
    private LocalDateTime gmtModified;

    private Integer point;

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getOrderSn() {
        return orderSn;
    }

    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn;
    }

    public IdName getCustomer() {
        return customer;
    }

    public void setCustomer(IdName customer) {
        this.customer = customer;
    }

    public IdName getShop() {
        return shop;
    }

    public void setShop(IdName shop) {
        this.shop = shop;
    }

    public Integer getOriginPrice() {
        return originPrice;
    }

    public void setOriginPrice(Integer originPrice) {
        this.originPrice = originPrice;
    }

    public Integer getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(Integer discountPrice) {
        this.discountPrice = discountPrice;
    }

    public Integer getExpressFee() {
        return expressFee;
    }

    public void setExpressFee(Integer expressFee) {
        this.expressFee = expressFee;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Consignee getConsignee() {
        return consignee;
    }

    public void setConsignee(Consignee consignee) {
        this.consignee = consignee;
    }

    public List<Long> getPayTransIdList() {
        return payTransIdList;
    }

    public void setPayTransIdList(List<Long> payTransIdList) {
        this.payTransIdList = payTransIdList;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public LocalDateTime getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(LocalDateTime gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Long getModifierId() {
        return modifierId;
    }

    public void setModifierId(Long modifierId) {
        this.modifierId = modifierId;
    }

    public String getModifierName() {
        return modifierName;
    }

    public void setModifierName(String modifierName) {
        this.modifierName = modifierName;
    }

    public LocalDateTime getGmtModified() {
        return gmtModified;
    }

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
