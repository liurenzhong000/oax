package com.oax.scheduled;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.oax.common.enums.CoinTypeEnum;
import com.oax.entity.front.Coin;
import com.oax.entity.front.Recharge;
import com.oax.entity.front.RechargeAddress;
import com.oax.entity.front.UserCoin;
import com.oax.service.CoinService;
import com.oax.service.RechargeAddressService;
import com.oax.service.RechargeService;
import com.oax.service.UserCoinService;
import com.oax.utils.BtcRPCApiUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/6/27
 * Time: 10:38
 * 同步
 * 转入记录
 */
@Component
@Slf4j
public class RechargeScheduled {

    @Autowired
    private UserCoinService userCoinService;

    @Autowired
    private CoinService coinService;

    @Autowired
    private RechargeService rechargeService;

    @Autowired
    private BtcRPCApiUtils btcRPCApiUtils;


    @Autowired
    private RechargeAddressService rechargeAddressService;

    @Value("${btc.min.recharge.qty}")
    private String minRecharge;


    @Value("${usdt.confirmations.num}")
    private Integer confirmationsNum;


    @Async
    @Scheduled(fixedRate = 1000 * 60 * 9)
    public void scanRecharge() {

        log.error("------------执行scanRecharge-----------");

        JSONArray listtransactions = null;
        try {
            listtransactions = btcRPCApiUtils.listtransactions();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        List<Coin> coinList = coinService.selectByType(CoinTypeEnum.BTC.getType());
        List<RechargeAddress> rechargeAddressList = rechargeAddressService.selectByCoinType(CoinTypeEnum.BTC.getType());
        List<Recharge> allRechargeList = rechargeService.selectAll();




//
//        listtransactions.stream()
//                .map(e -> (JSONObject) e)
//                //所有入账记录
//                .filter(e ->  StringUtils.equalsIgnoreCase(e.getString("category"), "receive")&&
//                        e.getInteger("confirmations")>confirmationsNum)
//                //所有数据库地址的交易
//                .filter(e -> {
//                    String address = e.getString("address");
//
//                    Optional<RechargeAddress> first = rechargeAddressList.stream()
//                            .filter(rechargeAddress -> StringUtils.equalsIgnoreCase(rechargeAddress.getAddress(), address))
//                            .findFirst();
//                    return first.isPresent();
//                })
//                //过滤所有 数据库存在交易
//                .filter(e -> {
//                        Optional<Recharge> rechargeOptional = allRechargeList.stream()
//                                .filter(dbRecharge -> StringUtils.equalsIgnoreCase(dbRecharge.getTxId(), e.getString("txid"))
//                                ).findFirst();
//                        return !rechargeOptional.isPresent();
//
//                }).collect(Collectors.toList());


        log.error("------------正在同步区块高度-----------");

        for (RechargeAddress rechargeAddress : rechargeAddressList) {


            List<Recharge> dbRechargeList = allRechargeList.stream()
                    .filter(recharge ->
                            StringUtils.equalsIgnoreCase(recharge.getToAddress(), rechargeAddress.getAddress()))
                    .collect(Collectors.toList());

            List<JSONObject> newRechargeList = listtransactions.stream()
                    .map(e -> (JSONObject) e)
                    //获取 所有对应地址 的到账记录
                    .filter(e -> StringUtils.equalsIgnoreCase(e.getString("address"), rechargeAddress.getAddress()) &&
                            StringUtils.equalsIgnoreCase(e.getString("category"), "receive")&&
                            e.getInteger("confirmations")>confirmationsNum)
                    //过滤数据库有的txid 交易
                    .filter(e -> {

                        Optional<Recharge> rechargeOptional = dbRechargeList.stream()
                                .filter(dbRecharge -> StringUtils.equalsIgnoreCase(dbRecharge.getTxId(), e.getString("txid"))
                                ).findFirst();
                        return !rechargeOptional.isPresent();
                    }).collect(Collectors.toList());


            for (JSONObject jsonObject : newRechargeList) {
                Recharge recharge = new Recharge();

                String txid = jsonObject.getString("txid");
                BigDecimal qty = jsonObject.getBigDecimal("amount");

                if (qty.compareTo(new BigDecimal(minRecharge)) < 0) {
                    continue;
                }
                recharge.setTxId(txid);
                recharge.setUserId(rechargeAddress.getUserId());
                recharge.setCoinId(coinList.get(0).getId());
                recharge.setQty(qty);
                try {
                    JSONObject transaction = btcRPCApiUtils.getTransaction(jsonObject.getString("txid"));

                    JSONArray details = transaction.getJSONArray("details");
                    Optional<JSONObject> send = details.stream()
                            .map(e -> (JSONObject) e)
                            .filter(e -> StringUtils.equalsIgnoreCase(e.getString("category"), "send"))
                            .findFirst();


                    if (send.isPresent()) {
                        String from = send.get().getString("address");
                        recharge.setFromAddress(from);
                    }

                    recharge.setToAddress(rechargeAddress.getAddress());


                    Coin coin = coinList.get(0);
                    UserCoin userCoin = userCoinService.selectByUserIdAndCoinId(rechargeAddress.getUserId(), coin.getId());

                    if (userCoin == null) {
                        userCoin = new UserCoin();
                        userCoin.setCoinId(coin.getId());
                        userCoin.setUserId(rechargeAddress.getUserId());
                        userCoin.setBanlance(BigDecimal.ZERO);
                        userCoinService.insert(userCoin);
                    }

                    int insert = rechargeService.insertAndaddBalance(recharge, userCoin);
                    if (insert>0){
                        log.info("----插入用户充值记录---,{}", JSON.toJSONString(recharge));
                        log.info("----更改用户余额----qty:{},更改后:{}", qty, userCoin);
                    }
                } catch (Exception e) {
                    log.error("---充值记录插入失败---", e);
                }
            }

        }

        log.error("------------执行scanRecharge结束-----------");

    }


}
