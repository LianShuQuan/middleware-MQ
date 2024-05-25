package cn.edu.xmu.oomall.prodorder.dao.openfeign.customer.bo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Consignee {

    private String name;

    private String mobile;

    private Long regionId;

    private String address;
}
