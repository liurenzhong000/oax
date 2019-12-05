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
import com.oax.common.enums.PlatformTransferTypeEnum;
import com.oax.entity.front.Coin;
import com.oax.entity.front.PlatformTransfer;
import com.oax.service.CoinService;
import com.oax.service.PlatformTransferService;
import com.oax.utils.BtcRPCApiUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/6/28
 * Time: 14:22
 * 转入冷钱包
 */
@Component
@Slf4j
public class TransferColdAddressSchduled {


    @Autowired
    private CoinService coinService;

    @Autowired
    private PlatformTransferService platformTransferService;

    @Autowired
    private BtcRPCApiUtils btcRPCApiUtils;


    /**
     * 每天凌晨2点
     */
//    @Scheduled(cron="0 0 2 * * ?")
    @Scheduled(cron="0 0/10 * * * ?")
    @Async
    public void transferColdAddress(){

        log.error("------------执行transferColdAddress--------------");

        try {
            List<Coin> coinList = coinService.selectByType(CoinTypeEnum.BTC.getType());
            Coin btcCoin = coinList.get(0);
            String getbalance = btcRPCApiUtils.getbalance(null);
            BigDecimal btcBalance = new BigDecimal(getbalance);
            BigDecimal outQtyToColdAddress = btcCoin.getOutQtyToColdAddress();

            List<Coin> tokenCoinList = coinService.selectUsdtCoin();

            if (btcBalance.compareTo(outQtyToColdAddress)>0){
                //需要转入冷钱包

                BigDecimal subtract = btcBalance.subtract(outQtyToColdAddress);

                String txId = btcRPCApiUtils.sendtoaddressByRaw(btcCoin.getColdAddress(), subtract,btcCoin.getMainAddress(),tokenCoinList.get(0).getOutQtyToMainAddress());

                JSONObject transaction = btcRPCApiUtils.getTransaction(txId);
                BigDecimal fee = transaction.getBigDecimal("fee");

                PlatformTransfer platformTransfer = new PlatformTransfer();
                platformTransfer.setTxFee(fee.abs());
                platformTransfer.setQty(subtract);
                platformTransfer.setStatus(PlatformTransferTypeEnum.CONFIRM_STATUS.getType());
                platformTransfer.setTxId(txId);
                platformTransfer.setType(PlatformTransferTypeEnum.TO_COLD_ADDRESS_TYPE.getType());
                platformTransfer.setCoinId(btcCoin.getId());
                platformTransfer.setToAddress(btcCoin.getColdAddress());
                platformTransfer.setFromAddress(btcCoin.getMainAddress());
                platformTransfer.setConfirmTime(new Date());

                platformTransferService.insert(platformTransfer);
                log.info("----转入冷钱包记录----:::{}",JSON.toJSONString(platformTransfer));
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.error("转入冷钱包失败",e);
        }
        log.error("------------执行transferColdAddress结束--------------");
    }

}
