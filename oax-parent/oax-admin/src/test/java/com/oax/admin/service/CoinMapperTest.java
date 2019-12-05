package com.oax.admin.service;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oax.OaxApiApplicationTest;
import com.oax.common.enums.CoinTypeEnum;
import com.oax.entity.admin.vo.CoinOutQtyCountVo;
import com.oax.entity.admin.vo.MarketCoinVo;
import com.oax.entity.admin.vo.SimpleCoin;
import com.oax.entity.front.Coin;
import com.oax.entity.front.CoinWithBLOBs;
import com.oax.mapper.front.CoinMapper;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/7/11
 * Time: 15:25
 */
@Component
public class CoinMapperTest extends OaxApiApplicationTest {

    @Autowired
    private CoinMapper coinMapper;
    @Test
    public void selectSimpleCoinByParam() {
        List<SimpleCoin> simpleCoins = coinMapper.selectSimpleCoinByParam(null, null, null);
        assert simpleCoins.size() >0;
    }

    @Test
    public void selectNameAll() {

        List<MarketCoinVo> marketCoinVos = coinMapper.selectNameAll();

    }

    @Test
    public void insert() {

        CoinWithBLOBs coinWithBLOBs = coinMapper.selectByPrimaryKey(1);

        coinWithBLOBs.setId(null);

        coinMapper.insertSelective(coinWithBLOBs);


        assert coinWithBLOBs.getId() > 0;
    }

    @Test
    public void selectCoinOutQtyCountVoByPage() {

        List<CoinOutQtyCountVo> coinOutQtyCountVoList = coinMapper.selectCoinOutQtyCountVoByPage(10);

        assert coinOutQtyCountVoList.size() > 0;
    }

    @Test
    public void selectByNameAndWithOutId() {
    }

    @Test
    public void selectByType() {

        List<Coin> coinList = coinMapper.selectByType(CoinTypeEnum.BTC.getType());

        assert coinList.size() > 0;
    }

    @Test
    public void selectAll() {


        List<Coin> coinList = coinMapper.selectAll();

        assert coinList.size() > 0;

    }

    @Test
    public void selectByContractAddress() {

        List<Coin> coinList = coinMapper.selectByContractAddress("0x6E453c010A606f5D336060f81DE7fe2b15c6cf4C");

        assert coinList.size() > 0;
    }

    @Test
    public void updateAllEHTAndTokenGasPrice() {
    }

    @Test
    public void selectByShortName() {
    }

    @Test
    public void getCoinByMarketId() {
    }
}