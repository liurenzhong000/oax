package com.oax.service;

import java.math.BigDecimal;
import java.util.List;

import com.oax.entity.front.BenchMarkShareBonus;
import com.oax.entity.front.UserCoin;

public interface BenchMarkShareBonusService {
    List<BenchMarkShareBonus> getBenchMarkShareBonusList(String beginTime, String endTime);

    List<UserCoin> getUserCoinX(Integer banlance);

    BigDecimal getCirculationTotal();

    void addBenchMarkShareBonus(UserCoin userCoin, List<BenchMarkShareBonus> list, BigDecimal totalQty,BigDecimal rate,Integer type);

    List<BenchMarkShareBonus> getTradeList(String beginTime,String endTime,Integer banlance);

    void addBenchMarkShareBonusMon(BenchMarkShareBonus benchMarkShareBonus, BigDecimal totalUSDT, BigDecimal totalBTC, BigDecimal rate);

    void addShareBonusRedis();

    boolean batchSaveTradeRedman(List<BenchMarkShareBonus> benchMarkShareBonusList);
}
