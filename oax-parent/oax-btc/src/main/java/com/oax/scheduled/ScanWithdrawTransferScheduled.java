package com.oax.scheduled;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.oax.common.enums.CoinTypeEnum;
import com.oax.common.enums.WithdrawStatusEnum;
import com.oax.entity.front.Coin;
import com.oax.entity.front.Withdraw;
import com.oax.service.CoinService;
import com.oax.service.WithdrawService;
import com.oax.utils.BtcRPCApiUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/6/28
 * Time: 16:47
 * 扫描所有 转出记录 (更改状态 且 存入 fee)
 */
@Slf4j
@Component
public class ScanWithdrawTransferScheduled {

    @Autowired
    private WithdrawService withdrawService;

    @Autowired
    private CoinService coinService;

    @Autowired
    private BtcRPCApiUtils btcRPCApiUtils;



    /**
     * 每10分钟
     */
    @Scheduled(cron="0 0/10 * * * ?")
    @Async
    public void scanWithdrawTransfer(){


        log.error("------------执行scanWithdrawTransfer--------------");

        List<Coin> coinList = coinService.selectByType(CoinTypeEnum.BTC.getType());
        Coin coin = coinList.get(0);
        List<Withdraw> btcWithdrawList = withdrawService.selectByCoinIdAndStatus(coin.getId(),WithdrawStatusEnum.IN_TXPOOL_STATUS.getStatus());

        for (Withdraw withdraw : btcWithdrawList) {

            try {
                JSONObject transaction = btcRPCApiUtils.getTransaction(withdraw.getTxId());

                BigDecimal fee = transaction.getBigDecimal("fee");
                withdraw.setTxFee(fee.abs());
                withdraw.setStatus(WithdrawStatusEnum.CONFIRM_STATUS.getStatus());
                withdraw.setConfirmTime(new Date());
                withdrawService.update(withdraw);
                log.info("----更改转出记录状态----:::{}",JSON.toJSONString(withdraw));
            } catch (Exception e) {
                log.error("更改转入状态失败",e);
            }
        }

        log.error("------------执行scanWithdrawTransfer结束--------------");


    }

}
