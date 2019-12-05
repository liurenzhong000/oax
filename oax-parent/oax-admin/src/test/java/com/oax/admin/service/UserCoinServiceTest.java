package com.oax.admin.service;

import java.math.BigDecimal;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oax.OaxApiApplicationTest;
import com.oax.entity.front.UserCoin;

import lombok.extern.slf4j.Slf4j;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/7/4
 * Time: 14:56
 */
@Slf4j
@Component
public class UserCoinServiceTest extends OaxApiApplicationTest {


    @Autowired
    private UserCoinService userCoinService;
    @Test
    public void countAllBanlanceByCoinId() {

        BigDecimal bigDecimal = userCoinService.countAllBanlanceByCoinId(1);
        log.info("balance::{}",bigDecimal);
    }

    @Test
    public void test() {

        UserCoin userCoin = userCoinService.selectByUserIdAndCoinId(100730, 3);

        assert userCoin==null;
    }
}