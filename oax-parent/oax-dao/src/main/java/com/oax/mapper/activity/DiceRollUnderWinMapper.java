package com.oax.mapper.activity;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.oax.entity.activity.DiceBetQtyWin;
import com.oax.entity.activity.DiceRollUnderWin;
import com.oax.entity.front.vo.DiceBetQtyWinVo;
import com.oax.entity.front.vo.DiceRollUnderWinVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Auther: hyp
 * @Date: 2019/1/14 14:54
 * @Description:
 */
public interface DiceRollUnderWinMapper extends BaseMapper<DiceRollUnderWin> {

    List<DiceRollUnderWinVo> pageForAdmin(@Param("coinId") Integer coinId);

    List<DiceRollUnderWin> selectOpenByCoinIdOrderRollUnder(@Param("coinId") Integer coinId);

}
