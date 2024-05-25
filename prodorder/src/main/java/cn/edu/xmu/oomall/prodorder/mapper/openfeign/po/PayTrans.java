package cn.edu.xmu.oomall.prodorder.mapper.openfeign.po;

import cn.edu.xmu.javaee.core.model.dto.IdNameTypeDto;

import java.time.LocalDateTime;

public class PayTrans {
    private Long id;

    private String outNo;

    private String transNo;

    private Long amount;

    private Long divAmount;

    private LocalDateTime successTime;

    private String prepayId;

    private Byte inRefund;

    private IdNameTypeDto channel;

    private Byte status;

    private LocalDateTime timeBegin;

    private LocalDateTime timeExpire;

    private IdNameTypeDto adjustor;

    private LocalDateTime adjustTime;

    private IdNameTypeDto creator;

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModified;

    private IdNameTypeDto modifier;
    private Ledger ledger;

    private class Ledger{
        private Long id;
        private String outNo;
        private String transNo;
        private Long amount;
        private LocalDateTime checkTime;
    }

    public Long getAmount() {
        return amount;
    }

    public Long getDivAmount() {
        return divAmount;
    }
}
