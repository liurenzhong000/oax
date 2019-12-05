package com.oax.mapper.front;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.oax.entity.front.BonusInfo;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

public interface BonusInfoMapper extends BaseMapper<BonusInfo> {

    int addBanlance(@Param("userId") Integer userId, @Param("qty") BigDecimal qty);
}
