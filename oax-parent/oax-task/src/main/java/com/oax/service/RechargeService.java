package com.oax.service;

import java.util.Date;
import java.util.List;

import com.oax.entity.front.Recharge;
import com.oax.entity.front.vo.RechargeSumVo;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/7/3
 * Time: 17:54
 * 用户充值 service
 */
public interface RechargeService {
    List<Recharge> selectByCoinIdAndTime(Integer coinId, Date startTime, Date endTime);

    List<RechargeSumVo> selectSumVoByCoinIdAndTime(Integer coinId, Date startTime, Date endTime);

    int insert(Recharge recharge);

    int insertIgnore(Recharge recharge);

    int insertIgnoreAndAddUserBalance(Recharge recharge);
}
