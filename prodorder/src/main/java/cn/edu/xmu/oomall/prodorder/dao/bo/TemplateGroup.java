package cn.edu.xmu.oomall.prodorder.dao.bo;

import java.util.HashMap;
import java.util.List;

public abstract class TemplateGroup {
    public ItemInfo construct(Long id, List<OrderItem> orderItems) {
        return repack(accessData(id, buildMap(orderItems)));
    }

    protected abstract HashMap<Long, List<OrderItem>> buildMap(List<OrderItem> orderItems);

    protected abstract List<ItemInfo> accessData(Long id, HashMap<Long, List<OrderItem>> map);

    protected abstract ItemInfo repack(List<ItemInfo> itemInfoList);

    protected abstract Integer calculate(List<ItemInfo> itemInfoList);
}
