package com.oax.eth.service;

import java.util.List;

import com.oax.entity.front.PlatformTransfer;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/6/21
 * Time: 17:37
 * 平台转账 账单service
 */
public interface PlatformTransferService {
    int insert(PlatformTransfer platformTransfer);

    List<PlatformTransfer> selectNotVerifyByAddressAndType(String address);

    List<PlatformTransfer> selectByType(int type);

    List<PlatformTransfer> selectByTypeAndStatus(Integer type, Integer status);

    int update(PlatformTransfer platformTransfer);

    PlatformTransfer selectByHash(String hash);
}
