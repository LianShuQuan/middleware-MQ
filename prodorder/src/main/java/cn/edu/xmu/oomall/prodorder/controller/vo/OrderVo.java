//School of Informatics Xiamen University, GPL-3.0 license

package cn.edu.xmu.oomall.prodorder.controller.vo;

import cn.edu.xmu.oomall.prodorder.dao.openfeign.customer.bo.Consignee;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
public class OrderVo {

    private List<OrderItemVo> orderItems;

    private Consignee consignee;

    private String message;

    public List<OrderItemVo> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItemVo> orderItems) {
        this.orderItems = orderItems;
    }

    public Consignee getConsignee() {
        return consignee;
    }

    public void setConsignee(Consignee consignee) {
        this.consignee = consignee;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
