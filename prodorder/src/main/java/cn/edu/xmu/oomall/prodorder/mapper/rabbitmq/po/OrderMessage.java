package cn.edu.xmu.oomall.prodorder.mapper.rabbitmq.po;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.oomall.prodorder.dao.bo.Order;

import java.util.List;

@CopyFrom({Order.class})
public class OrderMessage {

    private String objectId;

    private List<OrderItemMessage> orderItems;

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public List<OrderItemMessage> getOrderItems() {
        return orderItems;
    }

    @CopyFrom.Exclude({Order.class})
    public void setOrderItems(List<OrderItemMessage> orderItems) {
        this.orderItems = orderItems;
    }
}
