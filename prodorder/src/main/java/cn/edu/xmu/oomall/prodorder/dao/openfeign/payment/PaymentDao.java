package cn.edu.xmu.oomall.prodorder.dao.openfeign.payment;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.InternalReturnObject;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.prodorder.dao.openfeign.payment.bo.PayOrderInfo;
import cn.edu.xmu.oomall.prodorder.dao.openfeign.payment.bo.PayTransInfo;
import cn.edu.xmu.oomall.prodorder.mapper.openfeign.PaymentMapper;
import cn.edu.xmu.oomall.prodorder.mapper.openfeign.po.PayTrans;
import cn.edu.xmu.oomall.prodorder.mapper.openfeign.po.Refund;
import cn.edu.xmu.oomall.prodorder.mapper.openfeign.po.SimpleTrans;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class PaymentDao {

    private PaymentMapper paymentMapper;

    @Autowired
    public PaymentDao(PaymentMapper paymentMapper) {
        this.paymentMapper = paymentMapper;
    }

    public InternalReturnObject<PayTrans> getPayment(Long shopId, Long payTransId) {
        return paymentMapper.getPayment(shopId, payTransId);
    }

    public InternalReturnObject<SimpleTrans> createRefund(Long shopId, Long payTransId, Refund refund,UserDto user) {
        return paymentMapper.createRefund(shopId, payTransId, refund);
    }

    public PayOrderInfo createPayment(Long shopChannelId, PayTransInfo payTransInfo) {
        InternalReturnObject<PayOrderInfo> ret = this.paymentMapper.createPayment(shopChannelId, payTransInfo);
        if (ret == null || ret.getErrno() != ReturnNo.OK.getErrNo()) {
            throw new BusinessException(ReturnNo.ORDER_INVOKEAPI_ERR, String.format(ReturnNo.ORDER_INVOKEAPI_ERR.getMessage(), "创建支付单", ret == null ? null : ret.getErrno()));
        }
        return ret.getData();
    }
}
