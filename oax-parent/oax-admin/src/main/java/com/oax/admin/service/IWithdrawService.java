package com.oax.admin.service;

import com.github.pagehelper.PageInfo;
import com.oax.entity.admin.param.RechargesOrWithdrawParam;
import com.oax.entity.admin.param.SimpleCoinParam;
import com.oax.entity.admin.param.WithdrawPageParam;
import com.oax.entity.admin.vo.WithdrawFeeVo;
import com.oax.entity.admin.vo.WithdrawPageVo;
import com.oax.entity.admin.vo.WithdrawSums;
import com.oax.entity.admin.vo.WithdrawsVo;
import com.oax.entity.front.Withdraw;

import java.util.List;

public interface IWithdrawService {
    /**
     * 详情: 转出记录 ->  分页 高级查询
     */
    PageInfo<WithdrawsVo> getByUserIdQueryWithdraw(RechargesOrWithdrawParam withdrawsParam);

    /**
     * 计算所有费用总数
     *
     * @param withdrawPageParam userId          用户id
     *                          coinId          币种id
     *                          username        用户名(模糊查询)
     *                          withdrawStatus  状态 -2:终审不通过 -1：审核不通过 0：待审核 1:待终审 2：已转出 3：已广播 4：已确认
     *                          pageNo          页码
     *                          pageSize        一页展示数
     *                          startTime       开始时间
     *                          endTime         结束时间
     * @return
     */
    WithdrawSums countWithdrawSumsByParam(WithdrawPageParam withdrawPageParam);

    /**
     * 根据参数 获取转出记录 分页
     *
     * @param withdrawPageParam userId          用户id
     *                          coinId          币种id
     *                          username        用户名(模糊查询)
     *                          withdrawStatus  状态 -2:终审不通过 -1：审核不通过 0：待审核 1:待终审 2：已转出 3：已广播 4：已确认
     *                          pageNo          页码
     *                          pageSize        一页展示数
     *                          startTime       开始时间
     *                          endTime         结束时间
     * @return
     */
    PageInfo<WithdrawPageVo> getByWithdrawPageParam(WithdrawPageParam withdrawPageParam);

    Withdraw selectById(int withdrawId);

    int update(Withdraw withdraw);

    /**
     * 审核 更新状态 并记录日志
     *
     * @param withdraw
     * @param description 日志描述
     * @return
     */
    int updateAndInsertLog(Withdraw withdraw, String description);

    Withdraw selectByHash(String hash);

    PageInfo<WithdrawFeeVo> sumWithdrawFee(SimpleCoinParam simpleCoinParam);

    void blockWithdraw(List<Integer> withdrawIds, byte passStatus, String remark);
}
