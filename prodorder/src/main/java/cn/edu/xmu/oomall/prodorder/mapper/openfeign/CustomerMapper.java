package cn.edu.xmu.oomall.prodorder.mapper.openfeign;

import cn.edu.xmu.javaee.core.model.InternalReturnObject;
import cn.edu.xmu.oomall.prodorder.dao.bo.CheckInfo;
import cn.edu.xmu.oomall.prodorder.mapper.openfeign.po.CouponPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "customer-service")
public interface CustomerMapper {

    /**
     * 自定义API
     * couponIdList换couponList，并一同检验point是否充足
     * @param id
     * @param checkInfo
     * @return
     */
    @PostMapping("/customer/{id}/checkcoupon")
    InternalReturnObject<List<CouponPo>> checkCoupons(@PathVariable Long id, @RequestBody CheckInfo checkInfo);
}
