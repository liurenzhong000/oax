package com.oax.eth.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.oax.common.ResultResponse;
import com.oax.entity.front.Coin;
import com.oax.eth.exception.MyException;
import com.oax.eth.service.CoinService;
import com.oax.eth.service.EthApiService;
import com.oax.eth.utils.EthRPCApiUtils;

@RestController
@RequestMapping("/api")
public class APIController {

    @Autowired
    private EthApiService ethApiService;

    @Autowired
    private CoinService coinService;

    @Autowired
    private EthRPCApiUtils ethRPCApiUtils;


    /**
     * 创建一个地址
     *
     * @param password 密码
     * @return
     */
    @PostMapping("createNewAddress")
    public ResultResponse newAccount(@RequestParam(name = "password") String password) {
        try {
            String newAccount = ethRPCApiUtils.newAccount(password);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("newAccount", newAccount);
            return new ResultResponse(true, jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultResponse(false, "生成用户失败");
        }
    }


    /**
     * 获取eth余额
     *
     * @param address eth地址
     * @return
     */
    @GetMapping("getEthBalance/{address}")
    public ResultResponse getEthBalance(@PathVariable(name = "address") String address) {
        try {
            String ethBalance = ethRPCApiUtils.getETHBalance(address);
            BigDecimal bigDecimal = EthRPCApiUtils.stringToDBNum(ethBalance);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("balance", bigDecimal);
            return new ResultResponse(true, jsonObject);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return new ResultResponse(false, "地址:" + address + ",获取余额失败");
        }
    }


    /**
     * 获取代币余额
     *
     * @param contractAddress 代币合约地址
     * @param address         地址
     * @return
     */
    @GetMapping("getTokenBalance/{contractAddress}/{address}")
    public ResultResponse getTokenBalance(@PathVariable(name = "contractAddress") String contractAddress,
                                          @PathVariable(name = "address") String address) {

        try {
            String tokenBalance = ethRPCApiUtils.getTokenBalance(address, contractAddress);
            Coin coin = coinService.selectByContractAddress(contractAddress);
            if (coin == null) {
                return new ResultResponse(false, "合约地址不存在数据库");
            }
            BigDecimal bigDecimal = EthRPCApiUtils.stringToDBNum(tokenBalance, coin.getDecimals());
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("balance", bigDecimal);
            return new ResultResponse(true, jsonObject);

        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return new ResultResponse(false, "地址:" + address + ",获取余额失败");
        }
    }


    /**
     * eth转账
     *
     * @param toAddress 转出地址
     * @param qty       转出金额
     * @return
     */
    @PostMapping("transferEth")
    public ResultResponse transferEth(@RequestParam(name = "toAddress") String toAddress,
                                      @RequestParam(name = "qty") BigDecimal qty) {
        try {
            Map<String, Object> transferHash = ethApiService.transferEth(toAddress, qty);
            return new ResultResponse(true, transferHash);
        } catch (MyException e) {
            return new ResultResponse(false, e.getMessage());
        }
    }


    /**
     * 代币转账
     *
     * @param toAddress
     * @param qty
     * @param coinId
     * @return
     */
    @PostMapping("transferToken")
    public ResultResponse transferToken(@RequestParam(name = "toAddress") String toAddress,
                                        @RequestParam(name = "qty") BigDecimal qty,
                                        @RequestParam(name = "coinId") int coinId) {
        try {
            Map<String, Object> params = ethApiService.transferToken(toAddress, qty, coinId);
            return new ResultResponse(true, params);
        } catch (MyException e) {
            return new ResultResponse(false, e.getMessage());
        }
    }


    @GetMapping("/txpool/{address}/blocking")
    public ResultResponse getTxpoolCoinBlocking(@PathVariable("address") String address) {

        List<String> coinBlockingTxpoolVoList = ethApiService.getTxpoolCoinBlocking(address);

        if (coinBlockingTxpoolVoList == null) {
            return new ResultResponse(false, "交易池请求异常");
        }
        return new ResultResponse(true, coinBlockingTxpoolVoList);
    }

    @PostMapping("/coverTransfer/{hash}/{coinId}")
    public ResultResponse coverBlockingTransfer(@PathVariable("hash") String hash,
                                                @PathVariable("coinId") Integer coinId) {
        try {
            Optional<Map<String, String>> txInpoolOptional = ethApiService.selectTxPoolByHash(hash);
            if (!txInpoolOptional.isPresent()) {
                return new ResultResponse(false, "txpool中不存在hash:" + hash);
            }
            Map<String, String> tx = txInpoolOptional.get();


            Map<String, Object> params = ethApiService.coverTransferByTx(tx, coinId);
            return new ResultResponse(true, params);
        } catch (MyException e) {
            return new ResultResponse(false, e.getMessage());
        }
    }

    @PostMapping("/replaceTransfer/{withdrawId}")
    public ResultResponse replaceTransfer(@PathVariable("withdrawId") int withdrawId) {

        Map<String, Object> params = ethApiService.replaceTransfer(withdrawId);


        return new ResultResponse(true, params);

    }


    @GetMapping("/syncing")
    public ResultResponse ethSyncing() {

        try {
            String syncing = ethRPCApiUtils.ethSyncing();

            if (Boolean.FALSE.toString().equals(syncing)) {
                //说明没有同步区块
                return new ResultResponse(false, "未同步区块");
            } else {
                return new ResultResponse(true, "正在同步区块");
            }

        } catch (Exception e) {
            return new ResultResponse(false, e.getMessage());
        }
    }

    /**
     * eth转账 test
     *
     * @param toAddress 转出地址
     * @param qty       转出金额
     * @return
     */
    @PostMapping("test/transferEth")
    public ResultResponse transferEthTest(
            @RequestParam(name = "fromAddress") String fromAddress,
            @RequestParam(name = "toAddress") String toAddress,
            @RequestParam(name = "qty") BigDecimal qty
    ) {
        try {
            String ethHexQty = EthRPCApiUtils.dbNum2EthNum(qty,18);
            Map<String, Object> map = ethRPCApiUtils.transferETH(
                    fromAddress,
                    toAddress,
                    ethHexQty,
                    "123456");

            return new ResultResponse(true, map);
        } catch (MyException e) {
            return new ResultResponse(false, e.getMessage());
        } catch (Exception e) {
            return new ResultResponse(false, e.getMessage());
        }
    }

    /**
     * 代币转账
     *
     * @param fromAddress
     * @param toAddress
     * @param qty
     * @param contractAddress
     * @return
     */
    @PostMapping("test/transferToken")
    public ResultResponse transferTokenTest(@RequestParam(name = "fromAddress") String fromAddress,
                                            @RequestParam(name = "toAddress") String toAddress,
                                            @RequestParam(name = "qty") BigDecimal qty,
                                            @RequestParam(name = "contractAddress") String contractAddress) {
        try {
            String ethHexQty = EthRPCApiUtils.dbNum2EthNum(qty,18);
            Map<String, Object> transfer = ethRPCApiUtils.transfer(contractAddress, "0xa9059cbb", fromAddress, toAddress, ethHexQty, "123456");
            return new ResultResponse(true, transfer);
        } catch (MyException e) {
            return new ResultResponse(false, e.getMessage());
        } catch (Exception e) {
            return new ResultResponse(false,e.getMessage());
        }
    }

}
