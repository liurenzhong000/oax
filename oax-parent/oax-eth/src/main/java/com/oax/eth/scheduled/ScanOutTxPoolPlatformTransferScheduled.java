package com.oax.eth.scheduled;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.oax.common.enums.PlatformTransferTypeEnum;
import com.oax.entity.front.PlatformTransfer;
import com.oax.eth.service.PlatformTransferService;
import com.oax.eth.utils.EthRPCApiUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/6/22
 * Time: 16:44
 * 扫描所有 用户转入主钱包(PlatformTransfer) 是否出本地交易队(被打包),是否已成功
 */
@Component
@Slf4j
public class ScanOutTxPoolPlatformTransferScheduled {

    @Autowired
    private PlatformTransferService platformTransferService;

    @Autowired
    private EthRPCApiUtils ethRPCApiUtils;


    /**
     * 每十分钟
     */
    @Async
    @Scheduled(cron="0 0/10 * * * ?")
    public void scanOutTxPoolTransfer() {

        log.error("--------------执行ScanOutTxPoolPlatformTransferScheduled---------------");


        List<PlatformTransfer> platformTransferList = platformTransferService.selectByTypeAndStatus(
                null,
                PlatformTransferTypeEnum.IN_TXPOOL_STATUS.getType());
        ArrayList<Map<String, String>> allblockingTxList = ethRPCApiUtils.getAllTxpoolBlockings();
        if (allblockingTxList == null) return;

        for (PlatformTransfer platformTransfer : platformTransferList) {

            Optional<Map<String, String>> blockingTxOptional = allblockingTxList.stream()
                    .filter(e -> StringUtils.equalsIgnoreCase(e.get("hash"), platformTransfer.getTxId()))
                    .findFirst();

            if (!blockingTxOptional.isPresent()) {
                //说明已离开txpool
                //更改此 此状态
                platformTransfer.setStatus(PlatformTransferTypeEnum.OUT_TXPOOL_STATUS.getType());
                platformTransfer.setUpdateTime(null);
                platformTransferService.update(platformTransfer);
            }
        }

        List<PlatformTransfer> outTxpoolStatusList = platformTransferService.selectByTypeAndStatus(
                null,
                PlatformTransferTypeEnum.OUT_TXPOOL_STATUS.getType());

        for (PlatformTransfer platformTransfer : outTxpoolStatusList) {

            String txId = platformTransfer.getTxId();

            try {
                Map<String, Object> txReceipt = ethRPCApiUtils.getTransactionReceipt(txId);

                Object statusObject = txReceipt.get("status");
                if (statusObject != null) {
                    String statusString = (String) statusObject;

                    BigInteger status = EthRPCApiUtils.toNum(statusString);

                    if (status.intValue() == 1) {
                        //说明成功

                        Map<String, Object> transactionByHash = ethRPCApiUtils.getTransactionByHash(txId);


                        //获取使用费用
                        String gasUsedETH = (String) txReceipt.get("gasUsed");
                        String gasPriceETH = (String) transactionByHash.get("gasPrice");

                        BigInteger gasUsed = EthRPCApiUtils.toNum(gasUsedETH);
                        BigDecimal gasPrice = EthRPCApiUtils.stringToDBNum(gasPriceETH);

                        BigDecimal fee = gasPrice.multiply(new BigDecimal(gasUsed));
                        platformTransfer.setTxFee(fee.abs());

                        platformTransfer.setStatus(PlatformTransferTypeEnum.CONFIRM_STATUS.getType());
                        platformTransfer.setConfirmTime(new Date());
                        platformTransfer.setUpdateTime(null);
                        platformTransferService.update(platformTransfer);
                    } else if (status.intValue() == 0) {
                        //TODO 自动覆盖


                        Map<String, Object> transactionByHash = ethRPCApiUtils.getTransactionByHash(txId);


                        //获取使用费用
                        String gasUsedETH = (String) txReceipt.get("gasUsed");
                        String gasPriceETH = (String) transactionByHash.get("gasPrice");

                        BigInteger gasUsed = EthRPCApiUtils.toNum(gasUsedETH);
                        BigDecimal gasPrice = EthRPCApiUtils.stringToDBNum(gasPriceETH);

                        BigDecimal fee = gasPrice.multiply(new BigDecimal(gasUsed));
                        platformTransfer.setTxFee(fee.abs());

                        //说明失败
                        platformTransfer.setStatus(PlatformTransferTypeEnum.FALL_STATUS.getType());
                        platformTransfer.setUpdateTime(null);
                        platformTransferService.update(platformTransfer);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        log.error("--------------执行scanOutTxPoolTransfer结束---------------");


    }


}
