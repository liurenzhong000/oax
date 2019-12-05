package com.oax.eth.service.impl;

import static com.oax.eth.utils.EthRPCApiUtils.dbNum2EthNum;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.oax.common.Base64Utils;
import com.oax.common.enums.CoinTypeEnum;
import com.oax.entity.front.Coin;
import com.oax.entity.front.Withdraw;
import com.oax.eth.exception.MyException;
import com.oax.eth.service.CoinService;
import com.oax.eth.service.EthApiService;
import com.oax.eth.service.WithdrawService;
import com.oax.eth.utils.EthRPCApiUtils;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/6/21
 * Time: 15:22
 */
@Service
public class EthApiServiceImpl implements EthApiService {

    @Autowired
    private CoinService coinService;

    @Autowired
    private WithdrawService withdrawService;

    @Autowired
    private EthRPCApiUtils ethRPCApiUtils;

    @Override
    public Map<String, Object> transferEth(String toAddress, BigDecimal qty) throws MyException {

        List<Coin> coins = coinService.selectByType(CoinTypeEnum.ETH.getType());
        Coin ethCoin = coins.get(0);

        String mainAddress = ethCoin.getMainAddress();

        String ethHexQty = dbNum2EthNum(qty,ethCoin.getDecimals());
        String password = Base64Utils.getFromBase64(Base64Utils.getFromBase64(ethCoin.getMainAddressPassword())) ;

        try {
            Map<String, Object> params = ethRPCApiUtils.transferETH(
                    mainAddress,
                    toAddress,
                    ethHexQty,
                    password);
            params.put("gas",EthRPCApiUtils.toNum((String) params.get("gas")).intValue());
            params.put("gasPrice",EthRPCApiUtils.ethGasPrice2DbGasPrice((String)params.get("gasPrice")));
            return params;
        } catch (Exception e) {
            e.printStackTrace();
            throw new MyException("转账eth失败:"+e.getMessage());
        }

    }

    @Override
    public Map<String, Object> transferToken(String toAddress, BigDecimal qty, int coinId) throws MyException {

        Coin tokenCoin = coinService.selectById(coinId);

        String mainAddress = tokenCoin.getMainAddress();

        String ethHexQty = dbNum2EthNum(qty,tokenCoin.getDecimals());

        String contractAddress = tokenCoin.getContractAddress();
        String methodId = tokenCoin.getMethodId();
        String password = Base64Utils.getFromBase64(Base64Utils.getFromBase64(tokenCoin.getMainAddressPassword())) ;

        try {
            Map<String, Object> params = ethRPCApiUtils.transferToken(
                    contractAddress,
                    methodId,
                    mainAddress,
                    toAddress,
                    ethHexQty,
                    password);

            params.put("gas",EthRPCApiUtils.toNum((String) params.get("gas")).intValue());
            params.put("gasPrice",EthRPCApiUtils.ethGasPrice2DbGasPrice((String)params.get("gasPrice")));
            return params;
        } catch (Exception e) {
            e.printStackTrace();
            throw new MyException("转账token失败:"+e.getMessage());
        }
    }

    @Override
    public List<String> getTxpoolCoinBlocking(String address) {


        ArrayList<Map<String, String>> allTxpoolBlockings = ethRPCApiUtils.getAllTxpoolBlockings();
        if (CollectionUtils.isEmpty(allTxpoolBlockings)) {
            return null;
        }
        return  allTxpoolBlockings.stream()
                .filter(tx -> StringUtils.equalsIgnoreCase(tx.get("from"), address))
                .map(tx -> tx.get("hash"))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Map<String, String>> selectTxPoolByHash(String hash) throws MyException {
        ArrayList<Map<String, String>> allTxpoolBlockings = ethRPCApiUtils.getAllTxpoolBlockings();
        if (CollectionUtils.isEmpty(allTxpoolBlockings)) {
            throw new MyException("ETH请求txpool失败");
        }

        return allTxpoolBlockings.stream()
                .filter(map -> StringUtils.equalsIgnoreCase(map.get("hash"), hash))
                .findFirst();
    }

    @Override
    public Map<String, Object> coverTransferByTx(Map<String, String> tx, Integer coinId) throws MyException {

        Coin coin = coinService.selectById(coinId);


        String from = tx.get("from");
        String to = tx.get("to");
        String qty = tx.get("value");
        String nonce = tx.get("nonce");
        String input = tx.get("input");
        String oldgasPrice = tx.get("gasPrice");
        String password = Base64Utils.getFromBase64(Base64Utils.getFromBase64(coin.getMainAddressPassword())) ;

        try {
            Map<String, Object> params = ethRPCApiUtils.coverTransferETH(
                    from,
                    to,
                    qty,
                    password,
                    nonce,
                    input,
                    oldgasPrice);

            params.put("gas",EthRPCApiUtils.toNum((String) params.get("gas")).intValue());
            params.put("gasPrice",EthRPCApiUtils.ethGasPrice2DbGasPrice((String)params.get("gasPrice")));
            return params;
        } catch (Exception e) {
            e.printStackTrace();
            throw new MyException("覆盖交易失败,hash:" + tx.get("hash"));
        }
    }

    @Override
    public Map<String, Object> replaceTransfer(int withdrawId)  throws MyException{

        Withdraw withdraw = withdrawService.selectById(withdrawId);

        try {
            Map<String, Object> transaction = ethRPCApiUtils.getTransactionByHash(withdraw.getTxId());

            Coin coin = coinService.selectById(withdraw.getCoinId());

            String passowrd = Base64Utils.getFromBase64(Base64Utils.getFromBase64(coin.getMainAddressPassword()));

            JSONObject jsonObject = new JSONObject(transaction);
            String from = jsonObject.getString("from");
            String to = jsonObject.getString("to");
            String gas = jsonObject.getString("gas");
            String gasPrice = jsonObject.getString("gasPrice");
            String input = jsonObject.getString("input");
            String value = jsonObject.getString("value");
            String oldgasPrice = jsonObject.getString("gasPrice");

            Map<String, Object> params = ethRPCApiUtils.coverTransferETH(
                    from,
                    to,
                    value,
                    passowrd,
                    null,
                    input,
                    oldgasPrice
            );

            params.put("gas",EthRPCApiUtils.toNum(gas).intValue());
            params.put("gasPrice",EthRPCApiUtils.ethGasPrice2DbGasPrice(gasPrice));
            return params;
        } catch (Exception e) {
            e.printStackTrace();
            throw new MyException("补单失败:"+e.getMessage());
        }
    }
}
