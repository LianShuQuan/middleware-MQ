//School of Informatics Xiamen University, GPL-3.0 license
package cn.edu.xmu.oomall.prodorder.mapper.openfeign;

import cn.edu.xmu.javaee.core.model.InternalReturnObject;
import cn.edu.xmu.oomall.prodorder.mapper.openfeign.po.CouponAct;
import cn.edu.xmu.oomall.prodorder.mapper.openfeign.po.DiscountInfoPo;
import cn.edu.xmu.oomall.prodorder.mapper.openfeign.po.FullProductPo;
import cn.edu.xmu.oomall.prodorder.mapper.openfeign.po.SimpleDiscountItem;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@FeignClient(name = "product-service")
public interface ProductMapper {

    /**
     * onsaleId 换 FullProduct
     * @param onsaleId
     * @return FullProduct
     */
    @GetMapping("/onsales/{id}")
    InternalReturnObject<FullProductPo> findProductByOnsaleId(@PathVariable("id") Long onsaleId);

    /**
     * 计算优惠
     * @param actId
     * @param items
     * @return list of DiscountItem
     */
    @PostMapping("/couponactivities/{id}/caculate")
    InternalReturnObject<DiscountInfoPo> calcuDiscountsByActId(@PathVariable("id") Long actId, @RequestBody List<SimpleDiscountItem> items);

    /**
     * actId换对象
     * @param actId
     * @return
     */
    @GetMapping("/couponactivities/{id}")
    InternalReturnObject<CouponAct> findCouponActById(@PathVariable("id") Long actId);

}

