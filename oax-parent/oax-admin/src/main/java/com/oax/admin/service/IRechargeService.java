package com.oax.admin.service;

import java.math.BigDecimal;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.oax.entity.admin.param.RechargeParam;
import com.oax.entity.admin.param.RechargesOrWithdrawParam;
import com.oax.entity.admin.vo.RechargePageVo;
import com.oax.entity.admin.vo.RechargesVo;

public interface IRechargeService {
    /**
     * 详情: 转入记录 ->  分页 高级查询
     */
    PageInfo<RechargesVo> getByUserIdQueryRecharge(RechargesOrWithdrawParam rechargesOrWithdrawParam);

    /**
     * 根据 参数获取 转入记录
     *
     * @param rechargeParam (可选)
     *                      pageNo
     *                      pageSize
     *                      startTime
     *                      endTime
     *                      userId
     *                      username
     *                      coinId
     * @return
     */
    PageInfo<RechargePageVo> getByRechargeParam(RechargeParam rechargeParam);

    /**
     * 根据币种id 获取对应所有转入额
     *
     * @param coinId
     * @return
     */
    BigDecimal countTotalQtyByCoinId(int coinId);

    /**
     * 员工福利
     * @param o
     *          用户id
     *          发币数
     *          coinId
     *          remark
     * @return
     */
    int employeeWelfare(Map o);
}
