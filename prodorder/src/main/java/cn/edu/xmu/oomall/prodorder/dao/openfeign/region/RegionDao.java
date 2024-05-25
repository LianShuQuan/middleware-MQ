package cn.edu.xmu.oomall.prodorder.dao.openfeign.region;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.mapper.RedisUtil;
import cn.edu.xmu.javaee.core.model.InternalReturnObject;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.oomall.prodorder.mapper.openfeign.RegionMapper;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import cn.edu.xmu.oomall.prodorder.dao.openfeign.region.bo.Region;


@Repository
public class RegionDao {

    private Logger logger = LoggerFactory.getLogger(RegionDao.class);

    private RegionMapper regionMapper;

    private RedisUtil redisUtil;

    @Autowired
    public RegionDao(RegionMapper regionMapper, RedisUtil redisUtil) {
        this.regionMapper = regionMapper;
        this.redisUtil = redisUtil;
    }

    @Value("${oomall.redis.timeout}")
    private Long timeout;

    public Region findRegionById(Long id) {
        Region region = null;
        String key = "region_" + id.toString();
        if (redisUtil.hasKey(key)) {
            region = (Region) redisUtil.get(key);
        } else {
            InternalReturnObject<Region> ret = regionMapper.findRegionById(id);
            if (ret == null || ret.getErrno() != ReturnNo.OK.getErrNo()) {
                logger.debug("findRegionById: ret={}", ret);
                throw new BusinessException(ReturnNo.ORDER_INVOKEAPI_ERR, String.format(ReturnNo.ORDER_INVOKEAPI_ERR.getMessage(), "获取地区", ret == null ? null : ret.getErrno()));
            }
            region = ret.getData();
            redisUtil.set(key, region, timeout);
        }

        return region;
    }
}
