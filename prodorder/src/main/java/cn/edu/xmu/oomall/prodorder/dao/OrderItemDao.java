package cn.edu.xmu.oomall.prodorder.dao;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.javaee.core.util.CloneFactory;
import cn.edu.xmu.javaee.core.util.JacksonUtil;
import cn.edu.xmu.oomall.prodorder.dao.bo.OrderItem;
import cn.edu.xmu.oomall.prodorder.dao.openfeign.customer.bo.Coupon;
import cn.edu.xmu.oomall.prodorder.mapper.OrderItemPoMapper;
import cn.edu.xmu.oomall.prodorder.mapper.OrderPoMapper;
import cn.edu.xmu.oomall.prodorder.mapper.po.OrderItemPo;
import cn.edu.xmu.oomall.prodorder.mapper.po.OrderPo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class OrderItemDao {
    private final Logger logger = LoggerFactory.getLogger(OrderItemDao.class);

    private OrderItemPoMapper orderItemPoMapper;

    private OrderPoMapper orderPoMapper;

    @Autowired
    public OrderItemDao(OrderItemPoMapper orderItemPoMapper, OrderPoMapper orderPoMapper) {
        this.orderItemPoMapper = orderItemPoMapper;
        this.orderPoMapper = orderPoMapper;
    }

    public List<OrderItem> insertAll(List<OrderItem> orderItems, UserDto user) {
        List<OrderItemPo> poList = orderItems.stream().map(oi -> {
            OrderItemPo po = CloneFactory.copy(new OrderItemPo(), oi);
            po.setGmtCreate(LocalDateTime.now());
            po.setCreatorId(user.getId());
            po.setCreatorName(user.getName());
            return po;
        }).toList();
        List<OrderItemPo> newPoList = orderItemPoMapper.insert(poList);
        return newPoList.stream().map(this::build).toList();
    }

    public List<OrderItem> saveAll(List<OrderItem> orderItems, UserDto user) {
        List<OrderItemPo> poList = orderItems.stream().map(oi -> {
            OrderItemPo po = CloneFactory.copy(new OrderItemPo(), oi);
            po.setGmtModified(LocalDateTime.now());
            po.setModifierId(user.getId());
            po.setModifierName(user.getName());
            return po;
        }).toList();
        logger.info("saveAll: po[0]={}", JacksonUtil.toJson(poList.get(0)));
        List<OrderItemPo> newPoList = orderItemPoMapper.saveAll(poList);
        return newPoList.stream().map(this::build).toList();
    }

    public List<OrderItem> findAllByOrderId(String orderId, Long customerId) {
        if (orderId == null) {
            throw new IllegalArgumentException("OrderItemDao.findAllByOrderId: orderId is null");
        }
        Optional<OrderPo> optOrderPo = orderPoMapper.findById(orderId);
        if (optOrderPo.isEmpty()) {
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST, String.format(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage().replace("%d", "%s"), "订单明细", orderId));
        } else if (!Objects.equals(optOrderPo.get().getCustomer().getId(), customerId)) {
            throw new BusinessException(ReturnNo.RESOURCE_ID_OUTSCOPE, String.format(ReturnNo.RESOURCE_ID_OUTSCOPE.getMessage().replaceFirst("%d", "%s"), "订单", orderId, customerId));
        }
        List<OrderItemPo> orderItemPoList = orderItemPoMapper.findAllByOrderId(orderId);
        return new ArrayList<>(orderItemPoList.stream().map(this::build).toList());
    }

    public OrderItem findById(String orderItemId, Long customerId) {
        logger.debug("OrderItemDao.findAllByOrderId: orderItemId={}", orderItemId);

        Optional<OrderItemPo> optOrderItemPo = orderItemPoMapper.findById(orderItemId);
        if (optOrderItemPo.isEmpty()) {
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST, String.format(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage().replace("%d", "%s"), "订单明细", orderItemId));
        }

        OrderItemPo orderItemPo = optOrderItemPo.get();
        String orderId = orderItemPo.getOrderId();

        Optional<OrderPo> optOrderPo = orderPoMapper.findById(orderId);
        if (optOrderPo.isEmpty()) {
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST, String.format(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage().replace("%d", "%s"), "订单", orderId));
        } else if (!optOrderPo.get().getObjectId().equals(orderId)) {
            throw new BusinessException(ReturnNo.RESOURCE_ID_OUTSCOPE, String.format(ReturnNo.RESOURCE_ID_OUTSCOPE.getMessage().replaceFirst("%d", "%s"), "订单", orderId, customerId));
        }
        return build(orderItemPo);
    }

    private OrderItem build(OrderItemPo po) {
        logger.debug("OrderItem: build: po={}", JacksonUtil.toJson(po));
        OrderItem obj = CloneFactory.copy(new OrderItem(), po);
        obj.setCoupon(new Coupon(po.getCoupon()));
        return obj;
    }

}
