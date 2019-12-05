package com.oax.service.activity;

import com.oax.druid.DataSource;
import com.oax.druid.DataSourceType;

/**
 * @Auther: hyp
 * @Date: 2019/1/5 18:34
 * @Description:
 */
public interface DiceRandomRateService {
    @DataSource(DataSourceType.SLAVE)
    Integer getRandomRateByCoinId(Integer coinId);
}
