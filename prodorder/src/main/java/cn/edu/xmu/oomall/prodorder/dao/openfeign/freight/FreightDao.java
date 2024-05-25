package cn.edu.xmu.oomall.prodorder.dao.openfeign.freight;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.InternalReturnObject;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.prodorder.mapper.openfeign.FreightMapper;
import com.mongodb.lang.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class FreightDao {
    private FreightMapper freightMapper;

    @Autowired
    public FreightDao(FreightMapper freightMapper) {
        this.freightMapper = freightMapper;
    }

    public void cancelPackage(Long shopId, Long packageId,UserDto user) {
        InternalReturnObject<Nullable> internalReturnObject=freightMapper.cancelPackage(shopId, packageId, user);
        if(internalReturnObject.getErrno()!= ReturnNo.OK.getErrNo()){
            throw new BusinessException(ReturnNo.INTERNAL_SERVER_ERR,ReturnNo.INTERNAL_SERVER_ERR.getMessage());
        }
    }
}
