package cn.edu.xmu.oomall.prodorder.mapper.openfeign.po;


import java.util.List;

public class Pack {
    private Integer freightPrice;
    private List<List<ProductItemPo>> pack;

    public Integer getFreightPrice() {
        return freightPrice;
    }

    public void setFreightPrice(Integer freightPrice) {
        this.freightPrice = freightPrice;
    }

    public List<List<ProductItemPo>> getPack() {
        return pack;
    }

    public void setPack(List<List<ProductItemPo>> pack) {
        this.pack = pack;
    }
}
