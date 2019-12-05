package com.oax.service;

import com.github.pagehelper.PageInfo;
import com.oax.entity.front.RechargeResult;
import com.oax.vo.RechargeListVO;

public interface IRechargeService {
    /*
     * 根据用户id查询该用户资产下的充值所有记录
     */
	PageInfo<RechargeResult> getByUserIdRecharge(RechargeListVO vo);

}
