package cn.edu.xmu.oomall.prodorder.dao.openfeign.payment.bo;

import lombok.Data;

import java.time.LocalDateTime;


@Data
public class PayTransInfo {
    String outNo;
    String description;
    String timeExpire;
    String timeBegin;
    Integer amount;
    Integer divAmount;

    public PayTransInfo(String outNo, String description, Integer amount, Integer divAmount){
        this.setDescription(description);
        this.setOutNo(outNo);
        this.setAmount(amount);
        this.setDivAmount(divAmount);
        this.setTimeBegin(LocalDateTime.now().toString());
        this.setTimeExpire(LocalDateTime.now().plusMinutes(10).toString()); //支付期限为10min
    }


}
