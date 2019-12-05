package com.oax.service;

import java.util.Date;
import java.util.List;

import com.oax.entity.front.Withdraw;
import com.oax.entity.front.vo.WithdrawSumVo;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/7/3
 * Time: 17:53
 *
 * 用户提现 service
 */
public interface WithdrawService {
    List<Withdraw> selectOutByCoinIdAndTime(Integer coinId, Date startTime, Date endTime);

    List<WithdrawSumVo> selectSumVoOutByCoinIdAndTime(Integer coinId, Date startTime, Date endTime);
}
