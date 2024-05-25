//School of Informatics Xiamen University, GPL-3.0 license

package cn.edu.xmu.oomall.prodorder.mapper;

import cn.edu.xmu.oomall.prodorder.mapper.po.OrderItemPo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemPoMapper extends MongoRepository<OrderItemPo, String> {
    List<OrderItemPo> findAllByOrderId(String orderId);
}
