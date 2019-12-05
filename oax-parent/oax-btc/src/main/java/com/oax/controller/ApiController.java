package com.oax.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.oax.common.ResultResponse;
import com.oax.common.enums.CoinTypeEnum;
import com.oax.entity.front.Coin;
import com.oax.service.CoinService;
import com.oax.utils.BtcRPCApiUtils;
import com.oax.utils.UsdtRPCApiUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/6/27
 * Time: 9:53
 */
@RestController
@Slf4j
@RequestMapping("/api")
public class ApiController {


    @Autowired
    BtcRPCApiUtils btcRPCApiUtils;



    @PostMapping("createNewAddress")
    public ResultResponse newAccount(){
        try {
            String newAccount = btcRPCApiUtils.newAccount();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("newAccount",newAccount);
            return new ResultResponse(true,jsonObject);
        } catch (Exception e) {
            log.error("------btc rpc newAccount send error-----",e);
            return new ResultResponse(false,e.getMessage());
        }
    }

    @GetMapping("/getBtcBalance/Address")
    public ResultResponse getbalanceByMainAddress(){
        try {
            List<JSONObject> listunspent = btcRPCApiUtils.listunspent();

            Coin btcCoin = coinService.selectBtcCoin();

            String mainAddress = btcCoin.getMainAddress();

            BigDecimal balanceWithOutMain = listunspent.stream()
                    .filter(e -> !StringUtils.equalsIgnoreCase(mainAddress, e.getString("address")))
                    .map(e -> e.getBigDecimal("amount"))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("balance",balanceWithOutMain);
            return new ResultResponse(true,jsonObject);
        } catch (Exception e) {
            log.error("-----getbalance send error-----",e);
            return new ResultResponse(false,e.getMessage());
        }
    }


    @GetMapping("/getBtcBalance")
    public ResultResponse getAddressbalance(){
        try {
            List<JSONObject> listunspent = btcRPCApiUtils.listunspent();

            BigDecimal balanceWithOutMain = listunspent.stream()
                    .map(e -> e.getBigDecimal("amount"))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("balance",balanceWithOutMain);
            return new ResultResponse(true,jsonObject);
        } catch (Exception e) {
            log.error("-----getbalance send error-----",e);
            return new ResultResponse(false,e.getMessage());
        }
    }


    @PostMapping("/transferBtc")
    public ResultResponse transferBtc(@RequestParam(name = "address")String address,
                                      @RequestParam(name = "amount")BigDecimal amount){

        Coin coin = coinService.selectBtcCoin();
        List<Coin> coinList = coinService.selectUsdtCoin();
        try {
            String txId = btcRPCApiUtils.sendtoaddressByRaw(address, amount,coin.getMainAddress(),coinList.get(0).getOutQtyToMainAddress());

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("txHashId",txId);

            return new ResultResponse(true,jsonObject);
        } catch (Exception e) {
            log.error("----error send transferBtc-----",e);
            return new ResultResponse(false,e.getMessage());
        }

    }


    @Autowired
    private UsdtRPCApiUtils usdtRPCApiUtils;

    @Autowired
    private CoinService coinService;


    @GetMapping("/getOmniAndBtcBalance/{address}/{propertyid}")
    public ResultResponse getOmniAndBtcBalance(@PathVariable(name = "address") String address,
                                     @PathVariable(name = "propertyid") Integer propertyid) {
        if (address == null) {
            return new ResultResponse(false, "地址不能为null");
        }

        try {
            BigDecimal getbalance = usdtRPCApiUtils.getbalance(address, propertyid);

            List<JSONObject> listunspent = usdtRPCApiUtils.listunspent();

            Coin btcCoin = coinService.selectBtcCoin();

            BigDecimal btcBalanceByAddress = listunspent.stream()
                    .filter(e -> StringUtils.equalsIgnoreCase(btcCoin.getMainAddress(), e.getString("address")))
                    .map(e -> e.getBigDecimal("amount"))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("balance", getbalance+"USDT + "+btcBalanceByAddress+"BTC");
            return new ResultResponse(true, jsonObject);
        } catch (Exception e) {
            log.error("-----getbalance send error-----", e);
            return new ResultResponse(false, e.getMessage());
        }
    }


    @GetMapping("/getOmniTokenBalance/{address}/{propertyid}")
    public ResultResponse getBalance(@PathVariable(name = "address") String address,
                                     @PathVariable(name = "propertyid") Integer propertyid) {

        if (address == null) {
            return new ResultResponse(false, "地址不能为null");
        }

        try {
            BigDecimal getbalance = usdtRPCApiUtils.getbalance(address, propertyid);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("balance", getbalance);
            return new ResultResponse(true, jsonObject);
        } catch (Exception e) {
            log.error("-----getbalance send error-----", e);
            return new ResultResponse(false, e.getMessage());
        }
    }

    @PostMapping("/transferOmniToken")
    public ResultResponse transferOmniToken(@RequestParam(name = "address") String address,
                                            @RequestParam(name = "amount") BigDecimal amount,
                                            @RequestParam(name = "coinId") Integer coinId) {
        List<Coin> usdtTokenCoin = coinService.selectUsdtCoin();

        Coin coin = coinService.selectById(coinId);

        if (!coin.getType().equals(CoinTypeEnum.USDT.getType())) {
            return new ResultResponse(false,"币种不是OMNI类型");
        }

        Optional<Coin> optionalCoin = usdtTokenCoin.stream()
                .filter(usdtToken -> usdtToken.getPropertyid().equals(coin.getPropertyid()))
                .findFirst();

        if (!optionalCoin.isPresent()){
            return new ResultResponse(false,"没有propertyid:"+coin.getPropertyid()+"对应的omni-token");
        }
        Coin tokenCoin = optionalCoin.get();
        String from = tokenCoin.getMainAddress();


        try {

            BigDecimal getbalance = usdtRPCApiUtils.getbalance(from,tokenCoin.getPropertyid());
            if (getbalance.compareTo(amount) < 0) {

                return new ResultResponse(false, "主地址没有足够的USDT");
            }

            String txId = usdtRPCApiUtils.mainSendUsdt(from, address, amount,tokenCoin.getPropertyid(),tokenCoin.getMainAddress(),tokenCoin.getOutQtyToMainAddress());
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("txHashId", txId);

            return new ResultResponse(true, jsonObject);
        } catch (Exception e) {
            log.error("----error send transferUSDT-----", e);
            return new ResultResponse(false, e.getMessage());
        }

    }

    @PostMapping("/testTransferOmniToken")
    public ResultResponse testtransferOmniToken(@RequestParam(name = "address") String address,
                                                @RequestParam(name = "amount") BigDecimal amount,
                                                @RequestParam(name = "propertyid") Integer propertyid) {
        List<Coin> usdtTokenCoin = coinService.selectUsdtCoin();



        Optional<Coin> optionalCoin = usdtTokenCoin.stream()
                .filter(usdtToken -> usdtToken.getPropertyid().equals(propertyid))
                .findFirst();

        if (!optionalCoin.isPresent()){
            return new ResultResponse(false,"没有propertyid:"+propertyid+"对应的omni-token");
        }
        Coin tokenCoin = optionalCoin.get();
        String from = "n4Uf5FfwNZYvJyhd3WJFpg78hw6SUW92Jr";


        try {

            BigDecimal getbalance = usdtRPCApiUtils.getbalance(from,tokenCoin.getPropertyid());
            if (getbalance.compareTo(amount) < 0) {

                return new ResultResponse(false, "主地址没有足够的USDT");
            }

            String txId = usdtRPCApiUtils.sendByRawTransaction(from, address, amount,tokenCoin.getPropertyid(),tokenCoin.getMainAddress(),tokenCoin.getOutQtyToMainAddress());
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("txHashId", txId);

            return new ResultResponse(true, jsonObject);
        } catch (Exception e) {
            log.error("----error send transferUSDT-----", e);
            return new ResultResponse(false, e.getMessage());
        }

    }



}
