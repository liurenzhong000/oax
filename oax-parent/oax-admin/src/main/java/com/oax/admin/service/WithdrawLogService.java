package com.oax.admin.service;

import java.util.List;

import com.oax.entity.admin.WithdrawLog;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/6/14
 * Time: 15:09
 * 转出 后台人员操作日志 service
 */
public interface WithdrawLogService {
    int insert(WithdrawLog withdrawLog);

    /**
     * 根据 交易记录id 获取 交易记录操作日志
     *
     * @param withdrawId
     * @return
     */
    List<WithdrawLog> selectByWithdrawById(int withdrawId);
}
