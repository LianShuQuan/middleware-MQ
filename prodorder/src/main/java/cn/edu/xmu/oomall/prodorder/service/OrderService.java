package cn.edu.xmu.oomall.prodorder.service;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.javaee.core.util.SnowFlakeIdWorker;
import cn.edu.xmu.oomall.prodorder.dao.OrderDao;
import cn.edu.xmu.oomall.prodorder.dao.OrderPackDao;
import cn.edu.xmu.oomall.prodorder.dao.bo.Order;
import cn.edu.xmu.oomall.prodorder.dao.bo.OrderItem;
import cn.edu.xmu.oomall.prodorder.dao.openfeign.customer.bo.Coupon;
import cn.edu.xmu.oomall.prodorder.dao.bo.PayInfo;
import cn.edu.xmu.oomall.prodorder.dao.openfeign.product.OnsaleDao;
import cn.edu.xmu.oomall.prodorder.dao.openfeign.product.bo.Product;
import cn.edu.xmu.oomall.prodorder.dao.openfeign.region.RegionDao;
import cn.edu.xmu.oomall.prodorder.dao.openfeign.customer.CouponDao;
import cn.edu.xmu.oomall.prodorder.dao.bo.*;
import cn.edu.xmu.oomall.prodorder.dao.OrderItemDao;
import cn.edu.xmu.oomall.prodorder.dao.bo.CouponOrderItem;
import cn.edu.xmu.oomall.prodorder.listener.vo.PayOrderLocalTransParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class OrderService {

    private final Logger logger = LoggerFactory.getLogger(OrderService.class);

    private SnowFlakeIdWorker snowFlakeIdWorker;

    private OrderDao orderDao;

    private CouponDao couponDao;

    private OrderItemDao orderItemDao;

    private RegionDao regionDao;

    private OnsaleDao onsaleDao;
    private OrderPackDao orderPackDao;


    @Autowired
    public OrderService(SnowFlakeIdWorker snowFlakeIdWorker,
                        OrderDao orderDao,
                        CouponDao couponDao,
                        OrderItemDao orderItemDao,
                        RegionDao regionDao,
                        OnsaleDao onsaleDao,
                        OrderPackDao orderPackDao) {
        this.snowFlakeIdWorker = snowFlakeIdWorker;
        this.orderDao = orderDao;
        this.couponDao = couponDao;
        this.orderItemDao = orderItemDao;
        this.regionDao = regionDao;
        this.onsaleDao = onsaleDao;
        this.orderPackDao = orderPackDao;
    }

    public void createOrder(Order order, UserDto user) {

        checkOrderParam(order);
        String orderId = orderDao.insert(order, user);

        logger.debug("createOrder: orderId={}", orderId);

        Order newOrder = orderDao.findById(orderId, user.getId());
        newOrder.setOrderItems(order.getOrderItems());

        logger.debug("createOrder: Full Blood Order={}", newOrder);

        newOrder.build(user);    // 更新orderItems并插入数据库
        newOrder.setOrderSn(String.format("O%d", snowFlakeIdWorker.nextId()));

        orderDao.save(newOrder, user);
    }

    public void createPayment(String orderId, PayInfo payInfo, UserDto user) {
        Order order = this.orderDao.findById(orderId, user.getId());
        List<Coupon> coupons = checkPayParam(order.getCustomer().getId(), orderId, payInfo, order);
        String outTradeNo = String.format("P%d", snowFlakeIdWorker.nextId());

        // 发送事务消息,并执行本地事务
        PayOrderLocalTransParam localTransParam = PayOrderLocalTransParam.builder()
                .order(order).coupons(coupons).payInfo(payInfo).outTradeNo(outTradeNo).user(user).build();
        this.couponDao.useCouponCreatePayment(localTransParam);
        order.createPayment(coupons, payInfo, outTradeNo, user);

        orderDao.save(order, user);
    }


    private void checkOrderParam(Order order) {
        // 检验consignee.regionId是否合法
        Long regionId = order.getConsignee().getRegionId();
        regionDao.findRegionById(regionId);

        /* 检验onsaleId是否重复 */
        List<Long> dupliactedIdList = order.getOrderItems().stream()
                .map(OrderItem::getOnsaleId)
                .collect(Collectors.toMap(e -> e, e -> 1, Integer::sum))
                .entrySet().stream()
                .filter(entry -> entry.getValue() > 1)
                .map(Map.Entry::getKey)
                .toList();
        if (!dupliactedIdList.isEmpty()) {
            throw new BusinessException(ReturnNo.ONSALE_DUPLICATED, ReturnNo.ONSALE_DUPLICATED.getMessage());
        }
    }

    private List<Coupon> checkPayParam(Long customerId, String orderId, PayInfo payInfo, Order order) {
        List<CouponOrderItem> couponOrderItemList = payInfo.getCouponOrderItemList();
        List<Long> couponIdList = couponOrderItemList.stream().map(CouponOrderItem::getCouponId).toList();
        CheckInfo checkInfo = new CheckInfo(couponIdList, payInfo.getPoint());
        List<Coupon> coupons = this.couponDao.checkCoupons(customerId, checkInfo);
        IntStream.range(0, coupons.size()).forEach(index ->
                couponOrderItemList.get(index).setActivityId(coupons.get(index).getActivity().getId())
        );
        List<OrderItem> orderItems = orderItemDao.findAllByOrderId(orderId, customerId);
        order.setOrderItems(orderItems);
        List<String> orderItemIdList = orderItems.stream().map(OrderItem::getObjectId).toList();
        IntStream.range(0, couponOrderItemList.size()).forEach(index -> {
                    CouponOrderItem item = couponOrderItemList.get(index);
                    if (!orderItemIdList.contains(item.getOrderItemId())) {
                        throw new BusinessException(ReturnNo.ORDER_ORDERITEMNOTMATCH, ReturnNo.ORDER_ORDERITEMNOTMATCH.getMessage());
                    }
                    Product product = this.onsaleDao.getProductByOnsaleId(orderItems.get(index).getOnsaleId());
                    List<Long> actIdList = product.getActList().stream().map(SimpleCouponAct::getId).toList();
                    if (!actIdList.contains(item.getActivityId())) {
                        throw new BusinessException(ReturnNo.ORDER_ACTINVALID, String.format(ReturnNo.ORDER_ACTINVALID.getMessage(), item.getOrderItemId(), item.getActivityId()));
                    }
                }
        );
        return coupons;
    }

    public void cancelOrder(String id, UserDto user) {
        Order order = orderDao.findById(id, user.getId());
        List<OrderItem> orderItemPoList = orderItemDao.findAllByOrderId(id, user.getId());
        List<OrderPack> orderPacks = orderPackDao.findAllByOrderId(id, user.getId());
        order.setOrderItems(orderItemPoList);
        order.setPacks(orderPacks);
        order.cancelOrder(user);
        orderDao.save(order, user);
    }

    public void cancelOrderByshop(String id, UserDto user) {
        Order order = orderDao.findById(id, user.getId());
        List<OrderItem> orderItemPos = orderItemDao.findAllByOrderId(id, user.getId());
        List<OrderPack> orderPacks = orderPackDao.findAllByOrderId(id, user.getId());
        order.setOrderItems(orderItemPos);
        order.setPacks(orderPacks);
        order.cancelOrderByShop(user);
        orderDao.save(order, user);
    }
}
