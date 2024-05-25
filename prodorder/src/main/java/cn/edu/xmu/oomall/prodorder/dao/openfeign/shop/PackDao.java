package cn.edu.xmu.oomall.prodorder.dao.openfeign.shop;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.InternalReturnObject;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.util.CloneFactory;
import cn.edu.xmu.oomall.prodorder.dao.bo.OrderItem;
import cn.edu.xmu.oomall.prodorder.dao.bo.OrderPack;
import cn.edu.xmu.oomall.prodorder.dao.openfeign.product.bo.ProductItem;
import cn.edu.xmu.oomall.prodorder.dao.openfeign.shop.bo.PackInfo;
import cn.edu.xmu.oomall.prodorder.mapper.openfeign.ShopMapper;
import cn.edu.xmu.oomall.prodorder.mapper.openfeign.po.Pack;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PackDao {

    private ShopMapper shopMapper;

    @Autowired
    public PackDao(ShopMapper shopMapper) {
        this.shopMapper = shopMapper;
    }

    public PackInfo getPacks(Long templateId, Long regionId, List<OrderItem> orderItems) {
        List<ProductItem> productItems = orderItems.stream().map(ProductItem::new).toList();
        InternalReturnObject<Pack> ret = shopMapper.calcuFreightFee(templateId, regionId, productItems);

        if (ret == null || ret.getErrno() != ReturnNo.OK.getErrNo()) {
            throw new BusinessException(ReturnNo.ORDER_INVOKEAPI_ERR, String.format(ReturnNo.ORDER_INVOKEAPI_ERR.getMessage(), "计算运费", ret == null ? null : ret.getErrno()));
        }
        Pack pack = ret.getData();
        List<OrderPack> orderPacks = pack.getPack().stream().map(obj -> new OrderPack(obj.stream().map(item -> CloneFactory.copy(new ProductItem(), item)).toList())).toList();

        return new PackInfo(pack.getFreightPrice(), orderPacks);
    }
}
