package cn.edu.xmu.oomall.prodorder.dao.openfeign.shop.bo;

import cn.edu.xmu.oomall.prodorder.dao.bo.TemplateGroup;
import cn.edu.xmu.oomall.prodorder.dao.bo.ItemInfo;
import cn.edu.xmu.oomall.prodorder.dao.bo.OrderItem;
import cn.edu.xmu.oomall.prodorder.dao.bo.OrderPack;
import cn.edu.xmu.oomall.prodorder.dao.openfeign.product.bo.Product;
import cn.edu.xmu.oomall.prodorder.dao.openfeign.shop.PackDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class PackGroup extends TemplateGroup {

    private PackDao packDao;

    @Autowired
    public PackGroup(PackDao packDao) {
        this.packDao = packDao;
    }

    @Override
    protected HashMap<Long, List<OrderItem>> buildMap(List<OrderItem> orderItems) {
        HashMap<Long, List<OrderItem>> packMap = new HashMap<>();
        orderItems.forEach(oi -> {
            Product product = oi.getProduct();
            if (product == null) {
                throw new RuntimeException("PackGroup: construct null");
            }
            Long templateId = product.getFreightTemplate().getId();
            if (!packMap.containsKey(templateId)) {
                packMap.put(templateId, new ArrayList<>());
            }
            packMap.get(templateId).add(oi);
        });
        return packMap;
    }

    @Override
    protected List<ItemInfo> accessData(Long regionId, HashMap<Long, List<OrderItem>> packMap) {
        return new ArrayList<> (
                packMap.entrySet().stream().map(entry -> packDao.getPacks(entry.getKey(), regionId, entry.getValue())).toList()
        );
    }

    @Override
    protected PackInfo repack(List<ItemInfo> packInfoList) {
        List<OrderPack> orderPacks = new ArrayList<>();
        packInfoList.forEach(packInfo -> orderPacks.addAll(((PackInfo) packInfo).getPack()));
        Integer freightPrice = calculate(packInfoList);
        return new PackInfo(freightPrice, orderPacks);
    }

    @Override
    protected Integer calculate(List<ItemInfo> packInfoList) {
        return packInfoList.stream().mapToInt(ItemInfo::getSpecialPrice).sum();
    }

}
