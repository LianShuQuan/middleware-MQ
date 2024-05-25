package cn.edu.xmu.oomall.prodorder.dao.openfeign.product;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.util.CloneFactory;
import cn.edu.xmu.javaee.core.model.InternalReturnObject;
import cn.edu.xmu.javaee.core.util.JacksonUtil;
import cn.edu.xmu.oomall.prodorder.dao.bo.OrderItem;
import cn.edu.xmu.oomall.prodorder.dao.openfeign.product.bo.DiscountInfo;
import cn.edu.xmu.oomall.prodorder.mapper.openfeign.ProductMapper;
import cn.edu.xmu.oomall.prodorder.mapper.openfeign.po.DiscountInfoPo;
import cn.edu.xmu.oomall.prodorder.mapper.openfeign.po.SimpleDiscountItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DiscountDao {
    private final Logger logger = LoggerFactory.getLogger(DiscountDao.class);
    private ProductMapper productMapper;

    @Autowired
    public DiscountDao(ProductMapper productMapper) {
        this.productMapper = productMapper;
    }

    public DiscountInfo getDiscountInfo(Long actId, List<OrderItem> orderItems) {
        List<SimpleDiscountItem> items = orderItems.stream().map(oi -> CloneFactory.copy(new SimpleDiscountItem(), oi)).toList();
        logger.debug("getDiscountInfo: actId={}, items={}", actId, JacksonUtil.toJson(items));
        InternalReturnObject<DiscountInfoPo> ret = productMapper.calcuDiscountsByActId(actId, items);
        if (ret == null || ret.getErrno() != ReturnNo.OK.getErrNo()) {
            logger.debug("getDiscountInfo: ret={}", JacksonUtil.toJson(ret));
            throw new BusinessException(ReturnNo.ORDER_INVOKEAPI_ERR, String.format(ReturnNo.ORDER_INVOKEAPI_ERR.getMessage(), "计算优惠", ret == null ? null : ret.getErrno()));
        }
        DiscountInfoPo po = ret.getData();
        return CloneFactory.copy(new DiscountInfo(), po);
    }
}
