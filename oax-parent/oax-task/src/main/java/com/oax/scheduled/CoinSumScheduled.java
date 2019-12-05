package com.oax.scheduled;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.oax.common.json.JsonHelper;
import com.oax.entity.front.vo.PlatformTransferSumVo;
import com.oax.entity.front.vo.RechargeSumVo;
import com.oax.entity.front.vo.TradeSumVo;
import com.oax.entity.front.vo.WithdrawSumVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.oax.entity.front.Coin;
import com.oax.entity.front.PlatformTransfer;
import com.oax.entity.front.Recharge;
import com.oax.entity.front.SumCoin;
import com.oax.entity.front.Trade;
import com.oax.entity.front.Withdraw;
import com.oax.service.CoinService;
import com.oax.service.PlatformTransferService;
import com.oax.service.RechargeService;
import com.oax.service.SumCoinService;
import com.oax.service.TradeService;
import com.oax.service.WithdrawService;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/7/3
 * Time: 16:22
 *
 */
@Component
@Slf4j
public class CoinSumScheduled {

    @Autowired
    private WithdrawService withdrawService;

    @Autowired
    private RechargeService rechargeService;

    @Autowired
    private PlatformTransferService platformTransferService;

    @Autowired
    private CoinService coinService;

    @Autowired
    private SumCoinService sumCoinService;

    @Autowired
    private TradeService tradeService;

    /**
     * 每天凌晨两点执行
     */
    @Scheduled(cron="0 0 2 * * ?")
    public void countCoinSum(){
        Long start = System.currentTimeMillis();
        List<Coin> coinList = coinService.selectAll();
        Date endTime = new Date();
        Date startTime = DateUtils.addDays(endTime, -1);//一天前

        for (Coin coin : coinList) {
            Integer coinId = coin.getId();

            List<RechargeSumVo> rechargeSumVoList = rechargeService.selectSumVoByCoinIdAndTime(coinId, startTime, endTime);
            BigDecimal rechargeQty = rechargeSumVoList.stream()
                    .map(RechargeSumVo::getQty)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            List<WithdrawSumVo> withdrawSumVoList = withdrawService.selectSumVoOutByCoinIdAndTime(coinId, startTime, endTime);
            BigDecimal withdrawQty = withdrawSumVoList.stream()
                    .map(WithdrawSumVo::getQty)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal withdrawFee = withdrawSumVoList.stream()
                    .map(WithdrawSumVo::getFee)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            List<TradeSumVo> tradeSumVoList = tradeService.selectSumVoByCoinIdAndTime(coinId, startTime, endTime);
            BigDecimal tradeFee = tradeSumVoList.stream()
                    .map(TradeSumVo::getFee)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            List<PlatformTransferSumVo> platformTransferList = platformTransferService.selectSumVoByCoinIdAndTime(coinId, startTime, endTime);
            BigDecimal sumAllWithdrawTxFee = withdrawSumVoList.stream()
                    .map(WithdrawSumVo::getTxFee)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal platformCost = platformTransferList.stream()
                    .map(PlatformTransferSumVo::getTxFee)
                    .reduce(sumAllWithdrawTxFee, BigDecimal::add);

            SumCoin sumCoin = new SumCoin();
            sumCoin.setCoinId(coinId);
            sumCoin.setPlatformCost(platformCost);
            sumCoin.setRechargeQty(rechargeQty);
            sumCoin.setTradeFee(tradeFee);
            sumCoin.setWithdrawFee(withdrawFee);
            sumCoin.setWithdrawQty(withdrawQty);
            sumCoin.setSumDate(startTime);
            sumCoinService.insert(sumCoin);
            log.info(JsonHelper.writeValueAsString(sumCoin));
        }

        Long end = System.currentTimeMillis();
        log.info("CoinSumScheduled - 每日币种数据统计,执行时间", (end-start));
    }


}
