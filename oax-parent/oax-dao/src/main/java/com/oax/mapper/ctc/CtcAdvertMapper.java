package com.oax.mapper.ctc;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.oax.entity.ctc.CtcAdvert;
import com.oax.entity.front.vo.CtcAdvertVo;
import com.oax.entity.front.vo.ListCtcAdvertVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface CtcAdvertMapper extends BaseMapper<CtcAdvert> {

    boolean hasPutawayByType(@Param("userId")Integer userId, @Param("type")int type);

    CtcAdvertVo getSaleAdvert();

    CtcAdvertVo getBuyAdvert();

    List<ListCtcAdvertVo> pageForWeb(Map<String, Object> params);

    void closeAllByUserId(Integer userId);

    List<CtcAdvertVo> getTopSaleTwoAdvertVo();

    List<CtcAdvertVo> getTopBuyTwoAdvertVo();

    List<ListCtcAdvertVo> pageForAdvert(Map<String, Object> params);
}
