//School of Informatics Xiamen University, GPL-3.0 license

package cn.edu.xmu.oomall.prodorder.mapper;

import cn.edu.xmu.oomall.prodorder.mapper.po.OrderPo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderPoMapper extends MongoRepository<OrderPo, String> {

}