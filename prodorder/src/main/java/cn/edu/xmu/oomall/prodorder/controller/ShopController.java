package cn.edu.xmu.oomall.prodorder.controller;

import cn.edu.xmu.javaee.core.aop.Audit;
import cn.edu.xmu.javaee.core.aop.LoginUser;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.ReturnObject;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.prodorder.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(produces = "application/json;charset=UTF-8")
public class ShopController {
    private OrderService orderService;
    @Autowired
    public ShopController(OrderService orderService) {
        this.orderService = orderService;
    }
    @Audit
    @DeleteMapping("/shops/{shopId}/orders/{objectId}")
    public ReturnObject cancelOrderByShop( @LoginUser UserDto user, @PathVariable("shopId") Long shopId,@PathVariable("objectId") String id) {
        orderService.cancelOrderByshop(id, user);
        return new ReturnObject(ReturnNo.OK);
    }
}
