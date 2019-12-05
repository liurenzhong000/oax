package com.oax.mapper.front;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.oax.entity.front.vo.RechargeSumVo;
import org.apache.ibatis.annotations.Param;

import com.oax.entity.admin.param.RechargeParam;
import com.oax.entity.admin.param.RechargesOrWithdrawParam;
import com.oax.entity.admin.vo.RechargePageVo;
import com.oax.entity.admin.vo.RechargesVo;
import com.oax.entity.front.Recharge;
import com.oax.entity.front.RechargeResult;

public interface RechargeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Recharge record);

    int insertSelective(Recharge record);

    Recharge selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Recharge record);

    int updateByPrimaryKey(Recharge record);

    List<Recharge> getByUserIdRecharge(Integer userId);

    List<RechargesVo> getByUserIdQueryRecharge(RechargesOrWithdrawParam rechargesOrWithdrawParam);

    /**
     * 根据 参数获取 转入记录
     *
     * @param rechargeParam (可选)
     *                      pageNo
     *                      pageSize
     *                      startTime
     *                      endTime
     *                      username
     *                      userId
     *                      coinId
     * @return
     */
    List<RechargePageVo> selectByRechargeParam(RechargeParam rechargeParam);

    /**
     * 根据币种id 获取对应所有转入额
     *
     * @param coinId
     * @return
     */
    BigDecimal countTotalQtyByCoinId(int coinId);

    List<RechargeResult> getByUserId(RechargesOrWithdrawParam rechargesOrWithdrawParam);

    /**
     * 根据 toAddress(我们平台地址查询)
     * @param address
     * @return
     */
    List<Recharge> selectByToAddress(String address);

    List<Recharge> selectByCoinIdAndTime(@Param("coinId") Integer coinId, @Param("startTime") Date startTime, @Param("endTime") Date endTime);

    int insertIgnore(Recharge recharge);

    List<Recharge> selectAll();

    List<RechargeSumVo> selectSumVoByCoinIdAndTime(@Param("coinId") Integer coinId, @Param("startTime") Date startTime, @Param("endTime") Date endTime);
}