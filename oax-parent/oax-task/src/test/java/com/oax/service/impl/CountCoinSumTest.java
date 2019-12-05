package com.oax.service.impl;

import com.oax.OaxApiApplicationTest;
import com.oax.scheduled.CoinSumScheduled;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Auther: hyp
 * @Date: 2019/1/25 15:31
 * @Description:
 */
@Component
public class CountCoinSumTest extends OaxApiApplicationTest {

    @Autowired
    private CoinSumScheduled coinSumScheduled;

    @Test
    public void test(){
        coinSumScheduled.countCoinSum();
    }
}
