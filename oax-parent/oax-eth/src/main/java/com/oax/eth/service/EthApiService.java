package com.oax.eth.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.oax.eth.exception.MyException;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/6/21
 * Time: 15:21
 * eht操作 service
 */
public interface EthApiService {
    /**
     * ETH转账
     *
     * @param toAddress
     * @param qty
     * @return 交易hash
     */
    Map<String, Object> transferEth(String toAddress, BigDecimal qty) throws MyException;

    /**
     * ETHToken转账
     *
     * @param toAddress
     * @param qty
     * @param coinId
     * @return
     */
    Map<String, Object> transferToken(String toAddress, BigDecimal qty, int coinId) throws MyException;

    List<String> getTxpoolCoinBlocking(String address);

    Optional<Map<String, String>> selectTxPoolByHash(String hash) throws MyException;

    Map<String, Object> coverTransferByTx(Map<String, String> tx, Integer coinId) throws MyException;

    Map<String,Object> replaceTransfer(int withdrawId);
}
