package com.oax.mapper.front;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.oax.entity.front.UserCoinSnapshoot;
import com.oax.entity.front.UserCoinSnapshootLight;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface UserCoinSnapshootLightMapper extends BaseMapper<UserCoinSnapshootLight> {

    BigDecimal subordinateSum(@Param("startTime") Date startTime, @Param("endTime") Date endTime, @Param("userIds") List<Integer> userIds);

    List<UserCoinSnapshootLight> pageForAdmin(@Param("userId") Integer userId, @Param("startTime") Date startTime, @Param("endTime") Date endTime);
}
