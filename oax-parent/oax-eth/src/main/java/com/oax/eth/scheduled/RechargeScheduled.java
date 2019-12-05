package com.oax.eth.scheduled;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.oax.common.enums.CoinTypeEnum;
import com.oax.common.enums.SysConfigEnum;
import com.oax.entity.front.Coin;
import com.oax.entity.front.Recharge;
import com.oax.entity.front.RechargeAddress;
import com.oax.entity.front.SysConfig;
import com.oax.eth.service.CoinService;
import com.oax.eth.service.RechargeAddressService;
import com.oax.eth.service.RechargeService;
import com.oax.eth.service.SysConfigService;
import com.oax.eth.utils.EthRPCApiUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/6/20
 * Time: 17:20
 * <p>
 * 充值记录 定时任务
 */
@Slf4j
@Component
public class RechargeScheduled {

    @Autowired
    private CoinService coinService;

    @Autowired
    private SysConfigService sysConfigService;

    @Autowired
    private RechargeService rechargeService;

    @Autowired
    private RechargeAddressService rechargeAddressService;

    @Autowired
    private EthRPCApiUtils ethRPCApiUtils;


    @Async
    @Scheduled(fixedRate = 1000 * 60 * 60 * 24 * 365)
    public void scanRecharge() {

        log.error("--------------执行scanRecharge---------------");
        while (true) {


            SysConfig sysConfig = sysConfigService.selectByKey(SysConfigEnum.ETH_BLOCK_NUMBER.getName());


            //系统查询区块高度
            BigInteger blockNumber = new BigInteger(sysConfig.getValue());

            //当前区块高度
            BigInteger lastblockNumber = null;
            try {
                lastblockNumber = ethRPCApiUtils.getBlockNumber();
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }


            if (lastblockNumber.subtract(blockNumber).intValue() < 10) {
                try {
                    Thread.sleep(60*1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                continue;
            }

            log.error("--------------正在同步区块高度,当前区块{},同步区块{}---------------", lastblockNumber, blockNumber);

            BigInteger txCount = null;
            try {
                txCount = ethRPCApiUtils.getBlockTransactionCountByNumber(blockNumber);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }

            //用户 币交易地址
            List<RechargeAddress> rechargeAddressList = rechargeAddressService.selectAllETHAdress();


            //ETH与EHTtoken所有合约地址

            List<Coin> ethList = coinService.selectByType(CoinTypeEnum.ETH.getType());
            Coin ethCoin = ethList.get(0);

            List<Coin> coinList = coinService.selectByType(CoinTypeEnum.ETH_TOKEN.getType());
            coinList = coinList.stream()
                    .filter(coin -> coin.getContractAddress() != null)
                    .collect(Collectors.toList());

            for (int i = 0; i < txCount.intValue(); i++) {

                Map<String, Object> tx = null;
                try {
                    tx = ethRPCApiUtils.getTransactionByBlockNumberAndIndex(blockNumber, new BigInteger(i + ""));
                } catch (Exception e) {
                    e.printStackTrace();
                    continue;
                }

//                log.info("TxHash::{}", tx.get("hash"));

                Object input = tx.get("input");
                if (input != null && StringUtils.equals((String) input, "0x6f6178")) {
                    //说明是内部转账
                    continue;
                }

                String to = (String) tx.get("to");
                Optional<RechargeAddress> optionalRechargeAddress = rechargeAddressList.stream()
                        .filter(rechargeAddress -> StringUtils.equalsIgnoreCase(to, rechargeAddress.getAddress()))
                        .findFirst();
                if (optionalRechargeAddress.isPresent()) {
                    //找到
                    //说明 用户转入 以太坊
                    RechargeAddress rechargeAddress = optionalRechargeAddress.get();
                    if (insertETHRecharge(tx, to, rechargeAddress, ethCoin)) continue;

                }

                Optional<Coin> coinOptional = coinList.stream()
                        .filter(coin -> StringUtils.equalsIgnoreCase(to, coin.getContractAddress()))
                        .findFirst();

                if (coinOptional.isPresent()) {
                    //找到
                    //说明 用户转入 代币
                    Coin coin = coinOptional.get();
                    if (insertEHTTokenRecharge(tx, (String) input, coin)) continue;
                }
            }


            //区块高度增加
            sysConfig.setValue(blockNumber.add(new BigInteger("1")).toString());
            sysConfigService.update(sysConfig);
            rechargeAddressList = null;
            ethList = null;
            coinList = null;
        }
    }

    private boolean insertEHTTokenRecharge(Map<String, Object> tx, String input, Coin coin) {
        String hash = (String) tx.get("hash");
        Map<String, Object> txReceipt = null;
        try {
            txReceipt = ethRPCApiUtils.getTransactionReceipt(hash);
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
        Object status = txReceipt.get("status");
        if (status == null) {
            //转账未确定
            return true;
        }
        BigInteger bigInteger = EthRPCApiUtils.toNum((String) status);
        if (bigInteger.intValue() != 1) {
            //转账不成功
            return true;
        }

        JSONArray logs = (JSONArray) txReceipt.get("logs");
        if (logs.size() <= 0) {
            return true;
        }
        JSONObject o = (JSONObject) logs.get(0);
        JSONArray topics = o.getJSONArray("topics");

        String encode = "0xddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef";
        if (!(topics != null && topics.size() == 3 && encode.equals(topics.get(0)))) {
            return true;
        }


        Recharge recharge = new Recharge();

        try {
            /**
             * 分割 input获取 toaddress,qty
             */
            partitionInputDataAndSetRecharge(input, coin, recharge);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return true;
        }

        String from = (String) tx.get("from");
        recharge.setFromAddress(from);
        recharge.setTxId(hash);
        String toAddress = recharge.getToAddress();


        RechargeAddress rechargeAddress = rechargeAddressService.selectByAddressAndParentCoinId(toAddress, coin.getParentId());

        if (rechargeAddress == null) {
            return true;
        }

        recharge.setUserId(rechargeAddress.getUserId());
        recharge.setCoinId(coin.getId());
        int insert = rechargeService.insertIgnoreAndAddUserBalance(recharge, rechargeAddress);
        if (insert > 0) {
            log.info("插入ETHToken转入数据::{}", JSON.toJSONString(recharge));
        }
        return false;
    }

    private void partitionInputDataAndSetRecharge(String input, Coin coin, Recharge recharge) throws Throwable {
        String inputData = input;
        //拆分合约数据
        inputData = inputData.replace(coin.getMethodId(), "");
        String toAddressInfo = inputData.substring(0, inputData.length() / 2);
        String moneyInfo = inputData.substring(inputData.length() / 2);
        String toAddress = EthRPCApiUtils.getridof_zero_address(toAddressInfo);
        BigDecimal money = new BigDecimal(EthRPCApiUtils.toNum(EthRPCApiUtils.getridof_zero(moneyInfo)).toString());
        BigDecimal realMoney = money.divide(new BigDecimal(10).pow(coin.getDecimals()));

        recharge.setToAddress(toAddress);
        recharge.setQty(realMoney);
    }

    private boolean insertETHRecharge(Map<String, Object> tx, String to, RechargeAddress rechargeAddress, Coin eth) {
        String hash = (String) tx.get("hash");

        Map<String, Object> txReceipt = null;
        try {
            txReceipt = ethRPCApiUtils.getTransactionReceipt(hash);
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }

        Object status = txReceipt.get("status");
        if (status == null) {
            //转账未确定
            return true;
        }
        BigInteger bigInteger = EthRPCApiUtils.toNum((String) status);
        if (bigInteger.intValue() != 1) {
            //转账不成功
            return true;
        }

        String from = (String) tx.get("from");
        String value = (String) tx.get("value");
        BigDecimal qty = EthRPCApiUtils.stringToDBNum(value);


        Recharge recharge = new Recharge();
        recharge.setCoinId(eth.getId());
        recharge.setUserId(rechargeAddress.getUserId());
        recharge.setFromAddress(from);
        recharge.setToAddress(to);
        recharge.setTxId(hash);
        recharge.setQty(qty);
        int insert = rechargeService.insertIgnoreAndAddUserBalance(recharge, rechargeAddress);
        if (insert > 0) {
            log.info("插入ETH转入数据::{}", JSON.toJSONString(recharge));
        }
        return false;
    }


}
