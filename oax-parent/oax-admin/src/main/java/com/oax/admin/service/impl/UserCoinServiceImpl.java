package com.oax.admin.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.oax.admin.service.UserCoinDetailService;
import com.oax.common.AssertHelper;
import com.oax.entity.enums.UserCoinDetailType;
import com.oax.entity.front.CoinWithBLOBs;
import com.oax.entity.front.Member;
import com.oax.entity.front.UserCoinInfo;
import com.oax.entity.front.vo.MemberCoinVo;
import com.oax.mapper.front.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oax.admin.service.UserCoinService;
import com.oax.entity.front.UserCoin;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/6/27
 * Time: 14:40
 * 用户币种 service
 */
@Service
public class UserCoinServiceImpl implements UserCoinService {
    @Autowired
    private UserCoinMapper userCoinMapper;

    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private CoinMapper coinMapper;

    @Autowired
    private TradeMapper tradeMapper;

    @Autowired
    private MarketCategoryMapper marketCategoryMapper;

    @Autowired
    private UserCoinDetailService userCoinDetailService;

    @Override
    public UserCoin selectByUserIdAndCoinId(Integer userId, Integer coinId) {
        return userCoinMapper.selectByUserIdAndCoinId(userId, coinId);
    }

    @Override
    public int update(UserCoin userCoin) {
        return userCoinMapper.updateByPrimaryKeySelective(userCoin);
    }

    @Override
    public BigDecimal countAllBanlanceByCoinId(Integer coinId) {
        return userCoinMapper.countAllBanlanceByCoinId(coinId);
    }

    @Override
    public UserCoin selectAndInsert(Integer userId, Integer coinId) {
        UserCoin userCoin = userCoinMapper.selectByUserIdAndCoinId(userId, coinId);
        if (userCoin != null) {
            return userCoin;
        }
        UserCoin entity = UserCoin.newInstance(userId, coinId);
        userCoinMapper.insertSelective(entity);
        entity = userCoinMapper.selectByUserIdAndCoinId(userId, coinId);
        return entity;
    }


    @Override
    public List<UserCoinInfo> listUserCoinByUserId(Integer userId, String coinName) {
        //ETH/USDT 的比例
        BigDecimal ethUsdtRatio = tradeMapper.getRatioByLeftIdAndRightId(1, 10);
        if (ethUsdtRatio == null) {
            ethUsdtRatio = BigDecimal.ZERO;
        }
        //BTC/USDT 的比例
        BigDecimal btcUsdtRatio = tradeMapper.getRatioByLeftIdAndRightId(2, 10);
        if (btcUsdtRatio == null) {
            btcUsdtRatio = BigDecimal.ZERO;
        }
        //USDT,BTC,ETH的人民币价格 T
        BigDecimal usdtCnyPrice = marketCategoryMapper.getUsdtCnyPrice();
        if (usdtCnyPrice == null) {
            usdtCnyPrice = BigDecimal.ZERO;
        }
        BigDecimal ethCnyPrice = ethUsdtRatio.multiply(usdtCnyPrice);
        BigDecimal btcCnyPrice = btcUsdtRatio.multiply(usdtCnyPrice);
        //获取用户的资金列表
        Member member = memberMapper.selectByPrimaryKey(userId);
        List<UserCoin> userCoins = userCoinMapper.selectUserCoinByUserIdNoZero(member.getId(), coinName);
        List<UserCoinInfo> userCoinInfoList = new ArrayList<>();
        BigDecimal allCnyValue = BigDecimal.ZERO;
        for (UserCoin userCoin : userCoins) {
            Integer coinId = userCoin.getCoinId();
            BigDecimal qty = userCoin.getBanlance().add(userCoin.getFreezingBanlance());
            //获取该币的市场
            CoinWithBLOBs coinWithBLOBs = coinMapper.selectById(coinId);
            Integer coinType = coinWithBLOBs.getType();//币种类型：1 ETH 2 BTC 3 EHT_TOKEN 4 USDT
            BigDecimal cnyValue = BigDecimal.ZERO;
            if (coinType == 1) {
                //获取该币在ETH交易区的最新价格,即能转换成多少个eth
                BigDecimal coinEthRatio = tradeMapper.getRatioByLeftIdAndRightId(coinId, 1);
                if (coinEthRatio == null) {
                    coinEthRatio = BigDecimal.ZERO;
                }
                cnyValue = qty.multiply(coinEthRatio).multiply(ethCnyPrice);
            } else if (coinType == 2) {
                //获取该币在BTC交易区的最新价格
                BigDecimal coinBtcRatio = tradeMapper.getRatioByLeftIdAndRightId(coinId, 2);
                if (coinBtcRatio == null) {
                    coinBtcRatio = BigDecimal.ZERO;
                }
                cnyValue = qty.multiply(coinBtcRatio).multiply(btcCnyPrice);
            } else if (coinType == 4) {//为usdt
                cnyValue = qty.multiply(usdtCnyPrice);
            } else {
                //获取该币在USDT交易区的最新价格
                BigDecimal coinUsdtRatio = tradeMapper.getRatioByLeftIdAndRightId(coinId, 10);
                if (coinUsdtRatio == null) {
                    coinUsdtRatio = BigDecimal.ZERO;
                }
                cnyValue = qty.multiply(coinUsdtRatio).multiply(usdtCnyPrice);
            }

            //完善userCoinInfoList
            UserCoinInfo userCoinInfo = new UserCoinInfo();

            userCoinInfo.setAllowRecharge(coinWithBLOBs.getAllowRecharge());
            userCoinInfo.setAllowWithdraw(coinWithBLOBs.getAllowWithdraw());
            userCoinInfo.setBanlance(userCoin.getBanlance());
            userCoinInfo.setFreezingBanlance(userCoin.getFreezingBanlance());
            userCoinInfo.setFullName(coinWithBLOBs.getFullName());
            userCoinInfo.setImage(coinWithBLOBs.getImage());
            userCoinInfo.setMaxOutQty(coinWithBLOBs.getMaxOutQty());
            userCoinInfo.setMinOutQty(coinWithBLOBs.getMinOutQty());
            userCoinInfo.setShortName(coinWithBLOBs.getShortName());
            userCoinInfo.setType(coinWithBLOBs.getType());
            userCoinInfo.setUserId(userCoin.getUserId());
            userCoinInfo.setWithdrawFee(coinWithBLOBs.getWithdrawFee());
//			BeanHepler.copySrcToDest(coinWithBLOBs, userCoinInfo);
//			BeanHepler.copySrcToDest(userCoin, userCoinInfo);


            userCoinInfo.setId(coinId);
            userCoinInfo.setBtcPrice(cnyValue.divide(btcCnyPrice, 10, BigDecimal.ROUND_HALF_UP));
            userCoinInfo.setCnyPrice(cnyValue);
            userCoinInfo.setTotalBanlance(qty);
            userCoinInfo.setTradeList(null);
            userCoinInfo.setUsdtPrice(cnyValue.divide(usdtCnyPrice, 10, BigDecimal.ROUND_HALF_UP));
            userCoinInfo.setUseredWithdrawal(null);
            userCoinInfo.setWithdrawalAmount(null);

            allCnyValue = allCnyValue.add(cnyValue);
            userCoinInfoList.add(userCoinInfo);
        }
        return userCoinInfoList;
    }

    @Override
    public void addBalanceWithType(Integer userId, Integer coinId, BigDecimal qty, String targetId, UserCoinDetailType type) {
        UserCoin betUserCoin = selectAndInsert(userId, coinId);
        int addSucc = userCoinMapper.addBanlance(qty, coinId, userId, betUserCoin.getVersion());
        AssertHelper.isTrue(addSucc>=1, "系统处理异常，余额未发生变化，请稍后再试");
        userCoinDetailService.addUserCoinDetail(betUserCoin, targetId, type);
    }

    @Override
    public List<MemberCoinVo> listBCBMemberCoinVos() {
        return userCoinMapper.listBCBMemberCoinVos();
    }
}
