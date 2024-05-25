package cn.edu.xmu.oomall.prodorder.mapper.rocketmq.po;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class PointAndCouponMessage {
    List<Long> couponIdList;
    Integer point;
}
