package cn.edu.xmu.oomall.prodorder.dao.bo;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.InternalReturnObject;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.bo.OOMallObject;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.prodorder.dao.OrderItemDao;
import cn.edu.xmu.oomall.prodorder.dao.OrderPackDao;
import cn.edu.xmu.oomall.prodorder.dao.bo.state.*;
import cn.edu.xmu.oomall.prodorder.dao.openfeign.customer.CouponDao;
import cn.edu.xmu.oomall.prodorder.dao.openfeign.freight.FreightDao;
import cn.edu.xmu.oomall.prodorder.dao.openfeign.payment.PaymentDao;
import cn.edu.xmu.oomall.prodorder.controller.vo.OrderVo;
import cn.edu.xmu.oomall.prodorder.dao.openfeign.customer.bo.*;
import cn.edu.xmu.oomall.prodorder.dao.openfeign.payment.bo.PayOrderInfo;
import cn.edu.xmu.oomall.prodorder.dao.openfeign.payment.bo.PayTransInfo;
import cn.edu.xmu.oomall.prodorder.dao.openfeign.product.OnsaleDao;
import cn.edu.xmu.oomall.prodorder.dao.openfeign.product.bo.*;
import cn.edu.xmu.oomall.prodorder.dao.openfeign.shop.bo.PackGroup;
import cn.edu.xmu.oomall.prodorder.dao.openfeign.shop.bo.PackInfo;
import cn.edu.xmu.oomall.prodorder.mapper.openfeign.po.PayTrans;
import cn.edu.xmu.oomall.prodorder.mapper.openfeign.po.Refund;
import cn.edu.xmu.oomall.prodorder.mapper.po.OrderPo;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.IntStream;

@NoArgsConstructor
@CopyFrom({OrderVo.class, OrderPo.class})
public class Order extends OOMallObject implements Serializable {

    private OrderItemDao orderItemDao;

    private OnsaleDao onsaleDao;

    private FreightDao freightDao;

    private PaymentDao paymentDao;

    private PackGroup packGroup;

    private DiscountGroup discountGroup;

    private OrderPackDao orderPackDao;

    private CouponDao couponDao;

    /* In Dto */

    private String objectId;

    private String orderSn;  // start with "O"

    private IdName customer;

    private IdName shop;

    private Integer originPrice;

    private Integer discountPrice;

    private Integer expressFee;

    private String message;

    private Consignee consignee;

    private List<OrderPack> packs;

    private List<OrderItem> orderItems;

    private Integer point;

    /* extra */

    private List<Long> payTransIdList;

    private StateInf stateInf;

    public void createPayment(List<Coupon> coupons, PayInfo payInfo, String outTradeNo, UserDto user) {
        List<CouponOrderItem> couponOrderItemList = payInfo.getCouponOrderItemList();
        Integer point = payInfo.getPoint();
        DiscountInfo discountInfo = (DiscountInfo) discountGroup.constructForCoupon(user.getId(), couponOrderItemList);

        this.setPoint(point);
        this.setDiscountPrice(this.getDiscountPrice() - (discountInfo.getOriginPrice() - discountInfo.getSpecialPrice()) - point);
        this.updateOrderItems(discountInfo, coupons, couponOrderItemList, payInfo.getPoint(), user);

        Integer divAmount = orderItems.stream().mapToInt(item -> item.getDiscountPrice() * item.getProduct().getCommissionRatio()).sum() / 100;
        PayTransInfo payTransInfo = new PayTransInfo(outTradeNo, this.message, this.discountPrice, divAmount);
        PayOrderInfo payOrderInfo = this.paymentDao.createPayment(payInfo.getShopChannelId(), payTransInfo);

        this.payTransIdList.add(payOrderInfo.getId());
    }

    private void initOrderItems(UserDto user) {
        this.orderItems.stream().forEach(oi -> {
            oi.setOrderId(this.objectId);
            oi.setProduct(this.onsaleDao.getProductByOnsaleId(oi.getOnsaleId()));
            if (oi.getProduct().getMaxQuantity() < oi.getQuantity()) {
                throw new BusinessException(ReturnNo.ITEM_OVERMAXQUANTITY, String.format(ReturnNo.ITEM_OVERMAXQUANTITY.getMessage(), oi.getOnsaleId(), oi.getQuantity(), oi.getProduct().getMaxQuantity()));
            }
        });
        this.setOrderItems(orderItemDao.insertAll(orderItems, user));
    }

    public void build(UserDto user) {
        initOrderItems(user);
        decreaseInventory();

        PackInfo packInfo = (PackInfo) packGroup.construct(consignee.getRegionId(), orderItems);
        updateOrderPacksByPackInfo(packInfo, user);

        DiscountInfo discountInfo = (DiscountInfo) discountGroup.construct(null, orderItems);
        updateOrderItemsByDiscount(discountInfo, user);

        this.setExpressFee(packInfo.getSpecialPrice());
        this.setDiscountPrice(discountInfo.getSpecialPrice());
        this.setShop(orderItems.get(0).getProduct().getShop());
        this.setOriginPrice(orderItems.stream().mapToInt(OrderItem::getPrice).sum());
    }

    private void updateOrderPacksByPackInfo(PackInfo packInfo, UserDto user) {
        this.setPacks(packInfo.getPack());
        this.packs.stream().forEach(item -> item.setOrderId(this.objectId));
        this.orderPackDao.insertAll(this.packs, user);
    }

    private void updateOrderItemsByDiscount(DiscountInfo discountInfo, UserDto user) {
        orderItems.forEach(orderItem -> {
            Product product = orderItem.getProduct();
            orderItem.setName(product.getName());
            orderItem.setPrice(product.getPrice());
            orderItem.setDiscountPrice(discountInfo.getDiscountByOnsaleId(orderItem.getOnsaleId()));
        });
        this.setOrderItems(orderItems);
        this.orderItemDao.saveAll(orderItems, user);
    }

    //顾客取消订单
    public void cancelOrder(UserDto user) {
        //根据不同的状态进行取消订单状态
        stateInf.cancelOrder(this, user);
    }

    //管理员取消订单
    public void cancelOrderByShop(UserDto user) {
        //根据不同的状态进行取消订单状态
        stateInf.cancelOrderByShop(this, user);
    }

    //增加库存
    public void increaseInventory(UserDto user) {
        onsaleDao.incrInventory(this, user);
    }

    //返还积点和优惠券
    public void returnPointAndCoupon(UserDto user) {
        this.couponDao.returnPointAndCoupon(this, user);
    }

    // 减库存
    private void decreaseInventory() {
        onsaleDao.decrInventory(this, this.customer);
    }

    //退款
    public void refund(UserDto user) {
        if (this.getPayTransIdList() == null) {
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST, String.format(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage().replace("%d", "%s"), "订单中的支付单", this.getObjectId()));
        }
        List<Long> payTransIdList = this.getPayTransIdList();
        for (Long payTransId : payTransIdList) {
            InternalReturnObject<PayTrans> result = paymentDao.getPayment(this.getShop().getId(), payTransId);
            Refund refund = new Refund(result.getData());
            paymentDao.createRefund(this.getShop().getId(), payTransId, refund, user);
        }
    }

    //取消运单
    public void cancelPackage(UserDto user) {
        List<OrderPack> packs = this.getPacks();
        for (OrderPack orderPack : packs) {
            freightDao.cancelPackage(this.getShop().getId(), Long.parseLong(orderPack.getObjectId()), user);
        }
    }

    private void updateOrderItems(DiscountInfo discountInfo, List<Coupon> coupons, List<CouponOrderItem> couponOrderItemList, Integer point, UserDto user) {
        this.updateOrderItemsByDiscountAndCoupons(discountInfo, coupons, couponOrderItemList);
        this.updateOrderItemsPoint(point);
        orderItemDao.saveAll(this.orderItems, user);
    }

    private void updateOrderItemsByDiscountAndCoupons(DiscountInfo discountInfo, List<Coupon> coupons, List<CouponOrderItem> couponOrderItemList) {
        IntStream.range(0, couponOrderItemList.size()).forEach(index -> {
            OrderItem orderItem = this.getOrderItemById(couponOrderItemList.get(index).getOrderItemId());
            orderItem.setDiscountPrice(orderItem.getDiscountPrice() - orderItem.getPrice() + discountInfo.getDiscountByOnsaleId(orderItem.getOnsaleId()));
            orderItem.setCoupon(coupons.get(index));
        });
    }

    private OrderItem getOrderItemById(String id) {
        if (this.orderItems == null) {
            this.orderItems = orderItemDao.findAllByOrderId(this.objectId, this.customer.getId());
        }
        Optional<OrderItem> ret = this.orderItems.stream().filter(orderItem -> Objects.equals(orderItem.getObjectId(), id)).findFirst();
        return ret.orElse(null);
    }

    private void updateOrderItemsPoint(Integer point) {
        this.orderItems.forEach(orderItem ->
                orderItem.setPoint(point * orderItem.getDiscountPrice() / this.discountPrice)
        );
    }


    public void setStateInterfaceByCode(int code) {
        stateInf = StateInf.statePool.get(code);
    }

    public void setStateInterface(StateInf stateInf) {
        this.stateInf = stateInf;
    }

    public StateInf getStateInterface() {
        return this.stateInf;
    }

    public int getCode() {
        return this.stateInf.getCode();
    }

    public CouponDao getCouponDao() {
        return couponDao;
    }

    public void setCouponDao(CouponDao couponDao) {
        this.couponDao = couponDao;
    }

    @Override
    public void setGmtCreate(LocalDateTime gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    @Override
    public void setGmtModified(LocalDateTime gmtModified) {
        this.gmtModified = gmtModified;
    }

    public OrderItemDao getOrderItemDao() {
        return orderItemDao;
    }

    public void setOrderItemDao(OrderItemDao orderItemDao) {
        this.orderItemDao = orderItemDao;
    }

    public OnsaleDao getOnsaleDao() {
        return onsaleDao;
    }

    public void setOnsaleDao(OnsaleDao onsaleDao) {
        this.onsaleDao = onsaleDao;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getOrderSn() {
        return orderSn;
    }

    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn;
    }

    public IdName getCustomer() {
        return customer;
    }

    public void setCustomer(IdName customer) {
        this.customer = customer;
    }

    public IdName getShop() {
        return shop;
    }

    public void setShop(IdName shop) {
        this.shop = shop;
    }

    public Integer getOriginPrice() {
        return originPrice;
    }

    public void setOriginPrice(Integer originPrice) {
        this.originPrice = originPrice;
    }

    public Integer getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(Integer discountPrice) {
        this.discountPrice = discountPrice;
    }

    public Integer getExpressFee() {
        return expressFee;
    }

    public void setExpressFee(Integer expressFee) {
        this.expressFee = expressFee;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Consignee getConsignee() {
        return consignee;
    }

    public void setConsignee(Consignee consignee) {
        this.consignee = consignee;
    }

    public List<OrderPack> getPacks() {
        return packs;
    }

    public void setPacks(List<OrderPack> packs) {
        this.packs = packs;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    @CopyFrom.Exclude({OrderVo.class})
    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public List<Long> getPayTransIdList() {
        return payTransIdList;
    }

    public void setPayTransIdList(List<Long> payTransIdList) {
        this.payTransIdList = payTransIdList;
    }

    public StateInf getStateInf() {
        return stateInf;
    }

    public void setStateInf(StateInf stateInf) {
        this.stateInf = stateInf;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }


    public FreightDao getFreightDao() {
        return freightDao;
    }

    public void setFreightDao(FreightDao freightDao) {
        this.freightDao = freightDao;
    }

    public PaymentDao getPaymentDao() {
        return paymentDao;
    }

    public void setPaymentDao(PaymentDao paymentDao) {
        this.paymentDao = paymentDao;
    }

    public PackGroup getPackGroup() {
        return packGroup;
    }

    public void setPackGroup(PackGroup packGroup) {
        this.packGroup = packGroup;
    }

    public DiscountGroup getDiscountGroup() {
        return discountGroup;
    }

    public void setDiscountGroup(DiscountGroup discountGroup) {
        this.discountGroup = discountGroup;
    }

    public Integer getPoint() {
        return point;
    }

    public OrderPackDao getOrderPackDao() {
        return orderPackDao;
    }

    public void setOrderPackDao(OrderPackDao orderPackDao) {
        this.orderPackDao = orderPackDao;
    }
}
