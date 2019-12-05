package com.oax.mapper.activity;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.oax.entity.activity.DiceRandomRate;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Auther: hyp
 * @Date: 2019/1/5 18:21
 * @Description:
 */
public interface DiceRandomRateMapper extends BaseMapper<DiceRandomRate> {
    List<DiceRandomRate> pageForAdmin(@Param("coinId") Integer coinId);

    Integer getRandomRateByCoinId(Integer coinId);
}
