package cn.edu.xmu.oomall.prodorder.dao.openfeign.product.bo;


import cn.edu.xmu.oomall.prodorder.dao.OrderItemDao;
import cn.edu.xmu.oomall.prodorder.dao.bo.CouponOrderItem;
import cn.edu.xmu.oomall.prodorder.dao.bo.TemplateGroup;
import cn.edu.xmu.oomall.prodorder.dao.bo.ItemInfo;
import cn.edu.xmu.oomall.prodorder.dao.bo.OrderItem;
import cn.edu.xmu.oomall.prodorder.dao.openfeign.product.DiscountDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.IntStream;


@Repository
public class DiscountGroup extends TemplateGroup {

    private final Logger logger = LoggerFactory.getLogger(DiscountGroup.class);

    private DiscountDao discountDao;

    private OrderItemDao orderItemDao;

    @Autowired
    public DiscountGroup(DiscountDao discountDao, OrderItemDao orderItemDao) {
        this.discountDao = discountDao;
        this.orderItemDao = orderItemDao;
    }

    @Override
    protected HashMap<Long, List<OrderItem>> buildMap(List<OrderItem> orderItems) {
        HashMap<Long, List<OrderItem>> discountMap = new HashMap<>();
        orderItems.forEach(oi -> {
            Long actId = oi.getActId();
            if (!discountMap.containsKey(actId)) {
                discountMap.put(actId, new ArrayList<>());
            }
            discountMap.get(actId).add(oi);
        });
        return discountMap;
    }

    @Override
    protected ItemInfo repack(List<ItemInfo> discountInfoList) {
        List<DiscountItem> discountItems = new ArrayList<>();
        discountInfoList.forEach(discountInfo -> discountItems.addAll(((DiscountInfo) discountInfo).getDiscountItems()));
        Integer discountPrice = calculate(discountInfoList);
        Integer originPrice = getOriginPrice(discountInfoList);
        return new DiscountInfo(discountPrice, originPrice, discountItems);
    }

    @Override
    protected Integer calculate(List<ItemInfo> discountInfoList) {
        return discountInfoList.stream().mapToInt(discountInfo -> ((DiscountInfo) discountInfo).getDiscountItems().stream().mapToInt(DiscountItem::getDiscount).sum()).sum();
    }

    @Override
    protected List<ItemInfo> accessData(Long id, HashMap<Long, List<OrderItem>> discountMap) {
        return new ArrayList<>(
                discountMap.entrySet().stream()
                        .map(entry -> discountDao.getDiscountInfo(entry.getKey(), entry.getValue()))
                        .toList()
        );
    }

    public ItemInfo constructForCoupon(Long customerId, List<CouponOrderItem> couponOrderItemList) {
        HashMap<Long, List<OrderItem>> discountMap = new HashMap<>();
        IntStream.range(0, couponOrderItemList.size()).forEach(index -> {
                    CouponOrderItem couponOrderItem = couponOrderItemList.get(index);
                    OrderItem orderItem = orderItemDao.findById(couponOrderItem.getOrderItemId(), customerId);
                    Long actId = couponOrderItem.getActivityId();
                    if (!discountMap.containsKey(actId)) {
                        discountMap.put(actId, new ArrayList<>());
                    }
                    discountMap.get(actId).add(orderItem);
                }
        );
        return repack(accessData(null, discountMap));
    }

    public Integer getOriginPrice(List<ItemInfo> discountInfoList) {
        return discountInfoList.stream().mapToInt(discountInfo -> ((DiscountInfo) discountInfo).getDiscountItems().stream().mapToInt(DiscountItem::getPrice).sum()).sum();
    }

}
