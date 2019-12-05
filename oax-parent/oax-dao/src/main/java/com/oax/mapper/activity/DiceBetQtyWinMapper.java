package com.oax.mapper.activity;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.oax.entity.activity.DiceBetQtyWin;
import com.oax.entity.admin.param.PageParam;
import com.oax.entity.front.vo.DiceBetQtyWinVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Auther: hyp
 * @Date: 2019/1/4 17:11
 * @Description:
 */
public interface DiceBetQtyWinMapper extends BaseMapper<DiceBetQtyWin> {

    List<DiceBetQtyWinVo> pageForAdmin(@Param("coinId") Integer coinId);

    List<DiceBetQtyWin> selectOpenByCoinIdOrderBetQty(@Param("coinId") Integer coinId);

}
