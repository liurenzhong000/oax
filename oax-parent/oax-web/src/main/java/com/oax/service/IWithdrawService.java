package com.oax.service;

import com.github.pagehelper.PageInfo;
import com.oax.entity.front.WithdrawResult;
import com.oax.vo.WithdrawListVO;

public interface IWithdrawService {
    /*
    * 根据用户id查出该用户 该用户资产下的提现所有记录
    */
	PageInfo<WithdrawResult> getByUserIdWithdraw(WithdrawListVO withdrawsParam);



}
