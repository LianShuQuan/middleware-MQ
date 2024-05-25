//School of Informatics Xiamen University, GPL-3.0 license

package cn.edu.xmu.oomall.prodorder.controller;

import cn.edu.xmu.javaee.core.aop.Audit;
import cn.edu.xmu.javaee.core.util.CloneFactory;
import cn.edu.xmu.javaee.core.aop.LoginUser;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.ReturnObject;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.prodorder.controller.vo.OrderVo;
import cn.edu.xmu.oomall.prodorder.controller.vo.PayInfoVo;
import cn.edu.xmu.oomall.prodorder.dao.bo.CouponOrderItem;
import cn.edu.xmu.oomall.prodorder.dao.bo.Order;
import cn.edu.xmu.oomall.prodorder.dao.bo.OrderItem;
import cn.edu.xmu.oomall.prodorder.dao.bo.PayInfo;
import cn.edu.xmu.oomall.prodorder.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(produces = "application/json;charset=UTF-8")
public class CustomerController {

    private OrderService orderService;

    @Autowired
    public CustomerController(OrderService orderService) {
        this.orderService = orderService;
    }

    @Audit
    @PostMapping("/orders")
    public ReturnObject createOrder(@RequestBody @Validated OrderVo orderVo, @LoginUser UserDto user) {
        List<OrderItem> orderItems = orderVo.getOrderItems().stream().map(oi -> CloneFactory.copy(new OrderItem(), oi)).toList();
        Order order = CloneFactory.copy(new Order(), orderVo);
        order.setOrderItems(orderItems);
        orderService.createOrder(order, user);
        return new ReturnObject(ReturnNo.CREATED);
    }

    @Audit
    @PostMapping("/orders/{objectId}/pay")
    public ReturnObject pay(@PathVariable("objectId") String objectId, @RequestBody @Validated PayInfoVo payInfoVo, @LoginUser UserDto user) {
        List<CouponOrderItem> couponOrderItemsList = payInfoVo.getCouponOrderItemList().stream().map(item -> CloneFactory.copy(new CouponOrderItem(), item)).toList();
        PayInfo payInfo = CloneFactory.copy(new PayInfo(), payInfoVo);
        payInfo.setCouponOrderItemList(couponOrderItemsList);
        orderService.createPayment(objectId, payInfo, user);
        return new ReturnObject();
    }

    @Audit
    @DeleteMapping("/orders/{objectId}")
    public ReturnObject cancelOrder(@PathVariable("objectId") String id, @LoginUser UserDto user) {
        orderService.cancelOrder(id, user);
        return new ReturnObject(ReturnNo.OK);
    }

}
