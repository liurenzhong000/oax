package com.oax.mapper.admin;

import java.util.List;

import com.oax.entity.admin.WithdrawLog;

public interface WithdrawLogMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(WithdrawLog record);

    int insertSelective(WithdrawLog record);

    WithdrawLog selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(WithdrawLog record);

    int updateByPrimaryKeyWithBLOBs(WithdrawLog record);

    int updateByPrimaryKey(WithdrawLog record);

    List<WithdrawLog> selectByWithdrawById(int withdrawId);
}