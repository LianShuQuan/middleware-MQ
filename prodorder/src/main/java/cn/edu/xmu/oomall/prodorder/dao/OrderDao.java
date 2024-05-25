//School of Informatics Xiamen University, GPL-3.0 license

package cn.edu.xmu.oomall.prodorder.dao;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.javaee.core.util.JacksonUtil;
import cn.edu.xmu.oomall.prodorder.dao.bo.IdName;
import cn.edu.xmu.oomall.prodorder.dao.bo.Order;
import cn.edu.xmu.oomall.prodorder.dao.openfeign.customer.CouponDao;
import cn.edu.xmu.oomall.prodorder.dao.openfeign.freight.FreightDao;
import cn.edu.xmu.oomall.prodorder.dao.openfeign.product.bo.DiscountGroup;
import cn.edu.xmu.oomall.prodorder.dao.openfeign.product.OnsaleDao;
import cn.edu.xmu.oomall.prodorder.dao.openfeign.payment.PaymentDao;
import cn.edu.xmu.javaee.core.util.CloneFactory;
import cn.edu.xmu.oomall.prodorder.dao.openfeign.shop.bo.PackGroup;
import cn.edu.xmu.oomall.prodorder.mapper.OrderPoMapper;
import cn.edu.xmu.oomall.prodorder.mapper.po.OrderPo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@Repository
public class OrderDao {

    private Logger logger = LoggerFactory.getLogger(OrderDao.class);

    private OrderPoMapper orderPoMapper;
    private OrderItemDao orderItemDao;
    private OnsaleDao onsaleDao;
    private FreightDao freightDao;
    private PaymentDao paymentDao;
    private PackGroup packGroup;
    private DiscountGroup discountGroup;
    private OrderPackDao orderPackDao;
    private CouponDao couponDao;

    @Autowired
    public OrderDao(OrderPoMapper orderPoMapper,
                    OrderItemDao orderItemDao,
                    OrderPackDao orderPackDao,
                    OnsaleDao onsaleDao,
                    FreightDao freightDao,
                    PaymentDao paymentDao,
                    PackGroup packGroup,
                    DiscountGroup discountGroup,
                    CouponDao couponDao) {
        this.orderPoMapper = orderPoMapper;
        this.orderItemDao = orderItemDao;
        this.onsaleDao = onsaleDao;
        this.freightDao = freightDao;
        this.paymentDao = paymentDao;
        this.packGroup = packGroup;
        this.discountGroup = discountGroup;
        this.orderPackDao = orderPackDao;
        this.couponDao = couponDao;
    }

    public String insert(Order order, UserDto user) {
        OrderPo po = CloneFactory.copy(new OrderPo(), order);
        po.setCustomer(new IdName(user.getId(), user.getName()));
        po.setGmtCreate(LocalDateTime.now());
        po.setCreatorId(user.getId());
        po.setCreatorName(user.getName());

        logger.debug("insert: po={}", JacksonUtil.toJson(po));

        OrderPo newPo = orderPoMapper.insert(po);
        return newPo.getObjectId();
    }

    public Order save(Order order, UserDto user) {
        if (!Objects.equals(order.getCustomer().getId(), user.getId())) {
            throw new BusinessException(ReturnNo.RESOURCE_ID_OUTSCOPE, String.format(ReturnNo.RESOURCE_ID_OUTSCOPE.getMessage().replaceFirst("%d", "%s"), "订单", order.getObjectId(), user.getId()));
        }
        OrderPo po = CloneFactory.copy(new OrderPo(), order);
        po.setGmtModified(LocalDateTime.now());
        po.setModifierId(user.getId());
        po.setModifierName(user.getName());
        logger.info("save: po={}", JacksonUtil.toJson(po));
        OrderPo newPo = orderPoMapper.save(po);
        return build(newPo);
    }

    public Order findById(String objectId, Long customerId) {

        logger.debug("OrderDao.findById: objectId={}", objectId);

        Optional<OrderPo> optPo = orderPoMapper.findById(objectId);
        if (optPo.isEmpty()) {
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST, String.format(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage().replace("%d", "%s"), "订单", objectId));
        }

        OrderPo po = optPo.get();
        if (!po.getCustomer().getId().equals(customerId)) {
            throw new BusinessException(ReturnNo.RESOURCE_ID_OUTSCOPE, String.format(ReturnNo.RESOURCE_ID_OUTSCOPE.getMessage().replaceFirst("%d", "%s"), "订单", objectId, customerId));
        }

        return build(po);
    }

    private Order build(OrderPo po) {
        Order obj = CloneFactory.copy(new Order(), po);
        obj.setStateInterfaceByCode(po.getStatus());
        obj.setOnsaleDao(this.onsaleDao);
        obj.setOrderItemDao(this.orderItemDao);
        obj.setFreightDao(this.freightDao);
        obj.setPaymentDao(this.paymentDao);
        obj.setPackGroup(this.packGroup);
        obj.setDiscountGroup(this.discountGroup);
        obj.setOrderPackDao(this.orderPackDao);
        obj.setCouponDao(this.couponDao);
        return obj;
    }
}
