package com.oax.service;

import java.math.BigDecimal;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oax.OaxApiApplicationTest;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/7/3
 * Time: 15:30
 */
@Component
public class MarketCategoryServiceTest extends OaxApiApplicationTest {


    @Autowired
    private MarketCategoryService marketCategoryService;
    @Test
    public void updateMarketExchangePrice() {

        int i = marketCategoryService.updateMarketExchangePrice(
                BigDecimal.ONE,
                BigDecimal.TEN,
                BigDecimal.ONE,
                BigDecimal.TEN,BigDecimal.ONE);


    }
}