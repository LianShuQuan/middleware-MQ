package cn.edu.xmu.oomall.prodorder.dao.openfeign.product;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.mapper.RedisUtil;
import cn.edu.xmu.javaee.core.model.InternalReturnObject;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.javaee.core.util.CloneFactory;
import cn.edu.xmu.javaee.core.util.JacksonUtil;
import cn.edu.xmu.oomall.prodorder.dao.bo.IdName;
import cn.edu.xmu.oomall.prodorder.dao.bo.Order;
import cn.edu.xmu.oomall.prodorder.dao.bo.OrderItem;
import cn.edu.xmu.oomall.prodorder.dao.openfeign.product.bo.Product;
import cn.edu.xmu.oomall.prodorder.mapper.openfeign.ProductMapper;
import cn.edu.xmu.oomall.prodorder.mapper.openfeign.po.FullProductPo;
import cn.edu.xmu.oomall.prodorder.mapper.rabbitmq.ProductMQMapper;
import cn.edu.xmu.oomall.prodorder.mapper.rabbitmq.po.OrderItemMessage;
import cn.edu.xmu.oomall.prodorder.mapper.rabbitmq.po.OrderMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class OnsaleDao {

    private ProductMapper productMapper;

    private ProductMQMapper productMQMapper;

    private RedisUtil redisUtil;

    private Logger logger = LoggerFactory.getLogger(OnsaleDao.class);

    private final RedisTemplate<String, Object> redisTemplate;

    private static final DefaultRedisScript<Long> ONSALE_CHECK_DECREASE_SCRIPT;

    //把脚本装入内存，不用每次都去读取文件
    static {
        ONSALE_CHECK_DECREASE_SCRIPT = new DefaultRedisScript<Long>();
        ONSALE_CHECK_DECREASE_SCRIPT.setLocation(new ClassPathResource("onsale_check_decrease.lua"));
        ONSALE_CHECK_DECREASE_SCRIPT.setResultType(Long.class);
    }

    @Autowired
    public OnsaleDao(ProductMapper productMapper, ProductMQMapper productMQMapper, RedisUtil redisUtil, RedisTemplate redisTemplate) {
        this.productMapper = productMapper;
        this.productMQMapper = productMQMapper;
        this.redisUtil = redisUtil;
        this.redisTemplate = redisTemplate;
    }

    @Value("${oomall.redis.timeout}")
    private Long timeout;

    public Product getProductByOnsaleId(Long onsaleId) {
        FullProductPo fullProductPo = null;
        String key = "fp_" + onsaleId.toString();
        if (redisUtil.hasKey(key)) {
            fullProductPo = (FullProductPo) redisUtil.get(key);
        } else {
            InternalReturnObject<FullProductPo> ret = productMapper.findProductByOnsaleId(onsaleId);
            if (ret == null || ret.getErrno() != ReturnNo.OK.getErrNo()) {
                throw new BusinessException(ReturnNo.ORDER_INVOKEAPI_ERR, String.format(ReturnNo.ORDER_INVOKEAPI_ERR.getMessage(), "获取OnsaleId对应的Product", ret == null ? null : ret.getErrno()));
            }
            fullProductPo = ret.getData();
            redisUtil.set(key, fullProductPo, timeout);
        }

        return CloneFactory.copy(new Product(), fullProductPo);
    }

    public void decrInventory(Order order, IdName user) {

        logger.debug("OnsaleDao: decrInventory.");

        // 使用lua脚本 判断库存+减库存 保证原子性
        // KEYS：所有的onsaleIdKey
        // ARGV：对应的quantity
        Long scriptRes = null;
        try {
            scriptRes = redisTemplate.execute(ONSALE_CHECK_DECREASE_SCRIPT,
                    order.getOrderItems().stream().map(oi -> "fp_" + oi.getOnsaleId().toString()).toList(),
                    order.getOrderItems().stream().map(OrderItem::getQuantity).toList());
            if (scriptRes == null) {
                throw new RuntimeException("返回null");
            }
        } catch (Exception e) {
            throw new RuntimeException(String.format("lua脚本错误：%s", e.getMessage()));
        }

        if (scriptRes == -1) {  // 本地redis数据库中无此数据，调API
            logger.info("有key不存在");
            order.getOrderItems().stream()
                    .forEach(oi -> {
                        InternalReturnObject<FullProductPo> ret = productMapper.findProductByOnsaleId(oi.getOnsaleId());
                        if (ret == null || ret.getErrno() != ReturnNo.OK.getErrNo()) {
                            throw new BusinessException(ReturnNo.ORDER_INVOKEAPI_ERR, String.format(ReturnNo.ORDER_INVOKEAPI_ERR.getMessage(), "获取OnsaleId对应的Product", ret == null ? null : ret.getErrno()));
                        }
                        FullProductPo fullProductPo = ret.getData();
                        redisUtil.set("fp_" + oi.getOnsaleId(), fullProductPo, timeout);
                        if (fullProductPo.getQuantity() < oi.getQuantity()) {  // 库存不足，抛出异常
                            throw new BusinessException(ReturnNo.ORDER_OUTSTOCK, String.format(ReturnNo.ORDER_OUTSTOCK.getMessage(), oi.getOnsaleId()));
                        }
                    });
        } else if (scriptRes == 0) {
            logger.info("redis-库存不足");
            throw new BusinessException(ReturnNo.ORDER_OUTSTOCK, String.format(ReturnNo.ORDER_OUTSTOCK.getMessage(), order.getObjectId()));
        } else if (scriptRes == 1) {
            logger.info("redis-库存充足");
        }

        OrderMessage orderMessage = CloneFactory.copy(new OrderMessage(), order);
        List<OrderItemMessage> orderItemMessageList = order.getOrderItems().stream()
                .map(oi -> {
                    logger.debug("decrInventory: orderItem={}", JacksonUtil.toJson(oi));
                    OrderItemMessage orderItemMessage = CloneFactory.copy(new OrderItemMessage(), oi);
                    orderItemMessage.setDiscount(oi.getDiscountPrice());
                    orderItemMessage.setCategoryId(oi.getProduct().getCategory().getId());
                    orderItemMessage.setCouponActivityId(oi.getActId());
                    return orderItemMessage;
                }).toList();
        orderMessage.setOrderItems(orderItemMessageList);

        productMQMapper.decrInventory(orderMessage, user);
    }

    public void incrInventory(Order order, UserDto user){
        List<OrderItem> orderItems = order.getOrderItems();
        OrderMessage orderMessage = new OrderMessage();
        orderMessage.setObjectId(order.getObjectId());
        List<OrderItemMessage> orderItemMessages = new ArrayList<>();
        for(OrderItem orderItem:orderItems){
            OrderItemMessage orderItemMessage = new OrderItemMessage();
            orderItemMessage.setObjectId(orderItem.getObjectId());
            orderItemMessage.setQuantity(orderItem.getQuantity());
            orderItemMessage.setPrice(orderItem.getPrice());
            orderItemMessage.setDiscount(orderItem.getDiscountPrice());
            orderItemMessage.setCategoryId(null);
            orderItemMessage.setCouponActivityId(null);
            orderItemMessages.add(orderItemMessage);
        }
        orderMessage.setOrderItems(orderItemMessages);
        productMQMapper.incrInventory(orderMessage, user);
    }

}
