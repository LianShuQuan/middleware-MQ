package cn.edu.xmu.oomall.prodorder.dao.bo.state;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.prodorder.dao.bo.Order;
import lombok.NoArgsConstructor;

//未付款
@NoArgsConstructor
public class WaitPayState implements StateInf {
    private final int code = WAIT_PAY;

    @Override
    public boolean canStateChange(int orderState) {
        return toState.get(this.code).contains(orderState);
    }

    public int getCode() {
        return code;
    }

    @Override
    public void cancelOrder(Order order, UserDto user) {
        if (!canStateChange(CANCEL) && !canStateChange(WAIT_REFUND)) {
            throw new BusinessException(ReturnNo.STATENOTALLOW, String.format(ReturnNo.STATENOTALLOW.getMessage().replace("%d", "%s"), "订单", order.getObjectId(), STATENAMES.get(code)));
        }
        order.increaseInventory(user);
        order.setStateInterfaceByCode(CANCEL);
    }

    @Override
    public void cancelOrderByShop(Order order, UserDto user) {
        if (!canStateChange(CANCEL) && !canStateChange(WAIT_REFUND)) {
            throw new BusinessException(ReturnNo.STATENOTALLOW, String.format(ReturnNo.STATENOTALLOW.getMessage().replace("%d", "%s"), "订单", order.getObjectId(), STATENAMES.get(code)));
        }
        order.increaseInventory(user);
        order.setStateInterfaceByCode(CANCEL);
    }

}
