package cn.edu.xmu.oomall.prodorder.dao.openfeign.region.bo;

import cn.edu.xmu.javaee.core.model.dto.IdNameTypeDto;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@NoArgsConstructor
public class Region implements Serializable {
    private Long id;
    private String name;
    private Byte status;
    private Byte level;
    private String shortName;
    private String mergerName;
    private String pinyin;
    private Double lng;
    private Double lat;
    private String areaCode;
    private String zipCode;
    private String cityCode;
    private IdNameTypeDto creator;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;
    private IdNameTypeDto modifier;
}
