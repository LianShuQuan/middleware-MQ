package cn.edu.xmu.oomall.prodorder.mapper.openfeign;


import cn.edu.xmu.javaee.core.model.InternalReturnObject;
import cn.edu.xmu.oomall.prodorder.dao.openfeign.region.bo.Region;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("region-service")
public interface RegionMapper {

    /**
     * regionId换region对象
     * @param id
     * @return
     */
    @GetMapping("/regions/{id}")
    InternalReturnObject<Region> findRegionById(@PathVariable("id") Long id);

}
