package cn.edu.xmu.oomall.prodorder.mapper.openfeign.po;

import cn.edu.xmu.oomall.prodorder.dao.bo.IdName;
import cn.edu.xmu.oomall.prodorder.dao.bo.SimpleCouponAct;

import java.io.Serializable;
import java.util.List;

public class FullProductPo implements Serializable {

    private class SimpleProduct {
        private Long id;
        private String name;
        private Integer price;
        private Integer quantity;
        private Byte status;
    }

    private Long id;
    private IdName shop;
    private String name;
    private Integer originalPrice;
    private Integer weight;
    private Integer price;
    private Integer quantity;
    private Integer maxQuantity;
    private Byte status;
    private String unit;
    private String barCode;
    private String originPlace;
    private IdName category;
    private IdName freightTemplate;
    private List<SimpleProduct> otherProducts;
    private List<SimpleCouponAct> actList;
    private Integer freeThreshold;  // 免邮金额，如果是-1返回商铺默认门槛
    private Integer commissionRatio;  // 平台分账比例

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

    public Integer getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(Integer originalPrice) {
        this.originalPrice = originalPrice;
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

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getMaxQuantity() {
        return maxQuantity;
    }

    public void setMaxQuantity(Integer maxQuantity) {
        this.maxQuantity = maxQuantity;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public String getOriginPlace() {
        return originPlace;
    }

    public void setOriginPlace(String originPlace) {
        this.originPlace = originPlace;
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

    public List<SimpleProduct> getOtherProducts() {
        return otherProducts;
    }

    public void setOtherProducts(List<SimpleProduct> otherProducts) {
        this.otherProducts = otherProducts;
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
