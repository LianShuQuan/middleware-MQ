package cn.edu.xmu.oomall.prodorder.dao.bo.state;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.prodorder.dao.bo.Order;

//已完成
public class FinishState implements StateInf {
    private final int code = FINISH;

    @Override
    public boolean canStateChange(int orderState) {
        return toState.get(this.code).contains(orderState);
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public void cancelOrder(Order order,UserDto user ) {
        if (!canStateChange(CANCEL) && !canStateChange(WAIT_REFUND)) {
            throw new BusinessException(ReturnNo.STATENOTALLOW, String.format(ReturnNo.STATENOTALLOW.getMessage().replace("%d", "%s"), "订单", order.getObjectId(), STATENAMES.get(code)));
        }
    }
    @Override
    public void cancelOrderByShop(Order order,UserDto user ) {
        if (!canStateChange(CANCEL) && !canStateChange(WAIT_REFUND)) {
            throw new BusinessException(ReturnNo.STATENOTALLOW, String.format(ReturnNo.STATENOTALLOW.getMessage().replace("%d", "%s"), "订单", order.getObjectId(), STATENAMES.get(code)));
        }
    }
}
