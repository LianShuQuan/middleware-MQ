package cn.edu.xmu.oomall.prodorder.mapper;

import cn.edu.xmu.oomall.prodorder.mapper.po.OrderPackPo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderPackPoMapper extends MongoRepository<OrderPackPo, String> {
    List<OrderPackPo> findAllByOrderId(String orderId);
}
