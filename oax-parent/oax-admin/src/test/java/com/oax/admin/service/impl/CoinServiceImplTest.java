package com.oax.admin.service.impl;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.pagehelper.PageInfo;
import com.oax.OaxApiApplicationTest;
import com.oax.admin.service.CoinService;
import com.oax.entity.admin.param.SimpleCoinParam;
import com.oax.entity.admin.vo.SimpleCoin;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/6/4
 * Time: 16:00
 */
@Component
public class CoinServiceImplTest extends OaxApiApplicationTest {


    @Autowired
    private CoinService coinService;

    @Test
    public void selectSimpleCoinByPage() {

        SimpleCoinParam simpleCoinParam = new SimpleCoinParam();
        simpleCoinParam.setCoinId(2);
//        simpleCoinParam.setEndTime(new Date());
//        simpleCoinParam.setStartTime(new Date(System.currentTimeMillis() - 100000000));
        simpleCoinParam.setPageNum(1);
        simpleCoinParam.setPageSize(10);
        PageInfo<SimpleCoin> simpleCoinPageInfo = coinService.selectSimpleCoinByParam(simpleCoinParam);


        assert simpleCoinPageInfo != null;
    }
}