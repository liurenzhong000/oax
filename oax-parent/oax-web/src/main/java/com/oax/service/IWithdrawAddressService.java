package com.oax.service;

import com.github.pagehelper.PageInfo;
import com.oax.entity.front.WithdrawAddress;
import com.oax.vo.WithdrawAddressListVO;

public interface IWithdrawAddressService {
	/**
     * 提币管理地址: 地址列表 根据用户id查询
     */
	PageInfo<WithdrawAddress> list(WithdrawAddressListVO vo);
    
    /**
     * 提币管理地址: 添加按钮 保存操作
     */
    int save(WithdrawAddress vo);

	int deleteById(Integer id);

	int updateById(WithdrawAddress vo);
}
