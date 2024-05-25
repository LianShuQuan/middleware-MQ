package cn.edu.xmu.oomall.prodorder.dao.bo.state;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.prodorder.dao.bo.Order;

public class WaitRefundState implements StateInf {
    private final int code = WAIT_REFUND;

    @Override
    public boolean canStateChange(int orderState) {
        return toState.get(this.code).contains(orderState);
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public void cancelOrder(Order order, UserDto user) {
        //待退款状态可以变成已取消，但是对于一个待退款的订单，取消订单的操作是不允许的
        if (!canStateChange(CANCEL) && !canStateChange(WAIT_REFUND)) {
            throw new BusinessException(ReturnNo.STATENOTALLOW, String.format(ReturnNo.STATENOTALLOW.getMessage().replace("%d", "%s"), "订单", order.getObjectId(), STATENAMES.get(code)));
        }
        throw new BusinessException(ReturnNo.STATENOTALLOW, String.format(ReturnNo.STATENOTALLOW.getMessage().replace("%d", "%s"), "订单", order.getObjectId(), STATENAMES.get(code)));
    }
    @Override
    public void cancelOrderByShop(Order order, UserDto user) {
        //待退款状态可以变成已取消，但是对于一个待退款的订单，取消订单的操作是不允许的
        if (!canStateChange(CANCEL) && !canStateChange(WAIT_REFUND)) {
            throw new BusinessException(ReturnNo.STATENOTALLOW, String.format(ReturnNo.STATENOTALLOW.getMessage().replace("%d", "%s"), "订单", order.getObjectId(), STATENAMES.get(code)));
        }
        throw new BusinessException(ReturnNo.STATENOTALLOW, String.format(ReturnNo.STATENOTALLOW.getMessage().replace("%d", "%s"), "订单", order.getObjectId(), STATENAMES.get(code)));
    }
}
