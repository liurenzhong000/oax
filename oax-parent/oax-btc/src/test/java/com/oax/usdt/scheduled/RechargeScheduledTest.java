package com.oax.usdt.scheduled;

import java.math.BigDecimal;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oax.OaxApiApplicationTest;
import com.oax.entity.front.RechargeAddress;
import com.oax.scheduled.omni.OmniRechargeScheduled;
import com.oax.scheduled.omni.OmniScanOutTxPoolPlatformTransferScheduled;
import com.oax.scheduled.omni.OmniScanWaningQtyScheduled;
import com.oax.scheduled.omni.OmniScanWithdrawTransferScheduled;
import com.oax.scheduled.omni.OmniTransferColdAddressScheduled;
import com.oax.scheduled.omni.OmniUserToMainAddressScheduled;
import com.oax.service.RechargeAddressService;
import com.oax.utils.UsdtRPCApiUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/8/13
 * Time: 15:52
 */
@Component
@Slf4j
public class RechargeScheduledTest extends OaxApiApplicationTest {

    @Autowired
    private OmniRechargeScheduled rechargeScheduled;
    @Test
    public void scanRecharge() {
        rechargeScheduled.scanRecharge();
    }

    @Autowired
    private OmniScanWaningQtyScheduled omniScanWaningQtyScheduled;

    @Test
    public void scanWaningQty() {
        omniScanWaningQtyScheduled.scanWaningQty();
    }

    @Autowired
    private OmniScanWithdrawTransferScheduled omniScanWithdrawTransferScheduled;

    @Test
    public void scanWithdrawTransfer() {
        omniScanWithdrawTransferScheduled.scanWithdrawTransfer();
    }


    @Autowired
    private OmniTransferColdAddressScheduled omniTransferColdAddressScheduled;

    @Test
    public void transferColdAddress() {
        omniTransferColdAddressScheduled.transferColdAddress();
    }

    @Autowired
    private OmniUserToMainAddressScheduled omniUserToMainAddressScheduled;

    @Test
    public void userToMainAddress() {
        omniUserToMainAddressScheduled.userToMainAddress();
    }





    @Autowired
    private OmniScanOutTxPoolPlatformTransferScheduled omniScanOutTxPoolPlatformTransferScheduled;

    @Test
    public void scanOutTxPoolPlatformTransferScheduled() {
        omniScanOutTxPoolPlatformTransferScheduled.scanOutTxPoolTransfer();
    }

    @Autowired
    private RechargeAddressService rechargeAddressService;
    @Test
    public void test() {

        List<RechargeAddress> rechargeAddressList = rechargeAddressService.selectByUsdtAddress();

        assert rechargeAddressList.size() > 0;

    }

    @Autowired
    private UsdtRPCApiUtils usdtRPCApiUtils;
    @Test
    public void name() throws Exception {

        BigDecimal ba = usdtRPCApiUtils.getBtcBalanceByAddress("mjyduPCnpGqyBVxFBuHV1DeJT94NvBxdaS");

        assert ba.toString().length()>0;
    }

    @Test
    public void sendByRawTransaction() throws Exception {
        usdtRPCApiUtils.sendByRawTransaction("mfrowNgczrif4imCFtwEQoa2sYtjbjSqeo","moUPk9iZMjZ5dXxFhjRdZXGfURKmgqpBEq",new BigDecimal("0.11"),2,"moUPk9iZMjZ5dXxFhjRdZXGfURKmgqpBEq",new BigDecimal("0.01"));
    }


    @Test
    public void testSendByMain() throws Exception {

        String s = usdtRPCApiUtils.mainSendUsdt("muNDmYmGt8FVSRg27cDEAgbQaaWCHHcKMt",
                "n498q47gDJAH6V3CkY8LjBfcid7LCawhFg",
                new BigDecimal("0.01"),
                2,
                "muNDmYmGt8FVSRg27cDEAgbQaaWCHHcKMt",
                new BigDecimal("3"));


        log.info(s);
    }
}