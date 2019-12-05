package com.oax.service.strategy;

import com.oax.service.ctc.CtcOrderService;
import com.oax.service.delay.DelayQueueDesc;
import com.oax.service.delay.DelayQueueType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 订单超时过期处理
 */
@Service
@Transactional
public class DelayCtcOrderTimeout implements DelayConsumerStrategy{

    @Autowired
    private CtcOrderService ctcOrderService;

    @Override
    public boolean accept(DelayQueueType type) {
        if (type == DelayQueueType.CTC_BUYORDER_TIMEOUT || type == DelayQueueType.CTC_SALEORDER_TIMEOUT) {
            return true;
        }
        return false;
    }

    @Override
    public void handle(DelayQueueDesc item) {
        Long ctcOrderId = Long.parseLong(item.getId());
        ctcOrderService.timeoutCancel(ctcOrderId);
    }
}
