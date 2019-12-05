package com.oax.service.activity;

import java.math.BigDecimal;

/**
 * @Auther: hyp
 * @Date: 2019/1/14 15:30
 * @Description:
 */
public interface DiceRollUnderWinService {

    Integer getBackendWinByRollUnder(Integer rollUnder, Integer coinId);

}
