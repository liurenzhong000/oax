package com.oax.scheduled.omni;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.oax.common.enums.PlatformTransferTypeEnum;
import com.oax.entity.front.Coin;
import com.oax.entity.front.PlatformTransfer;
import com.oax.service.CoinService;
import com.oax.service.PlatformTransferService;
import com.oax.utils.UsdtRPCApiUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/8/14
 * Time: 14:31
 * 扫描 平台转账 是否到账
 */
@Component
@Slf4j
public class OmniScanOutTxPoolPlatformTransferScheduled {

    @Autowired
    private PlatformTransferService platformTransferService;

    @Autowired
    private UsdtRPCApiUtils usdtRPCApiUtils;

    @Autowired
    private CoinService coinService;


    /**
     * 每十分钟
     */
    @Async
    @Scheduled(cron = "0 0/30 * * * ?")
    public void scanOutTxPoolTransfer() {

        log.error("--------------Omni-执行scanOutTxPoolTransfer---------------");

        List<Coin> usdtTokenCoin = coinService.selectUsdtCoin();

        for (Coin tokenCoin : usdtTokenCoin) {
            List<PlatformTransfer> platformTransferList = platformTransferService.selectByCoinIdAndStatus(tokenCoin.getId(), PlatformTransferTypeEnum.OUT_TXPOOL_STATUS.getType());

            for (PlatformTransfer platformTransfer : platformTransferList) {

                try {
                    JSONObject transaction = usdtRPCApiUtils.getTransaction(platformTransfer.getTxId());

                    if (!transaction.getBoolean("valid")) {
                        log.info("不是有效数据");
                        continue;
                    }
                    int propertyidResult = transaction.getIntValue("propertyid");
                    if (propertyidResult != tokenCoin.getPropertyid()) {
                        log.info("非USDT数据");
                        continue;
                    }

                    BigDecimal fee = transaction.getBigDecimal("fee");

                    platformTransfer.setTxFee(fee.abs());
                    platformTransfer.setStatus(PlatformTransferTypeEnum.CONFIRM_STATUS.getType());
                    platformTransfer.setConfirmTime(new Date());
                    platformTransferService.update(platformTransfer);

                } catch (Exception e) {
                    log.error("scanOutTxPoolTransfer请求出错", e);
                }

            }
        }

        log.error("--------------Omni-执行scanOutTxPoolTransfer结束---------------");

    }


}
