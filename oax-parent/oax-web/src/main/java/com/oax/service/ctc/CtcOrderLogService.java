package com.oax.service.ctc;

import com.oax.entity.enums.CtcOrderLogType;
import com.oax.entity.enums.CtcOrderStatus;

/**
 * CTC订单状态变更记录相关
 */
public interface CtcOrderLogService {

    void saveOne(Long ctcOrderId, CtcOrderStatus beforeStatus, CtcOrderStatus afterStatus, CtcOrderLogType type, Integer userId);

}
