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
import com.oax.entity.front.RechargeAddress;
import com.oax.service.CoinService;
import com.oax.service.PlatformTransferService;
import com.oax.service.RechargeAddressService;
import com.oax.utils.UsdtRPCApiUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/8/13
 * Time: 11:52
 * 转入主地址
 */
@Component
@Slf4j
public class OmniUserToMainAddressScheduled {


    @Autowired
    private CoinService coinService;

    @Autowired
    private PlatformTransferService platformTransferService;

    @Autowired
    private RechargeAddressService rechargeAddressService;
    @Autowired
    private UsdtRPCApiUtils usdtRPCApiUtils;

    /**
     * 每整点
     */
    @Async
    @Scheduled(cron = "0 0 0/1 * * ?")
    public void userToMainAddress() {


        log.error("----------Omni-执行userToMainAddress------------");

        List<Coin> usdtTokenCoin = coinService.selectUsdtCoin();

        List<RechargeAddress> rechargeAddressList = rechargeAddressService.selectByUsdtAddress();

        for (RechargeAddress rechargeAddress : rechargeAddressList) {

            for (Coin tokenCoin : usdtTokenCoin) {
                try {

                    BigDecimal balance = usdtRPCApiUtils.getbalance(rechargeAddress.getAddress(),tokenCoin.getPropertyid());

                    if (balance.compareTo(tokenCoin.getOutQtyToMainAddress()) > 0) {

                        String to = tokenCoin.getMainAddress();
                        String from = rechargeAddress.getAddress();


                        String txId = usdtRPCApiUtils.sendByRawTransaction(from, to, balance,tokenCoin.getPropertyid(),tokenCoin.getMainAddress(),tokenCoin.getOutQtyToMainAddress());

                        JSONObject transaction = usdtRPCApiUtils.getTransaction(txId);
                        BigDecimal fee = transaction.getBigDecimal("fee");
                        PlatformTransfer platformTransfer = new PlatformTransfer();

                        platformTransfer.setFromAddress(from);
                        platformTransfer.setToAddress(to);
                        platformTransfer.setTxId(txId);
                        platformTransfer.setQty(balance);
                        platformTransfer.setCoinId(tokenCoin.getId());
                        platformTransfer.setTxFee(fee.abs());
                        platformTransfer.setStatus(PlatformTransferTypeEnum.OUT_TXPOOL_STATUS.getType());
                        platformTransfer.setType(PlatformTransferTypeEnum.TO_MAIN_ADDRESS_TYPE.getType());
                        platformTransferService.insert(platformTransfer);
                        log.info("转入主地址成功:{}", platformTransfer);
                    }
                } catch (Exception e) {
                    log.error("USDT-userToMainAddress出错", e);
                }

            }


        }
        log.error("----------Omni-执行userToMainAddress结束------------");
    }

}
