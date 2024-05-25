package cn.edu.xmu.oomall.prodorder.mapper.openfeign.po;

import cn.edu.xmu.javaee.core.model.dto.IdNameTypeDto;

import java.io.Serializable;
import java.time.LocalDateTime;

public class CouponAct implements Serializable {

    private Long id;

    private String name;

    private Integer quantity;

    private LocalDateTime couponTime;

    private Integer quantityType;

    private IdNameTypeDto shop;

    private Integer validTerm;

    private String strategy;

    private IdNameTypeDto creator;

    private IdNameTypeDto modifier;

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModified;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public LocalDateTime getCouponTime() {
        return couponTime;
    }

    public void setCouponTime(LocalDateTime couponTime) {
        this.couponTime = couponTime;
    }

    public Integer getQuantityType() {
        return quantityType;
    }

    public void setQuantityType(Integer quantityType) {
        this.quantityType = quantityType;
    }

    public IdNameTypeDto getShop() {
        return shop;
    }

    public void setShop(IdNameTypeDto shop) {
        this.shop = shop;
    }

    public Integer getValidTerm() {
        return validTerm;
    }

    public void setValidTerm(Integer validTerm) {
        this.validTerm = validTerm;
    }

    public String getStrategy() {
        return strategy;
    }

    public void setStrategy(String strategy) {
        this.strategy = strategy;
    }

    public IdNameTypeDto getCreator() {
        return creator;
    }

    public void setCreator(IdNameTypeDto creator) {
        this.creator = creator;
    }

    public IdNameTypeDto getModifier() {
        return modifier;
    }

    public void setModifier(IdNameTypeDto modifier) {
        this.modifier = modifier;
    }

    public LocalDateTime getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(LocalDateTime gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public LocalDateTime getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(LocalDateTime gmtModified) {
        this.gmtModified = gmtModified;
    }
}
