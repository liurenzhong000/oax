package com.oax.scheduled;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oax.OaxApiApplicationTest;
import com.oax.scheduled.omni.OmniRechargeScheduled;

import lombok.extern.slf4j.Slf4j;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/6/29
 * Time: 9:57
 */
@Component
@Slf4j
public class TransferColdAddressSchduledTest extends OaxApiApplicationTest {

    @Autowired
    private TransferColdAddressSchduled transferColdAddressSchduled;
    @Test
    public void transferColdAddress() {
        transferColdAddressSchduled.transferColdAddress();
        log.info("执行成功");
    }


    @Autowired
    private ScanWithdrawTransferScheduled scanWithdrawTransferScheduled;

    @Test
    public void scanWithdrawTransfer() {
        scanWithdrawTransferScheduled.scanWithdrawTransfer();
        log.info("执行成功");
    }

    @Autowired
    private RechargeScheduled rechargeScheduled;

    @Test
    public void scanRecharge() {
        rechargeScheduled.scanRecharge();
        log.info("执行成功");
    }


    @Autowired
    private OmniRechargeScheduled omniRechargeScheduled;
    @Test
    public void omniScanRecharge() {
        omniRechargeScheduled.scanRecharge();
    }
}