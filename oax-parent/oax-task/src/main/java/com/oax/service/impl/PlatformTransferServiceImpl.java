package com.oax.service.impl;

import java.util.Date;
import java.util.List;

import com.oax.entity.front.vo.PlatformTransferSumVo;
import com.oax.entity.front.vo.RechargeSumVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oax.entity.front.PlatformTransfer;
import com.oax.mapper.front.PlatformTransferMapper;
import com.oax.service.PlatformTransferService;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/7/3
 * Time: 18:18
 */
@Service
public class PlatformTransferServiceImpl implements PlatformTransferService {
    @Autowired
    private PlatformTransferMapper platformTransferMapper;

    @Override
    public List<PlatformTransfer> selectByCoinIdAndTime(Integer coinId, Date startTime, Date endTime) {
        return platformTransferMapper.selectByCoinIdAndTime(coinId, startTime, endTime);
    }

    @Override
    public List<PlatformTransferSumVo> selectSumVoByCoinIdAndTime(Integer coinId, Date startTime, Date endTime) {
        return platformTransferMapper.selectSumVoByCoinIdAndTime(coinId, startTime, endTime);
    }
}
