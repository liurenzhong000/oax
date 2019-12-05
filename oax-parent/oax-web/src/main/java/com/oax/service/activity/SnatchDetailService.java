package com.oax.service.activity;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.oax.entity.activity.SnatchDetail;
import com.oax.entity.front.vo.SnatchDetailAggregateVo;
import com.oax.entity.front.vo.SnatchDetailVo;
import com.oax.form.SnatchAggregateForm;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @Auther: hyp
 * @Date: 2019/1/16 19:18
 * @Description:
 */
public interface SnatchDetailService extends IService<SnatchDetail> {
    BigDecimal getBettedQty(Integer userId, Integer snatchActivityId);

    Integer getBettedUnits(Integer userId, Integer snatchActivityId);

    BigDecimal getPayoutQty(Integer userId, Integer snatchActivityId);

    Integer getWinCount(Integer userId, Integer snatchActivityId);

    int updateAllNotWin(Integer snatchActivityId);

    int updateWin(Integer id, List<Map<String, Object>> winHashMapList, BigDecimal payoutQty);

    int updateWinOne(Integer snatchActivityId, Integer number, String hash, BigDecimal payoutQty, BigDecimal chargesQty);

    List<SnatchDetail> winSnatchDetails(Integer activityId);

    List<SnatchDetail> listByActivityIdAndNumber(Integer id, List<Integer> winNumbers);

    List<SnatchDetailVo> listVoByUserIdAndActivityId(Integer userId, Integer activityId);

    Page<SnatchDetailAggregateVo> listAggregateVoByUserIdAndConfigId(Integer userId, SnatchAggregateForm configId);

    String getFirstNumberStr(Integer userId, Integer activityId);

    //获取开奖编号
    List<SnatchDetailVo> listWinVo(Integer userId, Integer activityId);

    List<Integer> listRobotNumber(Integer activityId);
}
