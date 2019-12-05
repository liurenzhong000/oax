package com.oax.service.activity;

import java.math.BigDecimal;

/**
 * @Auther: hyp
 * @Date: 2019/1/5 15:16
 * @Description:
 */
public interface DiceBetQtyWinService {

    Integer getBackendWinByBetQty(BigDecimal betQty, Integer coinId);
}
