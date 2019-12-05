package com.oax.eth.scheduled;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.oax.common.enums.WithdrawStatusEnum;
import com.oax.entity.front.Withdraw;
import com.oax.eth.service.WithdrawService;
import com.oax.eth.utils.EthRPCApiUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/6/25
 * Time: 18:10
 * <p>
 * 扫描所有 (转出记录)主地址转出记录,且改变状态
 */
@Slf4j
@Component
public class ScanOutTxPoolWithdrawTransferScheduled {

    @Autowired
    private WithdrawService withdrawService;

    @Autowired
    private EthRPCApiUtils ethRPCApiUtils;

    public static void main(String[] args) {

    }
    /**
     * 每10分钟
     */
    @Async
    @Scheduled(cron="0 0/10 * * * ?")
    public void ScanOutTxPoolTransfer() {


        log.error("--------------执行ScanOutTxPoolWithdrawTransferScheduled---------------");


        List<Withdraw> inTxpoolStatusList = withdrawService.selectByStatus(WithdrawStatusEnum.IN_TXPOOL_STATUS.getStatus());
        inTxpoolStatusList = inTxpoolStatusList.stream()
                .filter(withdraw->withdraw.getTxId().contains("0x"))
                .collect(Collectors.toList());
        ArrayList<Map<String, String>> allblockingTxList = ethRPCApiUtils.getAllTxpoolBlockings();
        if (allblockingTxList == null) return;

        for (Withdraw withdraw : inTxpoolStatusList) {
            Optional<Map<String, String>> blockingTxOptional = allblockingTxList.stream()
                    .filter(e -> StringUtils.equalsIgnoreCase(e.get("hash"),withdraw.getTxId()))
                    .findFirst();

            if (!blockingTxOptional.isPresent()) {
                //说明已离开txpool
                //更改此 此状态
                withdraw.setStatus(WithdrawStatusEnum.OUT_TXPOOL_STATUS.getStatus());
                withdraw.setUpdateTime(null);
                withdrawService.update(withdraw);
            }
        }


        List<Withdraw> outTxpoolStatusList = withdrawService.selectByStatus(WithdrawStatusEnum.OUT_TXPOOL_STATUS.getStatus());

        for (Withdraw withdraw : outTxpoolStatusList) {


            String txId = withdraw.getTxId();

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

                        withdraw.setTxFee(withdraw.getTxFee().add(fee));

                        withdraw.setStatus(WithdrawStatusEnum.CONFIRM_STATUS.getStatus());
                        withdraw.setConfirmTime(new Date());
                        withdraw.setUpdateTime(null);
                        withdrawService.update(withdraw);
                    } else if (status.intValue() == 0) {
                        //TODO 自动覆盖(重试)


                        Map<String, Object> transactionByHash = ethRPCApiUtils.getTransactionByHash(txId);


                        //获取使用费用
                        String gasUsedETH = (String) txReceipt.get("gasUsed");
                        String gasPriceETH = (String) transactionByHash.get("gasPrice");

                        BigInteger gasUsed = EthRPCApiUtils.toNum(gasUsedETH);
                        BigDecimal gasPrice = EthRPCApiUtils.stringToDBNum(gasPriceETH);

                        BigDecimal fee = gasPrice.multiply(new BigDecimal(gasUsed));

                        withdraw.setTxFee(withdraw.getTxFee().add(fee));

                        //说明失败
                        withdraw.setStatus(WithdrawStatusEnum.FALL_STATUS.getStatus());
                        withdraw.setUpdateTime(null);
                        withdrawService.update(withdraw);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        log.error("--------------执行ScanOutTxPoolTransfer结束---------------");

    }

}
