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
 * Time: 18:35
 */
@FeignClient(name = "eth",
        value = "eth",
        url = "${wallet.eth.server.url}")
@RequestMapping("/api")
public interface EthApiClient {



    @GetMapping("getEthBalance/{address}")
    public ResultResponse getEthBalance(@PathVariable(name = "address") String address);

    @GetMapping("getTokenBalance/{contractAddress}/{address}")
    public ResultResponse getTokenBalance(@PathVariable(name = "contractAddress") String contractAddress,
                                          @PathVariable(name = "address") String address);

    @PostMapping("transferEth")
    public  ResultResponse transferEth(@RequestParam(name = "toAddress") String toAddress,
                                       @RequestParam(name = "qty") BigDecimal qty);

    @PostMapping("transferToken")
    public  ResultResponse transferToken(@RequestParam(name = "toAddress") String toAddress,
                                         @RequestParam(name = "qty") BigDecimal qty,
                                         @RequestParam(name = "coinId") int coinId);

    @GetMapping("/txpool/{address}/blocking")
    public ResultResponse getTxpoolCoinBlocking(@PathVariable("address")String address);


    @PostMapping("/coverTransfer/{hash}/{coinId}")
    public ResultResponse coverBlockingTransfer(@PathVariable("hash") String hash,
                                                @PathVariable("coinId") Integer coinId);

    @PostMapping("/replaceTransfer/{withdrawId}")
    public ResultResponse replaceTransfer(@PathVariable("withdrawId")int withdrawId);

    @GetMapping("/syncing")
    public ResultResponse ethSyncing();


    @PostMapping("/test/transferEth")
    public ResultResponse transferEthTest(
            @RequestParam(name = "fromAddress") String fromAddress,
            @RequestParam(name = "toAddress") String toAddress,
            @RequestParam(name = "qty") BigDecimal qty
    );

    @PostMapping("/test/transferToken")
    public ResultResponse transferTokenTest(@RequestParam(name = "fromAddress") String fromAddress,
                                            @RequestParam(name = "toAddress") String toAddress,
                                            @RequestParam(name = "qty") BigDecimal qty,
                                            @RequestParam(name = "contractAddress") String contractAddress);


}
