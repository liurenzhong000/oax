package com.oax.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oax.async.MarketInfoUtils;
import com.oax.common.RedisUtil;
import com.oax.common.enums.CoinEnum;
import com.oax.common.enums.RedisKeyEnum;
import com.oax.entity.front.BenchMarkShareBonus;
import com.oax.entity.front.ShareBonusInfo;
import com.oax.entity.front.UserCoin;
import com.oax.mapper.front.BenchMarkShareBonusMapper;
import com.oax.mapper.front.TradeFeedBackLogMapper;
import com.oax.mapper.front.TradeSnapshotMapper;
import com.oax.service.BenchMarkShareBonusService;

@Service
public class BenchMarkShareBonusServiceImpl implements BenchMarkShareBonusService {
    @Autowired
    private BenchMarkShareBonusMapper mapper;
    @Autowired
    private TradeSnapshotMapper tradeSnapshotMapper;
    @Autowired
    private TradeFeedBackLogMapper tradeFeedBackLogMapper;
    @Autowired
    private MarketInfoUtils marketInfoUtils;
    @Autowired
    private RedisUtil redisUtil;

    @Override
    public List<BenchMarkShareBonus> getBenchMarkShareBonusList(String beginTime, String endTime) {
        return mapper.getBenchMarkShareBonusList(beginTime,endTime);
    }

    @Override
    public List<UserCoin> getUserCoinX(Integer banlance) {
        //设置交易挖矿门槛
        if (banlance!=null&&banlance>0){
            return mapper.getUserCoinXThreshold(banlance);
        }
        //不设置交易挖矿门槛
        return mapper.getUserCoinXNonThreshold();
    }

    @Override
    public BigDecimal getCirculationTotal() {
        return tradeSnapshotMapper.getCirculationTotal();
    }

    @Transactional
    @Override
    public void addBenchMarkShareBonus(UserCoin userCoin, List<BenchMarkShareBonus> list, BigDecimal totalQty,BigDecimal rate,Integer type) {
        BenchMarkShareBonus model = new BenchMarkShareBonus();
        model.setCreateTime(new Date());
        model.setUserId(userCoin.getUserId());
        model.setType(type);
        for (BenchMarkShareBonus bms : list) {
            BigDecimal qty = bms.getQty().multiply(rate).multiply(userCoin.getBanlance()).divide(totalQty,8 , BigDecimal.ROUND_FLOOR);
            model.setCoinId(bms.getCoinId());
            model.setQty(qty);
            Integer count = mapper.insert(model);
            if (count!=null&&count>0){
                //修改用户该币资产数据
                UserCoin uc = new UserCoin();
                uc.setUserId(userCoin.getUserId());
                //X币id=3
                uc.setCoinId(bms.getCoinId());
                uc.setBanlance(qty);
                Integer row = tradeFeedBackLogMapper.selectUserCoin(uc);
                if (row!=null&&row>0){
                    //如果该用户有该币资产  修改资产数据即可
                    tradeFeedBackLogMapper.updateUserCoinByUserIdAndCoinId(uc);
                }else{
                    //如果该用户没有该币资产 则添加用户该币资产数据
                    uc.setFreezingBanlance(BigDecimal.ZERO);
                    uc.setCreateTime(new Date());
                    uc.setUpdateTime(new Date());
                    //插入该币资产
                    tradeFeedBackLogMapper.insertUserCoin(uc);
                }
            }
        }
    }

    @Override
    public List<BenchMarkShareBonus> getTradeList(String beginTime,String endTime,Integer banlance) {
        //设置交易挖矿门槛
        List<BenchMarkShareBonus> list = null;
        if (banlance!=null&&banlance>0){
            list = mapper.getTradeListXThreshold(beginTime,endTime,banlance);
            return mergeTradeListUSDT(list);
        }
        //不设置交易挖矿门槛
        list = mapper.getTradeListXNonThreshold(beginTime,endTime);
        return mergeTradeListUSDT(list);
    }

    @Transactional
    @Override
    public void addBenchMarkShareBonusMon(BenchMarkShareBonus benchMarkShareBonus, BigDecimal totalUSDT, BigDecimal totalBTC, BigDecimal rate) {
        BigDecimal qty = totalBTC.multiply(rate).multiply(benchMarkShareBonus.getQty()).divide(totalUSDT, 8, BigDecimal.ROUND_FLOOR);
        BenchMarkShareBonus model = new BenchMarkShareBonus();
        model.setCreateTime(new Date());
        model.setUserId(benchMarkShareBonus.getUserId());
        model.setType(3);
        //返回BTC
        model.setCoinId(CoinEnum.TOKEN_BTC.getValue());
        model.setQty(qty);
        Integer count = mapper.insert(model);
        if (count!=null&&count>0){
            //修改用户BTC币资产数据
            UserCoin uc = new UserCoin();
            uc.setUserId(benchMarkShareBonus.getUserId());
            //BTC币id=2
            uc.setCoinId(CoinEnum.TOKEN_BTC.getValue());
            uc.setBanlance(qty);
            Integer row = tradeFeedBackLogMapper.selectUserCoin(uc);
            if (row!=null&&row>0){
                //如果该用户有X币资产  修改资产数据即可
                tradeFeedBackLogMapper.updateUserCoinByUserIdAndCoinId(uc);
            }else{
                //如果该用户没有X币资产 则添加用户X币资产数据
                uc.setFreezingBanlance(BigDecimal.ZERO);
                uc.setCreateTime(new Date());
                uc.setUpdateTime(new Date());
                //插入x币资产
                tradeFeedBackLogMapper.insertUserCoin(uc);
            }
        }

    }

    @Override
    public void addShareBonusRedis() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        Calendar calendar=Calendar.getInstance(Locale.CHINA);
        String toDay = sdf.format(calendar.getTime());
        calendar.add(Calendar.DATE, 1);
        String nextDay = sdf.format(calendar.getTime());
        Calendar calendar1 = Calendar.getInstance();
        calendar1.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
        String currentFriday = sdf.format(calendar1.getTime());
        calendar1.add(Calendar.DATE, -7);
        String lastFriday = sdf.format(calendar1.getTime());
        Calendar calendar2 = Calendar.getInstance();
        calendar2.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        String currentMonday = sdf.format(calendar2.getTime());
        calendar2.add(Calendar.DATE, 7);
        String nextMonday = sdf.format(calendar2.getTime());
        List<ShareBonusInfo> list = mapper.getShareBonusInfoList(toDay, nextDay,lastFriday,currentFriday,currentMonday,nextMonday);
        BigDecimal totalX = tradeSnapshotMapper.getCirculationTotal();
        for (ShareBonusInfo shareBonusInfo : list) {
            BigDecimal avgQty = shareBonusInfo.getToDayQty().multiply(new BigDecimal(1000000)).divide(totalX,8,BigDecimal.ROUND_FLOOR);
            shareBonusInfo.setAvgQty(avgQty);
        }
        redisUtil.setList(RedisKeyEnum.SHAREBONUS_LIST.getKey(), list,-1);
    }

    @Override
    public boolean batchSaveTradeRedman(List<BenchMarkShareBonus> benchMarkShareBonusList) {
        Integer count = mapper.batchSaveTradeRedman(benchMarkShareBonusList);
        if (count!=null&&count>0){
            return true;
        }
        return false;
    }

    private List<BenchMarkShareBonus> mergeTradeListUSDT(List<BenchMarkShareBonus> list){
        if (list==null||list.size()==0){
            return list;
        }
        //存 userId--usdt数量的map
        Map<Integer,BigDecimal> map = new HashMap<>();
        for (BenchMarkShareBonus benchMarkShareBonus : list) {
            Integer userId = benchMarkShareBonus.getUserId();
            //USDT的id=21 上线时改成21 测试库是10
            if (benchMarkShareBonus.getCoinId()==CoinEnum.TOKEN_USDT.getValue()){
                //如果交易额是USDT
                if (map.containsKey(userId)){
                    map.put(userId, map.get(userId).add(benchMarkShareBonus.getQty()));
                }else{
                    map.put(userId, benchMarkShareBonus.getQty());
                }
            }else{
                //如果交易额不是USDT,需要转换成USDT
                BigDecimal price = marketInfoUtils.getTranformCoin(benchMarkShareBonus.getCoinId(), CoinEnum.TOKEN_USDT.getValue(), null);
                if (map.containsKey(userId)){
                    map.put(userId, map.get(userId).add(benchMarkShareBonus.getQty().multiply(price).setScale(8, BigDecimal.ROUND_FLOOR)));
                }else{
                    map.put(userId, benchMarkShareBonus.getQty().multiply(price).setScale(8, BigDecimal.ROUND_FLOOR));
                }
            }
        }
        List<BenchMarkShareBonus> newList = new ArrayList<>();
        for (Map.Entry<Integer,BigDecimal> entry:map.entrySet()) {
            BenchMarkShareBonus model = new BenchMarkShareBonus();
            model.setUserId(entry.getKey());
            model.setQty(entry.getValue());
            newList.add(model);
        }
        Collections.sort(newList, new Comparator<BenchMarkShareBonus>() {
            @Override
            public int compare(BenchMarkShareBonus o1, BenchMarkShareBonus o2) {
                return o2.getQty().compareTo(o1.getQty());
            }
        });
        if (newList.size()>100){
            newList = newList.subList(0, 100);
        }
        return newList;
    }
}
