package cn.edu.xmu.oomall.prodorder.mapper.openfeign;

import cn.edu.xmu.javaee.core.model.InternalReturnObject;
import cn.edu.xmu.oomall.prodorder.dao.openfeign.payment.bo.PayOrderInfo;
import cn.edu.xmu.oomall.prodorder.dao.openfeign.payment.bo.PayTransInfo;
import cn.edu.xmu.oomall.prodorder.mapper.openfeign.po.PayTrans;
import cn.edu.xmu.oomall.prodorder.mapper.openfeign.po.Refund;
import cn.edu.xmu.oomall.prodorder.mapper.openfeign.po.SimpleTrans;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "payment-service")
public interface PaymentMapper {
    //查询订单支付信息
    @GetMapping("/payments/{id}")
    InternalReturnObject<PayTrans> getPayment(@PathVariable Long shopId, @PathVariable Long id);

    //创建退款
    @PostMapping("/shops/{shopId}/payments/{id}/refunds")
    InternalReturnObject<SimpleTrans> createRefund(@PathVariable Long shopId,
                                                   @PathVariable Long id,
                                                   @Validated @RequestBody Refund refund);

    @PostMapping("/internal/shopchannels/{id}/payments")
    InternalReturnObject<PayOrderInfo> createPayment(@PathVariable Long id, @RequestBody PayTransInfo paymentInfo);
}
