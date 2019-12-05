package com.oax.mapper.ctc;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.oax.entity.admin.param.CtcAppealParam;
import com.oax.entity.admin.vo.CtcAppealVo;
import com.oax.entity.ctc.CtcAppeal;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CtcAppealMapper extends BaseMapper<CtcAppeal> {

    List<CtcAppealVo> pageForAdmin(CtcAppealParam param);

    CtcAppeal getCanCancelByOrderId(Long ctcOrderId);

    //该用户是否为订单是申诉人
    boolean isAppealed(@Param("ctcOrderId") Long ctcOrderId, @Param("userId") Integer userId);
}
