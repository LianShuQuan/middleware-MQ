package cn.edu.xmu.oomall.prodorder.mapper.openfeign.po;

import cn.edu.xmu.javaee.core.model.dto.IdNameTypeDto;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
public class SimpleTrans {
    private Long id;
    private String outNo;
    private String transNo;
    private Long amount;
    private Byte status;
    private LocalDateTime successTime;
    private IdNameTypeDto channel;

    private IdNameTypeDto adjustor;

    private LocalDateTime adjustTime;
    private Ledger ledger;

    private class Ledger{
        private Long id;
        private String outNo;
        private String transNo;
        private Long amount;
        private LocalDateTime checkTime;
    }
}
