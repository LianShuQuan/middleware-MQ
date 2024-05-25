package cn.edu.xmu.oomall.prodorder.dao.bo.state;


import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.prodorder.dao.bo.Order;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.ToString;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

//inf
public interface StateInf {
    /**
     * 未付款
     */
    @ToString.Exclude
    @JsonIgnore
    public static int WAIT_PAY = 101;
    /**
     * 待支付尾款
     */
    @ToString.Exclude
    @JsonIgnore
    public static int WAIT_FINAL_PAY = 102;
    /**
     * 待成团
     */
    @ToString.Exclude
    @JsonIgnore
    public static int WAIT_GROUP = 201;
    /**
     * 待发货
     */
    @ToString.Exclude
    @JsonIgnore
    public static int WAIT_SHIP = 203;
    /**
     * 待收货
     */
    @ToString.Exclude
    @JsonIgnore
    public static int WAIT_RECEIVE = 204;
    /**
     * 已完成
     */
    @ToString.Exclude
    @JsonIgnore
    public static int FINISH = 300;
    /**
     * 待退款
     */
    @ToString.Exclude
    @JsonIgnore
    public static int WAIT_REFUND = 401;
    /**
     * 已取消
     */
    @ToString.Exclude
    @JsonIgnore
    public static int CANCEL = 402;
    public static final Map<Integer, String> STATENAMES = new HashMap() {
        {
            put(WAIT_PAY, "未付款");
            put(WAIT_FINAL_PAY, "待支付尾款");
            put(WAIT_GROUP, "待成团");
            put(WAIT_SHIP, "待发货");
            put(WAIT_RECEIVE, "待收货");
            put(FINISH, "已完成");
            put(WAIT_REFUND, "待退款");
            put(CANCEL, "已取消");
        }
    };
    /**
     * 允许的状态迁移
     */
    public static final Map<Integer, Set<Integer>> toState = new HashMap<>() {
        {
            put(WAIT_PAY, new HashSet<>() {
                {
                    add(WAIT_FINAL_PAY);
                    add(WAIT_GROUP);
                    add(WAIT_SHIP);
                    add(CANCEL);
                }
            });
            put(WAIT_FINAL_PAY, new HashSet<>() {
                        {
                            add(WAIT_SHIP);
                            add(WAIT_REFUND);
                            add(CANCEL);
                        }
                    }
            );
            put(WAIT_GROUP, new HashSet<>() {
                {
                    add(WAIT_SHIP);
                    add(WAIT_REFUND);
                }
            });
            put(WAIT_SHIP, new HashSet<>() {
                {
                    add(WAIT_RECEIVE);
                    add(WAIT_REFUND);
                }
            });
            put(WAIT_RECEIVE, new HashSet<>() {
                {
                    add(FINISH);
                    add(WAIT_REFUND);
                }
            });
            put(FINISH, new HashSet<>());
            put(WAIT_REFUND, new HashSet<>(){
                {
                    add(CANCEL);
                }
            });
            put(CANCEL, new HashSet<>());
        }

    };

    /*
     * 判断能否迁移
     */
    public boolean canStateChange(int state);
    /*
     * 享元设计模式
     */
    public static final Map<Integer,StateInf> statePool = new HashMap<>() {
        {
            put(WAIT_PAY, new WaitPayState());
            put(WAIT_FINAL_PAY, new WaitFinalPayState());
            put(WAIT_GROUP, new WaitGroupState());
            put(WAIT_SHIP, new WaitShipState());
            put(WAIT_RECEIVE, new WaitReceiveState());
            put(FINISH, new FinishState());
            put(WAIT_REFUND, new WaitRefundState());
            put(CANCEL, new CancelState());
        }
    };

    public int getCode();

    public void cancelOrder(Order order, UserDto user);

    public void cancelOrderByShop(Order order, UserDto user);
}
