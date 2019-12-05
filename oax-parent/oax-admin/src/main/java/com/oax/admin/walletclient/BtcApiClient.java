package com.oax.admin.walletclient;

import java.math.BigDecimal;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.oax.common.ResultResponse;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/6/29
 * Time: 18:46
 */
@FeignClient(name = "btc",
        value = "btc",
        url = "${wallet.btc.server.url}")
@RequestMapping("/api")
public interface BtcApiClient {


    @PostMapping("createNewAddress")
    public ResultResponse newAccount();

    @GetMapping("/getBtcBalance")
    public ResultResponse getAddressbalance();

    @GetMapping("/getBtcBalance/Address")
    public ResultResponse getbalanceByMainAddress();

    @PostMapping("/transferBtc")
    public ResultResponse transferBtc(@RequestParam(name = "address")String address,
                                      @RequestParam(name = "amount")BigDecimal amount);

    @GetMapping("/getOmniTokenBalance/{address}/{propertyid}")
    public ResultResponse getBalance(@PathVariable(name = "address") String address,
                                     @PathVariable(name = "propertyid") Integer propertyid);

    @PostMapping("/transferOmniToken")
    public ResultResponse transferOmniToken(@RequestParam(name = "address") String address,
                                            @RequestParam(name = "amount") BigDecimal amount,
                                            @RequestParam(name = "coinId") Integer coinId);

    @PostMapping("/testTransferOmniToken")
    public ResultResponse testtransferOmniToken(@RequestParam(name = "address") String address,
                                                @RequestParam(name = "amount") BigDecimal amount,
                                                @RequestParam(name = "propertyid") Integer propertyid);

    @GetMapping("/getOmniAndBtcBalance/{address}/{propertyid}")
    public ResultResponse getOmniAndBtcBalance(@PathVariable(name = "address") String address,
                                               @PathVariable(name = "propertyid") Integer propertyid) ;



}
