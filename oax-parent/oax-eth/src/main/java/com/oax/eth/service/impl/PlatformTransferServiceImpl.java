package com.oax.eth.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oax.entity.front.PlatformTransfer;
import com.oax.eth.service.PlatformTransferService;
import com.oax.mapper.front.PlatformTransferMapper;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/6/21
 * Time: 17:37
 */
@Service
public class PlatformTransferServiceImpl implements PlatformTransferService {
    @Autowired
    private PlatformTransferMapper platformTransferMapper;

    @Override
    public int insert(PlatformTransfer platformTransfer) {
        return platformTransferMapper.insertSelective(platformTransfer);
    }

    @Override
    public List<PlatformTransfer> selectNotVerifyByAddressAndType(String address) {
        return platformTransferMapper.selectNotVerifyByAddressAndType(address);
    }

    @Override
    public List<PlatformTransfer> selectByType(int type) {
        return platformTransferMapper.selectByType(type);
    }

    @Override
    public List<PlatformTransfer> selectByTypeAndStatus(Integer type, Integer status) {
        return platformTransferMapper.selectByTypeAndStatus(type, status);
    }

    @Override
    public int update(PlatformTransfer platformTransfer) {
        return platformTransferMapper.updateByPrimaryKeySelective(platformTransfer);
    }

    @Override
    public PlatformTransfer selectByHash(String hash) {

        return platformTransferMapper.selectByHash(hash);
    }
}
