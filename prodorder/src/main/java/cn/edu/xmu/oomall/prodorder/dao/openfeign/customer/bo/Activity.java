package cn.edu.xmu.oomall.prodorder.dao.openfeign.customer.bo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Activity {
    private Long id;
    private String name;
    private Integer quantity;
    private LocalDateTime couponTime;
}
