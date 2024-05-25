package cn.edu.xmu.oomall.prodorder.dao.openfeign.shop.bo;

import cn.edu.xmu.javaee.core.util.CloneFactory;
import cn.edu.xmu.oomall.prodorder.dao.bo.ItemInfo;
import cn.edu.xmu.oomall.prodorder.dao.bo.OrderPack;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class PackInfo extends ItemInfo {

    private List<OrderPack> pack;

    public PackInfo(Integer freightFee, List<OrderPack> pack) {
        super(freightFee);
        this.pack = pack;
    }

    public PackInfo() { }
}
