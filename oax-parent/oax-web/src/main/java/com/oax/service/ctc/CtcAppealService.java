package com.oax.service.ctc;

/**
 * CTC 订单申诉记录
 */
public interface CtcAppealService {

    void saveOne(Integer userId, Long ctcOrderId, String appealDesc);

}
