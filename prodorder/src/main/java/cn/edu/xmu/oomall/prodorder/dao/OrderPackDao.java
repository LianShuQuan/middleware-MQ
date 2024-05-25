package cn.edu.xmu.oomall.prodorder.dao;

import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.javaee.core.util.JacksonUtil;
import cn.edu.xmu.oomall.prodorder.dao.bo.OrderPack;
import cn.edu.xmu.oomall.prodorder.mapper.OrderPackPoMapper;
import cn.edu.xmu.oomall.prodorder.mapper.OrderPoMapper;
import cn.edu.xmu.oomall.prodorder.mapper.po.OrderPackPo;
import cn.edu.xmu.javaee.core.util.CloneFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class OrderPackDao {

    private Logger logger = LoggerFactory.getLogger(OrderPackDao.class);

    private OrderPackPoMapper orderPackPoMapper;
    private OrderPoMapper orderPoMapper;

    @Autowired
    public OrderPackDao(OrderPackPoMapper orderPackPoMapper, OrderPoMapper orderPoMapper) {
        this.orderPackPoMapper = orderPackPoMapper;
        this.orderPoMapper = orderPoMapper;
    }

    public List<OrderPack> insertAll(List<OrderPack> orderPacks, UserDto user) {
        List<OrderPackPo> orderPackPoList = orderPacks.stream().map(orderPack -> {
            OrderPackPo po = CloneFactory.copy(new OrderPackPo(), orderPack);
            po.setGmtCreate(LocalDateTime.now());
            po.setCreatorId(user.getId());
            po.setCreatorName(user.getName());
            return po;
        }).toList();
        logger.info("insertAll: orderPackPoList[0]={}", JacksonUtil.toJson(orderPackPoList.get(0)));
        List<OrderPackPo> newPoList = orderPackPoMapper.insert(orderPackPoList);
        return newPoList.stream().map(this::build).toList();
    }

    public List<OrderPack> findAllByOrderId(String orderId ,Long user) {
        List<OrderPackPo> orderPackPoList = orderPackPoMapper.findAllByOrderId(orderId);
        return orderPackPoList.stream().map(this::build).toList();
    }

    private OrderPack build(OrderPackPo po) {
        OrderPack obj = CloneFactory.copy(new OrderPack(), po);
        return obj;
    }
}
