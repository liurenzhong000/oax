package com.oax.scheduled;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oax.OaxApiApplicationTest;

import lombok.extern.slf4j.Slf4j;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/7/4
 * Time: 15:50
 */
@Slf4j
@Component
public class CoinSumScheduledTest extends OaxApiApplicationTest {

    @Autowired
    private CoinSumScheduled coinSumScheduled;

    @Test
    public void countCoinSum() {

        coinSumScheduled.countCoinSum();

    }

//    @Autowired
//    private MyTokenScheduled myTokenScheduled;
//
//
//    @Test
//    public void name() {
//
//        myTokenScheduled.updateTickersBatchTask();
//    }
}