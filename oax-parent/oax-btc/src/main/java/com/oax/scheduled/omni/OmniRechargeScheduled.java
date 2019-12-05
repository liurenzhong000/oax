package com.oax.scheduled.omni;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.oax.common.enums.SysConfigEnum;
import com.oax.entity.front.*;
import com.oax.service.*;
import com.oax.utils.UsdtRPCApiUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/8/13
 * Time: 9:45
 * <p>
 * 扫面用户入账(充值)
 */
@Component
@Slf4j
public class OmniRechargeScheduled {


    @Autowired
    private SysConfigService sysConfigService;

    @Autowired
    private CoinService coinService;

    @Autowired
    private RechargeService rechargeService;

    @Autowired
    private UsdtRPCApiUtils usdtRPCApiUtils;

    @Autowired
    private RechargeAddressService rechargeAddressService;

    @Value("${usdt.confirmations.num}")
    private Integer confirmationsNum;


    @Async
    @Scheduled(fixedRate = 1000 * 60 * 60 * 24 * 365)
    public void scanRecharge() {


        List<Coin> usdtTokenCoin = coinService.selectUsdtCoin();

        while (true) {
            try {
                SysConfig sysConfig = sysConfigService.selectByKey(SysConfigEnum.USDT_BLOCK_NUMBER.getName());


                //系统查询区块高度
                BigInteger dbblockNumber = new BigInteger(sysConfig.getValue());

                BigInteger getblockcount;
                try {
                    getblockcount = usdtRPCApiUtils.getblockcount();
                } catch (Exception e) {
                    log.error("USDT接口请求失败", e);
                    continue;
                }

                if (getblockcount.subtract(dbblockNumber).intValue() < confirmationsNum) {
                    try {
                        Thread.sleep(60 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    continue;
                }

                log.error("------------Omni-同步USDT区块:" + dbblockNumber + "-------------");
                //查询所有用户的 usdt地址
                List<RechargeAddress> rechargeAddressList = rechargeAddressService.selectByUsdtAddress();
                try {
                    JSONArray jsonArray = usdtRPCApiUtils.omniListblocktransactions(dbblockNumber.intValue());

                    for (int i = 0; i < jsonArray.size(); i++) {

                        String txId = jsonArray.getString(i);

                        JSONObject transaction = usdtRPCApiUtils.getTransaction(txId);
                        if (!transaction.getBoolean("valid")) {
                            log.info("不是有效数据");
                            continue;
                        }
                        int propertyidResult = transaction.getIntValue("propertyid");
                        Optional<Coin> first = usdtTokenCoin.stream()
                                .filter(e -> e.getPropertyid().equals(propertyidResult))
                                .findFirst();

                        if (!first.isPresent()) {
                            continue;
                        }
                        double value = transaction.getDouble("amount");
                        if (value > 0) {
                            Coin coin = first.get();


                            Optional<RechargeAddress> optionalRechargeAddress = rechargeAddressList.stream()
                                    .filter(rechargeAddress -> rechargeAddress.getAddress().equalsIgnoreCase(transaction.getString("referenceaddress")))
                                    .findFirst();

                            if (optionalRechargeAddress.isPresent()) {
                                RechargeAddress rechargeAddress = optionalRechargeAddress.get();
                                Recharge recharge = new Recharge();

                                recharge.setFromAddress(transaction.getString("sendingaddress"));
                                recharge.setToAddress(rechargeAddress.getAddress());
                                recharge.setCoinId(coin.getId());
                                recharge.setUserId(rechargeAddress.getUserId());
                                recharge.setQty(transaction.getBigDecimal("amount"));
                                recharge.setTxId(txId);
                                int insert = rechargeService.insertIgnoreAndAddUserBalance(recharge);
                                if (insert > 0) {
                                    log.info("插入USDT转入记录::{}", recharge);
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    log.error("USDT接口出错:", e);
                }

                sysConfig.setValue(dbblockNumber.add(new BigInteger("1")).toString());
                sysConfigService.update(sysConfig);
                rechargeAddressList = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }

}
