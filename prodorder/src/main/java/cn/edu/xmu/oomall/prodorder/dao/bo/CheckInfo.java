package cn.edu.xmu.oomall.prodorder.dao.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheckInfo {
    List<Long> couponList;
    Integer point;
}
