package cn.edu.xmu.oomall.prodorder.dao.openfeign.product.bo;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.oomall.prodorder.dao.bo.IdName;
import cn.edu.xmu.oomall.prodorder.dao.bo.SimpleCouponAct;
import cn.edu.xmu.oomall.prodorder.mapper.openfeign.po.FullProductPo;

import java.util.List;

@CopyFrom({FullProductPo.class})
public class Product {
    private Long id;
    private IdName shop;
    private String name;
    private Integer weight;
    private Integer price;
    private Integer maxQuantity;
    private IdName category;
    private IdName freightTemplate;
    private List<SimpleCouponAct> actList;
    private Integer freeThreshold;
    private Integer commissionRatio;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public IdName getShop() {
        return shop;
    }

    public void setShop(IdName shop) {
        this.shop = shop;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getMaxQuantity() {
        return maxQuantity;
    }

    public void setMaxQuantity(Integer maxQuantity) {
        this.maxQuantity = maxQuantity;
    }

    public IdName getCategory() {
        return category;
    }

    public void setCategory(IdName category) {
        this.category = category;
    }
    public IdName getFreightTemplate() {
        return freightTemplate;
    }

    public void setFreightTemplate(IdName freightTemplate) {
        this.freightTemplate = freightTemplate;
    }

    public List<SimpleCouponAct> getActList() {
        return actList;
    }

    public void setActList(List<SimpleCouponAct> actList) {
        this.actList = actList;
    }

    public Integer getFreeThreshold() {
        return freeThreshold;
    }

    public void setFreeThreshold(Integer freeThreshold) {
        this.freeThreshold = freeThreshold;
    }

    public Integer getCommissionRatio() {
        return commissionRatio;
    }

    public void setCommissionRatio(Integer commissionRatio) {
        this.commissionRatio = commissionRatio;
    }
}
