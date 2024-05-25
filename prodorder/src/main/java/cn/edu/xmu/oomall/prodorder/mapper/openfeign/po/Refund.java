package cn.edu.xmu.oomall.prodorder.mapper.openfeign.po;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Refund {
    @NotNull(message =  "退款金额不能为空")
    @Min(value = 1, message = "退款金额需大于0")
    private Long amount;

    @NotNull(message =  "分账金额不能为空")
    @Min(value = 0, message = "退回分账金额需大于等于0")
    private Long divAmount;
    public Refund(PayTrans payTrans){
        this.amount = payTrans.getAmount();
        this.divAmount = payTrans.getDivAmount();
    }
}
