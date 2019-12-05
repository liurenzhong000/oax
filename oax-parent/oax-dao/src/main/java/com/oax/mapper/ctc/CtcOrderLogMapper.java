package com.oax.mapper.ctc;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.oax.entity.ctc.CtcOrderLog;
import com.oax.entity.enums.CtcOrderStatus;

public interface CtcOrderLogMapper extends BaseMapper<CtcOrderLog> {

    //获取用户申诉前的订单状态
    CtcOrderStatus getBeforeAppealStatus(Long ctcOrderId);
}
