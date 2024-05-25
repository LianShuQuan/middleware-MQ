package cn.edu.xmu.oomall.prodorder.dao.bo;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.javaee.core.util.CloneFactory;
import cn.edu.xmu.oomall.prodorder.dao.openfeign.product.bo.ProductItem;
import cn.edu.xmu.oomall.prodorder.mapper.po.OrderPackPo;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@CopyFrom({OrderPackPo.class})
public class OrderPack {
    private String objectId;
    private String orderId;
    private String billCode;
    private List<ProductItem> pack;

    public OrderPack(List<ProductItem> pack) {
        this.setPack(pack);
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getBillCode() {
        return billCode;
    }

    public void setBillCode(String billCode) {
        this.billCode = billCode;
    }

    public List<ProductItem> getPack() {
        return pack;
    }

    public void setPack(List<ProductItem> pack) {
        this.pack = pack;
    }
}
