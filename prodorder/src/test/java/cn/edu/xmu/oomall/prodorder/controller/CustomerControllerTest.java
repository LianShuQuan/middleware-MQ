package cn.edu.xmu.oomall.prodorder.controller;

import cn.edu.xmu.javaee.core.mapper.RedisUtil;
import cn.edu.xmu.javaee.core.model.InternalReturnObject;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.util.CloneFactory;
import cn.edu.xmu.javaee.core.util.JwtHelper;
import cn.edu.xmu.oomall.prodorder.OrderApplication;
import cn.edu.xmu.oomall.prodorder.dao.bo.IdName;
import cn.edu.xmu.oomall.prodorder.dao.bo.SimpleCouponAct;
import cn.edu.xmu.oomall.prodorder.dao.bo.state.StateInf;
import cn.edu.xmu.oomall.prodorder.dao.openfeign.customer.bo.Consignee;
import cn.edu.xmu.oomall.prodorder.dao.openfeign.product.bo.DiscountItem;
import cn.edu.xmu.oomall.prodorder.dao.openfeign.product.bo.Product;
import cn.edu.xmu.oomall.prodorder.dao.openfeign.region.bo.Region;
import cn.edu.xmu.oomall.prodorder.mapper.OrderItemPoMapper;
import cn.edu.xmu.oomall.prodorder.mapper.OrderPoMapper;
import cn.edu.xmu.oomall.prodorder.mapper.openfeign.*;
import cn.edu.xmu.oomall.prodorder.mapper.openfeign.po.*;
import cn.edu.xmu.oomall.prodorder.mapper.po.OrderItemPo;
import cn.edu.xmu.oomall.prodorder.mapper.po.OrderPo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;


/**
 * 由于使用了 standalone 的 mongodb
 * 因此 必须注释掉测试的事务行为
 * 理由：参考 <a href="https://www.mongodb.com/zh-cn/docs/manual/core/retryable-writes/#std-label-retryable-writes">MongoDB官网对 retry write 行为的解释</a>，standalone 的 mongodb 不支持事务和 retry write，只有用了副本集才会支持事务和 retry write。**在测试中启用事务**后，因此在事务提交时必然出错
 * 问1：为什么报错信息显示的是 `This MongoDB deployment does not support retryable writes. Please add retryWrites=false to your connection string.`，并且我设置了 `retryWrites=false` 也没用？
 * 答1：参考 <a href="https://www.mongodb.com/zh-cn/docs/manual/core/retryable-writes/#retryable-writes-and-multi-document-transactions">可重试写入和多文档事务</a> 知 “事务提交和中止操作是可重试的写入操作。如果提交操作或中止操作遇到错误，则不管 `retryWrites` 是否设置为 false，MongoDB 驱动程序都会重试该操作一次。”
 * 问2：为什么不使用 replicaset 的 mongodb ？
 * 答2：参考 <a href="https://www.mongodb.com/zh-cn/docs/manual/tutorial/install-mongodb-on-windows/#localhost-binding-by-default">MongoDB官方安装教程</a>，得知只要是在 localhost 运行 mongodb，就无法使用 replicaset，必须有对外暴露的接口
 */
@SpringBootTest(classes = OrderApplication.class)
@AutoConfigureMockMvc
//@Transactional(propagation = Propagation.REQUIRED)
public class CustomerControllerTest {

    private Logger logger = LoggerFactory.getLogger(CustomerControllerTest.class);

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RedisUtil redisUtil;
    @MockBean
    private CustomerMapper customerMapper;
    @MockBean
    private FreightMapper freightMapper;
    @MockBean
    private PaymentMapper paymentMapper;
    @MockBean
    private ProductMapper productMapper;
    @MockBean
    private RegionMapper regionMapper;
    @MockBean
    private ShopMapper shopMapper;
    @MockBean
    private OrderPoMapper orderPoMapper;
    @MockBean
    private OrderItemPoMapper orderItemPoMapper;

    private static String adminToken1;
    private static String adminToken2;
    private static Long userId1 = 1L;
    private static Long userId2 = 2L;

    private final String ORDERS = "/orders";
    private final String CANCEL_ORDER = "/orders/{objectId}";

    private static FullProductPo findProductRetObj1 = new FullProductPo();
    private static FullProductPo findProductRetObj2 = new FullProductPo();

    private static Pack calcuFreightFeeRetObj1 = new Pack();
    private static Pack calcuFreightFeeRetObj2 = new Pack();

    @BeforeAll
    public static void setup() {
        JwtHelper jwtHelper = new JwtHelper();
        adminToken1 = jwtHelper.createToken(userId1, "13088admin", 0L, 1, 3600);
        adminToken2 = jwtHelper.createToken(userId2, "13089admin", 0L, 1, 3600);

        initObjects();
    }

    @Test
    public void testRabbitMQ() throws Exception {
        // 以 (id=1, New), (id=2, New), (id=2, Revoke), (id=1, Revoke) 顺序发送消息
        // 期望收到的效果是id=1的消息均被queue-1接收，id=2的消息均被queue-2接收
        performPost(adminToken1, userId1, "111");    // user1 创建 id 为 111 的订单
        logger.info("user1 create order(id=111). ");
        performPost(adminToken2, userId2, "333");    // user2 创建 id 为 333 的订单
        logger.info("user2 create order(id=333). ");
        performDelete(adminToken2, userId2, "333");  // user2 取消 id 为 333 的订单
        logger.info("user2 revoke order(id=333). ");
        performDelete(adminToken1, userId1, "111");  // user1 取消 id 为 111 的订单
        logger.info("user1 revoke order(id=111). ");
    }

    private void performPost(String adminToken, Long userId, String objectId) throws Exception {
        mockBeforeCreateOrder(userId, objectId);

        String body = """
                {"orderItems":[{"onsaleId":2,"quantity":2,"actId":98},{"onsaleId":134,"quantity":1,"actId":6}],"consignee":{"name":"Jack Ma","mobile":"12399768823","regionId":5,"address":"幸福小区02室"},"message":"商家留言"}""";

        this.mockMvc.perform(MockMvcRequestBuilders.post(ORDERS)
                        .header("authorization", adminToken)
                        .contentType("application/json;charset=UTF-8")
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.CREATED.getErrNo())));
    }

    private void performDelete(String adminToken, Long userId, String objectId) throws Exception {
        mockBeforeCancelOrder(userId, objectId);

        this.mockMvc.perform(MockMvcRequestBuilders.delete(CANCEL_ORDER, objectId)
                        .header("authorization", adminToken)
                        .contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno").value(0));
    }

    private OrderPo contructOrderPo(Long userId, String objectId) {
        OrderPo orderPo = new OrderPo();
        orderPo.setOrderSn("O" + objectId);
        orderPo.setConsignee(new Consignee("张三", "123456789", 10L, "123456"));
        orderPo.setStatus(StateInf.CANCEL);
        orderPo.setObjectId(objectId);
        orderPo.setCreatorId(userId);
        orderPo.setGmtCreate(LocalDateTime.now());
        orderPo.setGmtModified(LocalDateTime.now());
        orderPo.setShop(new IdName(1L, "shop1"));
        orderPo.setPayTransIdList(new ArrayList<>() {
            {
                add(1L);
                add(2L);
                add(5L);
            }
        });
        orderPo.setCustomer(new IdName(userId, "张三"));
        orderPo.setPoint(123);
        orderPo.setStatus(StateInf.WAIT_PAY);

        return orderPo;
    }

    private List<OrderItemPo> contructOrderItemPoList(String objectId) {
        List<OrderItemPo> orderItemPoList = new ArrayList<>();

        OrderItemPo orderItemPo1 = new OrderItemPo();
        orderItemPo1.setOrderId(objectId);
        orderItemPo1.setOnsaleId(2L);
        orderItemPo1.setQuantity(2);
        orderItemPo1.setCoupon(new CouponPo(1L, 98L, "coupon1", "coupon1Sn"));
        orderItemPo1.setPoint(10);
        orderItemPo1.setProduct(CloneFactory.copy(new Product(), findProductRetObj1));

        OrderItemPo orderItemPo2 = new OrderItemPo();
        orderItemPo2.setOrderId(objectId);
        orderItemPo2.setOnsaleId(134L);
        orderItemPo2.setQuantity(1);
        orderItemPo2.setCoupon(new CouponPo(2L, 6L, "coupon2", "coupon2Sn"));
        orderItemPo2.setPoint(10);
        orderItemPo2.setProduct(CloneFactory.copy(new Product(), findProductRetObj2));

        orderItemPoList.add(orderItemPo1);
        orderItemPoList.add(orderItemPo2);

        return orderItemPoList;
    }

    private void mockBeforeCreateOrder(Long userId, String objectId) {
        List<OrderItemPo> orderItemPoList = contructOrderItemPoList(objectId);
        OrderPo orderPo = contructOrderPo(userId, objectId);

        Mockito.when(redisUtil.hasKey(Mockito.any())).thenReturn(false);
        Mockito.when(redisUtil.get("fp_2")).thenReturn(findProductRetObj1);
        Mockito.when(redisUtil.get("fp_134")).thenReturn(findProductRetObj2);

        /* openfeign mapper */
        Mockito.when(regionMapper.findRegionById(Mockito.anyLong())).thenReturn(new InternalReturnObject<>(new Region()));
        Mockito.when(productMapper.findProductByOnsaleId(2L)).thenReturn(new InternalReturnObject<>(findProductRetObj1));
        Mockito.when(productMapper.findProductByOnsaleId(134L)).thenReturn(new InternalReturnObject<>(findProductRetObj2));
        Mockito.when(productMapper.calcuDiscountsByActId(Mockito.any(), Mockito.any())).thenReturn(
                new InternalReturnObject<>(
                        new DiscountInfoPo(
                                new ArrayList<>() {{
                                    add(new DiscountItem(2L, 2, 463, 400));
                                    add(new DiscountItem(134L, 1, 67699, 67000));
                                }}
                        )
                )
        );
        Mockito.when(shopMapper.calcuFreightFee(Mockito.anyLong(), Mockito.anyLong(), Mockito.any())).thenReturn(new InternalReturnObject<>(calcuFreightFeeRetObj1));

        /* Mongo Mapper */
        Mockito.when(orderPoMapper.save(Mockito.any())).thenReturn(orderPo);
        Mockito.when(orderPoMapper.findById(Mockito.any())).thenReturn(Optional.of(orderPo));
        Mockito.when(orderPoMapper.insert(Mockito.any(OrderPo.class))).thenReturn(orderPo);
        Mockito.when(orderItemPoMapper.insert(Mockito.any(List.class))).thenReturn(orderItemPoList);
    }

    private void mockBeforeCancelOrder(Long userId, String objectId) {
        List<OrderItemPo> orderItemPoList = contructOrderItemPoList(objectId);
        OrderPo orderPo = contructOrderPo(userId, objectId);

        Mockito.when(orderPoMapper.findById(Mockito.any())).thenReturn(Optional.of(orderPo));
        Mockito.when(orderItemPoMapper.findAllByOrderId(Mockito.any())).thenReturn(orderItemPoList);
    }

    private static void initObjects() {
        findProductRetObj1.setId(1551L);
        findProductRetObj1.setName("欢乐家杨梅罐头");
        findProductRetObj1.setPrice(463);
        findProductRetObj1.setWeight(700);
        findProductRetObj1.setShop(new IdName(3L, "向往时刻"));
        findProductRetObj1.setQuantity(97);
        findProductRetObj1.setMaxQuantity(50);
        findProductRetObj1.setCategory(new IdName(254L, "洗护清洁"));
        findProductRetObj1.setFreightTemplate(new IdName(16L, "最大背包分包计件模板"));
        findProductRetObj1.setFreeThreshold(-1);
        findProductRetObj1.setCommissionRatio(10);
        findProductRetObj1.setActList(new ArrayList<>() {{
            add(new SimpleCouponAct(98L, "满100打9折"));
        }});

        findProductRetObj2.setId(1683L);
        findProductRetObj2.setName("恋味炖鱼料");
        findProductRetObj2.setPrice(67699);
        findProductRetObj2.setWeight(28);
        findProductRetObj2.setShop(new IdName(3L, "向往时刻"));
        findProductRetObj2.setQuantity(93);
        findProductRetObj2.setMaxQuantity(50);
        findProductRetObj2.setCategory(new IdName(193L, "冬季女鞋"));
        findProductRetObj2.setFreightTemplate(new IdName(19L, "优费计件模板"));
        findProductRetObj2.setFreeThreshold(-1);
        findProductRetObj2.setCommissionRatio(10);
        findProductRetObj1.setActList(new ArrayList<>() {{
            add(new SimpleCouponAct(6L, "满3件9折"));
        }});

        calcuFreightFeeRetObj1.setFreightPrice(1212);
        calcuFreightFeeRetObj1.setPack(new ArrayList<>() {{
            add(new ArrayList<>() {{
                add(new ProductItemPo(null, 1551L, 2));
            }});
        }});

        calcuFreightFeeRetObj2.setFreightPrice(1212);
        calcuFreightFeeRetObj1.setPack(new ArrayList<>() {{
            add(new ArrayList<>() {{
                add(new ProductItemPo(null, 1683L, 1));
            }});
        }});
    }
}
