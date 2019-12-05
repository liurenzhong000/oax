package com.oax.service;

import java.util.Date;
import java.util.List;

import com.oax.entity.front.PlatformTransfer;
import com.oax.entity.front.vo.PlatformTransferSumVo;
import com.oax.entity.front.vo.RechargeSumVo;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/7/3
 * Time: 18:18
 */
public interface PlatformTransferService {
    List<PlatformTransfer> selectByCoinIdAndTime(Integer coinId, Date startTime, Date endTime);

    List<PlatformTransferSumVo> selectSumVoByCoinIdAndTime(Integer coinId, Date startTime, Date endTime);
}
