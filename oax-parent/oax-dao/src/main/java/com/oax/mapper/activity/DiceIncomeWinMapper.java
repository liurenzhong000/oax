package com.oax.mapper.activity;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.oax.entity.activity.DiceIncomeWin;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Auther: hyp
 * @Date: 2018/12/31 15:43
 * @Description:
 */
public interface DiceIncomeWinMapper extends BaseMapper<DiceIncomeWin> {

    List<DiceIncomeWin> listOpen();

    List<DiceIncomeWin> pageForAdmin(@Param("coinId") Integer coinId);

    List<DiceIncomeWin> listOpenByCoinId(@Param("coinId") Integer coinId);
}
