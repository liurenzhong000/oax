package com.oax.mapper.front;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.oax.entity.front.UserCoinSnapshoot;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface UserCoinSnapshootMapper extends BaseMapper<UserCoinSnapshoot> {

    BigDecimal subordinateSum(@Param("startTime") Date startTime, @Param("endTime")Date endTime, @Param("userIds") List<Integer> userIds);

}
