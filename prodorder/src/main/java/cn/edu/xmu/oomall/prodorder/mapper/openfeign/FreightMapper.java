package cn.edu.xmu.oomall.prodorder.mapper.openfeign;

import cn.edu.xmu.javaee.core.aop.LoginUser;
import cn.edu.xmu.javaee.core.model.InternalReturnObject;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import com.mongodb.lang.Nullable;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(name = "freight-service")
public interface FreightMapper {


    //取消运单
    @PutMapping("/internal/shops/{shopId}/packages/{id}/cancel")
    public InternalReturnObject<Nullable> cancelPackage(@PathVariable Long shopId, @PathVariable Long id, @LoginUser UserDto user);
}
