package com.oax.admin.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.oax.admin.exception.MyException;
import com.oax.admin.service.CoinService;
import com.oax.admin.walletclient.BtcApiClient;
import com.oax.admin.walletclient.EthApiClient;
import com.oax.common.ResultResponse;
import com.oax.common.enums.CoinTypeEnum;
import com.oax.entity.admin.param.SimpleCoinParam;
import com.oax.entity.admin.vo.CoinOutQtyCountVo;
import com.oax.entity.admin.vo.MarketCoinVo;
import com.oax.entity.admin.vo.SimpleCoin;
import com.oax.entity.front.Coin;
import com.oax.entity.front.CoinWithBLOBs;
import com.oax.mapper.front.CoinMapper;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/6/4
 * Time: 14:55
 */
@Service
public class CoinServiceImpl implements CoinService {

    @Autowired
    private CoinMapper coinMapper;

    @Autowired
    private EthApiClient ethApiClient;

    @Autowired
    private BtcApiClient btcApiClient;



    @Override
    public PageInfo<SimpleCoin> selectSimpleCoinByParam(SimpleCoinParam simpleCoinParam) {


        PageHelper.startPage(simpleCoinParam.getPageNum(), simpleCoinParam.getPageSize());
        List<SimpleCoin> simpleCoins = coinMapper.selectSimpleCoinByParam(simpleCoinParam.getStartTime(),
                simpleCoinParam.getEndTime(),
                simpleCoinParam.getCoinId());

        return new PageInfo<>(simpleCoins);
    }

    @Override
    public CoinWithBLOBs selectById(int coinId) {
        return coinMapper.selectById(coinId);
    }

    @Override
    public List<MarketCoinVo> selectNameAll() {
        return coinMapper.selectNameAll();
    }

    @Override
    public int updateCionBySelect(CoinWithBLOBs coinWithBLOBs) {
        return coinMapper.updateByPrimaryKeySelective(coinWithBLOBs);
    }

    @Override
    public int addCionBySelect(CoinWithBLOBs coinWithBLOBs) {
        return coinMapper.insertSelective(coinWithBLOBs);
    }

    @Override
    public PageInfo<CoinOutQtyCountVo> selectCoinOutQtyCountVoByPage(Integer coinId, int pageNo, int pageSize) {

        PageHelper.startPage(pageNo, pageSize);

        List<CoinOutQtyCountVo> coinOutQtyCountVoList = coinMapper.selectCoinOutQtyCountVoByPage(coinId);

        for (CoinOutQtyCountVo coinOutQtyCountVo : coinOutQtyCountVoList) {

            Integer type = coinOutQtyCountVo.getType();
            ResultResponse resultResponse = null;
            //TODO 根据币种类型不同 获取钱包余额
            if (type == CoinTypeEnum.ETH.getType()) {
                resultResponse = ethApiClient.getEthBalance(coinOutQtyCountVo.getMainAddress());
            } else if (type == CoinTypeEnum.ETH_TOKEN.getType()) {
                resultResponse = ethApiClient.getTokenBalance(coinOutQtyCountVo.getContractAddress(), coinOutQtyCountVo.getMainAddress());
            } else if (type == CoinTypeEnum.BTC.getType()) {
                resultResponse = btcApiClient.getbalanceByMainAddress();
                coinOutQtyCountVo.setMainAddress("");
            } else if (type == CoinTypeEnum.USDT.getType()) {
                resultResponse = btcApiClient.getOmniAndBtcBalance(coinOutQtyCountVo.getMainAddress(),coinOutQtyCountVo.getPropertyid());
            }

            if (resultResponse.isSuccess()) {
                JSONObject data = JSON.parseObject(JSON.toJSONString(resultResponse.getData()));
                String balance = data.getString("balance");
                coinOutQtyCountVo.setWalletBalance(balance);
            } else {
                throw new MyException(resultResponse.getMsg());
            }
        }

        return new PageInfo<>(coinOutQtyCountVoList);
    }

    @Override
    public List<Coin> selectByNameAndWithOutId(String shortName, String fullName, String cnName, Integer withOutId) {
        return coinMapper.selectByNameAndWithOutId(shortName, fullName, cnName, withOutId);
    }

    @Override
    public List<Coin> selectByType(int type) {
        return coinMapper.selectByType(type);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertParentCoin(CoinWithBLOBs coinWithBLOBs) {
        int insert = this.addCionBySelect(coinWithBLOBs);
        coinWithBLOBs.setParentId(coinWithBLOBs.getId());
        this.updateCionBySelect(coinWithBLOBs);
        return insert;
    }

    @Override
    public Coin selectByShortName(String coinShortName) {
        return coinMapper.selectByShortName(coinShortName);
    }

    @Override
    public List<Coin> selectByPropertyidWithOutCheckCoinId(Integer propertyid, Integer coinId) {
        return coinMapper.selectByPropertyidWithOutCheckCoinId(propertyid,coinId);
    }
}
