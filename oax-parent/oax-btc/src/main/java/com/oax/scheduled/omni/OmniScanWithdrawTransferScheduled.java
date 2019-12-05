package com.oax.scheduled.omni;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.oax.common.enums.WithdrawStatusEnum;
import com.oax.entity.front.Coin;
import com.oax.entity.front.Withdraw;
import com.oax.service.CoinService;
import com.oax.service.WithdrawService;
import com.oax.utils.UsdtRPCApiUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/8/13
 * Time: 15:15
 * 扫描转出记录,确认
 */
@Component
@Slf4j
public class OmniScanWithdrawTransferScheduled {


    @Autowired
    private WithdrawService withdrawService;

    @Autowired
    private CoinService coinService;

    @Autowired
    private UsdtRPCApiUtils usdtRPCApiUtils;


    /**
     * 每10分钟
     */
    @Scheduled(cron="0 0/10 * * * ?")
    @Async
    public void scanWithdrawTransfer(){

        log.error("------------Omni-执行scanWithdrawTransfer--------------");

        List<Coin> usdtTokenCoin = coinService.selectUsdtCoin();

        for (Coin tokenCoin : usdtTokenCoin) {
            List<Withdraw> usdtWithdrawList = withdrawService.selectByCoinIdAndStatus(tokenCoin.getId(),WithdrawStatusEnum.IN_TXPOOL_STATUS.getStatus());

            for (Withdraw withdraw : usdtWithdrawList) {

                try {
                    JSONObject transaction = usdtRPCApiUtils.getTransaction(withdraw.getTxId());
                    if (!transaction.getBoolean("valid")) {
                        withdraw.setUpdateTime(null);
                        withdraw.setStatus(WithdrawStatusEnum.FALL_STATUS.getStatus());
                        int update = withdrawService.updateByFall(withdraw);
                        if (update<=0){
                            log.error("updateByFall执行失败:"+JSON.toJSONString(withdraw));
                        }
                        continue;
                    }
                    int propertyidResult = transaction.getIntValue("propertyid");
                    if (propertyidResult != tokenCoin.getPropertyid()) {
                        continue;
                    }

                    BigDecimal fee = transaction.getBigDecimal("fee");

                    withdraw.setTxFee(fee.abs());
                    withdraw.setStatus(WithdrawStatusEnum.CONFIRM_STATUS.getStatus());
                    withdraw.setConfirmTime(new Date());
                    withdrawService.update(withdraw);
                    log.info("----更改转出记录状态----:::{}",JSON.toJSONString(withdraw));

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }


        log.error("-------------Omni-执行scanWithdrawTransfer结束------------");
    }


}
