package com.oax.mapper.front;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.oax.entity.front.vo.WithdrawSumVo;
import org.apache.ibatis.annotations.Param;

import com.oax.entity.admin.param.RechargesOrWithdrawParam;
import com.oax.entity.admin.param.SimpleCoinParam;
import com.oax.entity.admin.param.WithdrawPageParam;
import com.oax.entity.admin.vo.WithdrawFeeVo;
import com.oax.entity.admin.vo.WithdrawPageVo;
import com.oax.entity.admin.vo.WithdrawSums;
import com.oax.entity.admin.vo.WithdrawsVo;
import com.oax.entity.front.Withdraw;
import com.oax.entity.front.WithdrawResult;

public interface WithdrawMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Withdraw record);

    int insertSelective(Withdraw record);

    Withdraw selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Withdraw record);

    int updateByPrimaryKey(Withdraw record);

    List<Withdraw> getByUserIdWithdraw(Integer userId);

    List<WithdrawsVo> getByUserIdQueryWithdraw(RechargesOrWithdrawParam withdrawsParam);

    List<WithdrawResult> selectByUserId(Map<String, Object> map);

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
     * 根据参数 获取转出记录
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
    List<WithdrawPageVo> getByWithdrawPageParam(WithdrawPageParam withdrawPageParam);

    /**
     * 根据状态查询 所有转出订单
     *
     * @param inTxpoolStatus
     * @return
     */
    List<Withdraw> selectByStatus(byte inTxpoolStatus);

    Withdraw selectByHash(String hash);

    List<Withdraw> selectByCoinIdAndStatus(@Param("coinId") Integer coinId,
                                           @Param("status") byte status);

    List<Withdraw> selectOutByCoinIdAndTime(@Param("coinId") Integer coinId, @Param("startTime") Date startTime, @Param("endTime") Date endTime);

	BigDecimal queryUseredWithdraw(Integer userId);

    List<WithdrawFeeVo> sumWithdrawFee(SimpleCoinParam simpleCoinParam);

    List<WithdrawSumVo> selectSumVoOutByCoinIdAndTime(@Param("coinId") Integer coinId, @Param("startTime") Date startTime, @Param("endTime") Date endTime);
}

