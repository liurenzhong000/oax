package com.oax.service.impl;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oax.async.MarketInfoUtils;
import com.oax.common.RedisUtil;
import com.oax.common.enums.CoinEnum;
import com.oax.common.enums.RedisKeyEnum;
import com.oax.common.enums.SysConfigEnum;
import com.oax.entity.front.MarketInfo;
import com.oax.entity.front.SysConfig;
import com.oax.entity.front.Trade;
import com.oax.entity.front.TradeSnapshot;
import com.oax.mapper.front.SysConfigMapper;
import com.oax.mapper.front.TradeSnapshotMapper;
import com.oax.service.TradeSnapshotService;

@Service
public class TradeSnapshotServiceImpl implements TradeSnapshotService {
    @Autowired
    private TradeSnapshotMapper mapper;
    @Autowired
    private MarketInfoUtils util;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private SysConfigMapper sysConfigMapper;
    @Override
    public List<Integer> getMarketIds() {
        return mapper.getMarketIds();
    }

    @Override
    public boolean addTradeSnapshot(String beginTime, String endTime, Integer marketId) throws ParseException {
        TradeSnapshot tradeSnapshot = new TradeSnapshot();
        List<Trade> list = mapper.getTradeList(beginTime,endTime,marketId);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (list==null||list.size()==0){
            tradeSnapshot.setTradeQty(BigDecimal.ZERO);
            tradeSnapshot.setMarketId(marketId);
            tradeSnapshot.setFeeToETH(BigDecimal.ZERO);
            tradeSnapshot.setFeeToX(BigDecimal.ZERO);
            tradeSnapshot.setBeginTime(sdf.parse(beginTime));
            tradeSnapshot.setEndTime(sdf.parse(endTime));
            tradeSnapshot.setCreateTime(new Date());
            tradeSnapshot.setAvgFeeBackX(BigDecimal.ZERO);
            tradeSnapshot.setStatus(0);
            tradeSnapshot.setFeeToLeftCoin(BigDecimal.ZERO);
            tradeSnapshot.setFeeToRightCoin(BigDecimal.ZERO);
        }else{
            BigDecimal qty = new BigDecimal(0);
            //先判断右币是eth btc usdt
            BigDecimal leftCoinFee = new BigDecimal(0);
            BigDecimal rightCoinFee = new BigDecimal(0);
            for (Trade trade : list) {
                qty = qty.add(trade.getQty());
                // 如果是左币的手续费
                if (trade.getFeeCoinId()==trade.getLeftCoinId()){
                    leftCoinFee = leftCoinFee.add(trade.getFee());
                    //如果是右币的手续费
                }else{
                    rightCoinFee = rightCoinFee.add(trade.getFee());
                }
            }
            //计算出市场总共的折合成右币的所有的手续费 lastPrice表示一个左币 = 多少个右币
            BigDecimal lastPrice = util.getTranformCoin(list.get(0).getLeftCoinId(), list.get(0).getRightCoinId(), marketId);
            BigDecimal leftCoinToRightCoinFee = leftCoinFee.multiply(lastPrice);
            //手续费折合成右币的费用
            BigDecimal rightCoinTotalFee = rightCoinFee.add(leftCoinToRightCoinFee);
            // rightToleftLastPrice表示一个右币 = 多少个左币
            BigDecimal rightToleftLastPrice = BigDecimal.ONE.divide(lastPrice,8,BigDecimal.ROUND_FLOOR);
            //手续费折合成左币的费用
            BigDecimal leftCoinTotalFee = leftCoinFee.add(rightCoinFee.multiply(rightToleftLastPrice));
            BigDecimal feeToETH = null;
            BigDecimal feeToX = null;
            //  1.如果右币是ETH  2.如果不是ETH(比如USDT 或者BTC或者其他)
            if (list.get(0).getRightCoinId()==CoinEnum.TOKEN_ETH.getValue()){
                //ETH数量
                feeToETH = rightCoinTotalFee;
                // 将ETH折合成X  表示一个ETH值多少个X
                feeToX = feeToETH.divide(this.getAvgX(beginTime, endTime),8,BigDecimal.ROUND_FLOOR);
            }else{
                //如果右币不过ETH，需要再转一次  必定存在 ETH/?的市场  求出 一个？币值多少个ETH   lastPriceToETH
                BigDecimal lastPriceToETH = util.getTranformCoin(list.get(0).getRightCoinId(), CoinEnum.TOKEN_ETH.getValue(), null);
                feeToETH = rightCoinTotalFee.multiply(lastPriceToETH);
                feeToX = feeToETH.divide(this.getAvgX(beginTime, endTime),8,BigDecimal.ROUND_FLOOR);
            }
            tradeSnapshot.setTradeQty(qty);
            tradeSnapshot.setMarketId(marketId);
            tradeSnapshot.setFeeToETH(feeToETH);
            tradeSnapshot.setFeeToX(feeToX);
            tradeSnapshot.setBeginTime(sdf.parse(beginTime));
            tradeSnapshot.setEndTime(sdf.parse(endTime));
            tradeSnapshot.setCreateTime(new Date());
            tradeSnapshot.setAvgFeeBackX(feeToX.multiply(new BigDecimal(0.8)).divide(qty,8,BigDecimal.ROUND_FLOOR));
            tradeSnapshot.setStatus(0);
            tradeSnapshot.setFeeToRightCoin(rightCoinTotalFee);
            tradeSnapshot.setFeeToLeftCoin(leftCoinTotalFee);
        }
        //插入市场手续费的小时快照数据
        Integer count = mapper.insert(tradeSnapshot);
        if (count==null||count==0){
            System.out.println("插入oax.trade_snapshot快照数据不成功");
            return false;
        }
        return true;
    }

    @Override
    public boolean isBack(Integer userId) {
        // 添加先从缓存中取的操作
        List<SysConfig> list = redisUtil.getList(RedisKeyEnum.SYSCONFIG_LIST.getKey(), SysConfig.class);
        SysConfig sysConfig = getSysConfigFromList(list,SysConfigEnum.TRADE_FEEDBACK.getName());
        if (sysConfig==null) {
            list = sysConfigMapper.selectAll();
            sysConfig = getSysConfigFromList(list, SysConfigEnum.TRADE_FEEDBACK.getName());
            redisUtil.setList(RedisKeyEnum.SYSCONFIG_LIST.getKey(), list,-1);
        }
        //获取交易回馈的门槛 标准
        BigDecimal threshold = new BigDecimal(sysConfig.getValue());
        BigDecimal banlance =  mapper.getBanlanceX(userId);
        if (banlance!=null&&banlance.compareTo(threshold)>=0){
            return true;
        }
        return false;
    }


    public BigDecimal getAvgX(String beginTime, String endTime){
        List<MarketInfo> marketInfoList = redisUtil.getList(RedisKeyEnum.MARKET_LIST.getKey(), MarketInfo.class);
        MarketInfo info = null;
        // 1表示ETH的id  3表示X币的id
        //找到X/ETH的交易对信息
        for (MarketInfo marketInfo : marketInfoList) {
            boolean flag1 = CoinEnum.TOKEN_X.getValue()==marketInfo.getCoinId()&&CoinEnum.TOKEN_ETH.getValue()==marketInfo.getMarketCoinId();
            boolean flag2 = CoinEnum.TOKEN_X.getValue()==marketInfo.getMarketCoinId()&&CoinEnum.TOKEN_ETH.getValue()==marketInfo.getCoinId();
            if (flag1||flag2){
                info = marketInfo;
                break;
            }
        }
        //数据库中查询该时段快照内的X币的均价
        BigDecimal avgX = mapper.getTradeListForAvg(beginTime,endTime, info.getMarketId());
        //如果X币均价为0  说明在该时段内X/ETH 市场没有任何一笔交易  这时需要取最新的X币的价格作为均价
        if (avgX.compareTo(BigDecimal.ZERO)==0){
            avgX = info.getLastTradePrice();
        }
        return avgX;
    }

    public SysConfig getSysConfigFromList(List<SysConfig> list,String name) {
        SysConfig sysConfig = null;
        if (list!=null && list.size()!=0) {
            for (SysConfig config : list) {
                if (name.equals(config.getName())) {
                    sysConfig = config;
                    break;
                }
            }
        }
        return sysConfig;
    }
}
