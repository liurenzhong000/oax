package com.oax.admin.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.oax.admin.walletclient.BtcApiClient;
import com.oax.admin.walletclient.EthApiClient;
import com.oax.common.ResultResponse;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/7/12
 * Time: 16:41
 */
@RestController
@RequestMapping("/test")
public class TestController{

    @Autowired
    EthApiClient ethApiClient;

    @Autowired
    BtcApiClient btcApiClient;



    private final static String X_CONTRACT_ADDRESS = "0x182b6dbc3bc49cd63b1bc47631860d7067d61d93";
    private final static String UNC_CONTRACT_ADDRESS = "0x41eE71224A1B0Ab6bA91e3F2bCA61BC7A265dfed";
    //private final static String METHOD_ID = "0xa9059cbb";


    private final static String ETH_ADDRESS = "0xa6fa236b605a5f0eeaaba635fcfa70bbd4d9cc90";
    private final static String BTC_ADDRESS = "n4Uf5FfwNZYvJyhd3WJFpg78hw6SUW92Jr";


    @GetMapping
    public List<String> getbalance() {

        ArrayList<String> strings = new ArrayList<>();


        ResultResponse ethBalance = ethApiClient.getEthBalance(ETH_ADDRESS);
        if (ethBalance.isSuccess()) {
            JSONObject data = JSON.parseObject(JSON.toJSONString(ethBalance.getData()));
            BigDecimal balance = data.getBigDecimal("balance");
            strings.add("ETH 余额:" + balance);
        }

        ResultResponse xtokenBalance = ethApiClient.getTokenBalance(X_CONTRACT_ADDRESS, ETH_ADDRESS);
        if (xtokenBalance.isSuccess()) {
            JSONObject data = JSON.parseObject(JSON.toJSONString(xtokenBalance.getData()));
            BigDecimal balance = data.getBigDecimal("balance");
            strings.add("X 余额:" + balance);
        }

        ResultResponse unctokenBalance = ethApiClient.getTokenBalance(UNC_CONTRACT_ADDRESS, ETH_ADDRESS);
        if (unctokenBalance.isSuccess()) {
            JSONObject data = JSON.parseObject(JSON.toJSONString(unctokenBalance.getData()));
            BigDecimal balance = data.getBigDecimal("balance");
            strings.add("UNC 余额:" + balance);
        }

        ResultResponse addressbalance = btcApiClient.getAddressbalance();
        if (addressbalance.isSuccess()) {
            JSONObject data = JSON.parseObject(JSON.toJSONString(addressbalance.getData()));
            BigDecimal balance = data.getBigDecimal("balance");
            strings.add("BTC 余额:" + balance);
        }

        ResultResponse usdtbalance = btcApiClient.getBalance(BTC_ADDRESS, 2);
        if (usdtbalance.isSuccess()) {
            JSONObject data = JSON.parseObject(JSON.toJSONString(usdtbalance.getData()));
            BigDecimal balance = data.getBigDecimal("balance");
            strings.add("USDT 余额:" + balance);
        }


        return strings;
    }


    @PostMapping("/transfer")
    public String transfer(@RequestBody RechargeInfo rechargeInfo) {
    	Integer coinType = rechargeInfo.getCoinType();
    	String address = rechargeInfo.getAddress();
    	BigDecimal qty = rechargeInfo.getQty();
        ResultResponse resultResponse = null;
        if (coinType == 1) {
            //eth
            resultResponse = ethApiClient.transferEthTest(ETH_ADDRESS, address, qty);
        } else if (coinType == 2) {
            //x
            resultResponse = ethApiClient.transferTokenTest(ETH_ADDRESS, address, qty, X_CONTRACT_ADDRESS);
        } else if (coinType == 3) {
            //unc
            resultResponse = ethApiClient.transferTokenTest(ETH_ADDRESS, address, qty, UNC_CONTRACT_ADDRESS);
        } else if (coinType == 4) {
            //btc
            resultResponse = btcApiClient.transferBtc(address, qty);
        }else if (coinType==5){
            //usdt
            resultResponse = btcApiClient.testtransferOmniToken(address,qty,2);
        }
        if (resultResponse.isSuccess()) {
            JSONObject data = JSON.parseObject(JSON.toJSONString(resultResponse.getData()));
            String txHashId = data.getString("txHashId");

            return  txHashId;
        }
        return resultResponse.getMsg();
    }

}

