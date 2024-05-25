package cn.edu.xmu.oomall.prodorder.mapper.openfeign;

import cn.edu.xmu.javaee.core.model.InternalReturnObject;
import cn.edu.xmu.oomall.prodorder.dao.openfeign.product.bo.ProductItem;
import cn.edu.xmu.oomall.prodorder.mapper.openfeign.po.Pack;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "shop-service")
public interface ShopMapper {

    /**
     * 计算运费，并分包
     * @param templateId
     * @param regionId
     * @param items
     * @return
     */
    @PostMapping("/internal/templates/{id}/regions/{rid}/freightprice")
    InternalReturnObject<Pack> calcuFreightFee(@PathVariable("id") Long templateId, @PathVariable("rid") Long regionId,
                                               @RequestBody List<ProductItem> items);

}
