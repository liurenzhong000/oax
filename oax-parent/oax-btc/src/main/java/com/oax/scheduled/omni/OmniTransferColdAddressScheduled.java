package com.oax.scheduled.omni;

import java.math.BigDecimal;
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
 * Date: 2018/8/13
 * Time: 14:48
 * 转入冷钱包
 */
@Component
@Slf4j
public class OmniTransferColdAddressScheduled {

    @Autowired
    private CoinService coinService;

    @Autowired
    private PlatformTransferService platformTransferService;

    @Autowired
    private UsdtRPCApiUtils usdtRPCApiUtils;

    /**
     * 每天凌晨2点
     */
    @Scheduled(cron="0 0/10 * * * ?")
    @Async
    public void transferColdAddress() {

        log.error("------------Omni-执行transferColdAddress--------------");


        List<Coin> usdtTokenCoin = coinService.selectUsdtCoin();

        for (Coin tokenCoin : usdtTokenCoin) {
            try {
                BigDecimal balance = usdtRPCApiUtils.getbalance(tokenCoin.getMainAddress(),tokenCoin.getPropertyid());

                BigDecimal outQtyToColdAddress = tokenCoin.getOutQtyToColdAddress();

                BigDecimal subtract = balance.subtract(outQtyToColdAddress);

                if (subtract.compareTo(BigDecimal.ZERO)>0){

                    String from = tokenCoin.getMainAddress();
                    String to = tokenCoin.getColdAddress();
                    String txId = usdtRPCApiUtils.mainSendUsdt(from, to, subtract,tokenCoin.getPropertyid(),tokenCoin.getMainAddress(),tokenCoin.getOutQtyToMainAddress());

                    JSONObject transaction = usdtRPCApiUtils.getTransaction(txId);

                    BigDecimal fee = transaction.getBigDecimal("fee");

                    PlatformTransfer platformTransfer = new PlatformTransfer();

                    platformTransfer.setFromAddress(from);
                    platformTransfer.setToAddress(to);
                    platformTransfer.setTxId(txId);
                    platformTransfer.setQty(subtract);
                    platformTransfer.setCoinId(tokenCoin.getId());
                    platformTransfer.setTxFee(fee.abs());
                    platformTransfer.setStatus(PlatformTransferTypeEnum.OUT_TXPOOL_STATUS.getType());
                    platformTransfer.setType(PlatformTransferTypeEnum.TO_COLD_ADDRESS_TYPE.getType());
                    platformTransferService.insert(platformTransfer);

                    log.info("转入主地址成功:{}",platformTransfer);
                }

            } catch (Exception e) {
                log.error("转入冷钱失败.",e);
            }
        }


        log.error("------------Omni-执行transferColdAddress结束--------------");

    }
}
