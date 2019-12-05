package com.oax.service.impl.activity;

import com.oax.druid.DataSource;
import com.oax.druid.DataSourceType;
import com.oax.mapper.activity.DiceRandomRateMapper;
import com.oax.service.activity.DiceRandomRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * @Auther: hyp
 * @Date: 2019/1/5 18:34
 * @Description:
 */
@Service
@Transactional
public class DiceRandomRateServiceImpl implements DiceRandomRateService {

    public static final Integer DEFAULT_RANDOM_RATE = 100;

    @Autowired
    private DiceRandomRateMapper diceRandomRateMapper;

    @Override
    @DataSource(DataSourceType.SLAVE)
    public Integer getRandomRateByCoinId(Integer coinId) {
        Integer randomRate = diceRandomRateMapper.getRandomRateByCoinId(coinId);
        if (randomRate == null) {
            return DEFAULT_RANDOM_RATE;
        }
        return randomRate;
    }

}
