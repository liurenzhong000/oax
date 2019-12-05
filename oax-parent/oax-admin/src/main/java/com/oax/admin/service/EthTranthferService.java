package com.oax.admin.service;

import com.oax.entity.front.Withdraw;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/7/4
 * Time: 20:54
 * ETH钱包 service
 */
public interface EthTranthferService {
    void coverBlockingTransfer(String hash);

    void replaceTransfer(Withdraw withdraw);
}
