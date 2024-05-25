package cn.edu.xmu.oomall.prodorder.dao.openfeign.customer;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.InternalReturnObject;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.prodorder.dao.bo.Order;
import cn.edu.xmu.oomall.prodorder.dao.bo.OrderItem;
import cn.edu.xmu.oomall.prodorder.dao.openfeign.customer.bo.Activity;
import cn.edu.xmu.oomall.prodorder.dao.openfeign.customer.bo.Coupon;
import cn.edu.xmu.javaee.core.util.CloneFactory;
import cn.edu.xmu.oomall.prodorder.dao.bo.CheckInfo;
import cn.edu.xmu.oomall.prodorder.listener.vo.PayOrderLocalTransParam;
import cn.edu.xmu.oomall.prodorder.mapper.openfeign.CustomerMapper;
import cn.edu.xmu.oomall.prodorder.mapper.openfeign.po.CouponPo;
import cn.edu.xmu.oomall.prodorder.mapper.rocketmq.CustomerMQMapper;
import cn.edu.xmu.oomall.prodorder.mapper.rocketmq.po.PointAndCouponMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class CouponDao {
    private Logger logger = LoggerFactory.getLogger(CouponDao.class);

    private CustomerMapper customerMapper;

    private CustomerMQMapper customerMQMapper;

    @Autowired
    public CouponDao(CustomerMapper customerMapper, CustomerMQMapper customerMQMapper) {
        this.customerMapper = customerMapper;
        this.customerMQMapper = customerMQMapper;
    }

    public List<Coupon> checkCoupons(Long customerId, CheckInfo checkInfo) {
        InternalReturnObject<List<CouponPo>> ret = this.customerMapper.checkCoupons(customerId, checkInfo);
        if (ret == null || ReturnNo.OK.getErrNo() != ret.getErrno()) {
            logger.debug("CouponDaoFeign: check {}", ReturnNo.getReturnNoByCode(ret.getErrno()));
            throw new BusinessException(ReturnNo.ORDER_INVOKEAPI_ERR, String.format(ReturnNo.ORDER_INVOKEAPI_ERR.getMessage(), "获取优惠券", ret.getErrno()));
        } else {
            return ret.getData().stream().map(obj -> {
                Coupon coupon = CloneFactory.copy(new Coupon(), obj);
                coupon.setActivity(new Activity());
                coupon.getActivity().setId(obj.getActivityId());
                return coupon;
            }).toList();
        }
    }

    public void returnPointAndCoupon(Order order, UserDto user){
        PointAndCouponMessage pointAndCouponMessage = new PointAndCouponMessage();
        pointAndCouponMessage.setPoint(order.getPoint());
        List<Long> couponIdList = new ArrayList<>();
        for(OrderItem orderItem:order.getOrderItems()){
            couponIdList.add(orderItem.getCoupon().getId());
        }
        pointAndCouponMessage.setCouponIdList(couponIdList);
        customerMQMapper.returnPointAndCoupon(pointAndCouponMessage, user);
    }

    public void useCouponCreatePayment(PayOrderLocalTransParam localTransParam) {
        this.customerMQMapper.useCouponCreatePayment(localTransParam);
    }

}
