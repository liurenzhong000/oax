package com.oax.mapper.activity;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.oax.entity.activity.SnatchDetail;
import com.oax.entity.front.vo.SnatchDetailAggregateVo;
import com.oax.entity.front.vo.SnatchDetailVo;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @Auther: hyp
 * @Date: 2019/1/16 18:54
 * @Description:
 */
public interface SnatchDetailMapper extends BaseMapper<SnatchDetail> {

    BigDecimal getBettedQty(@Param("userId") Integer userId, @Param("snatchActivityId") Integer snatchActivityId);

    Integer getBettedUnits(@Param("userId") Integer userId, @Param("snatchActivityId") Integer snatchActivityId);

    BigDecimal getPayoutQty(@Param("userId") Integer userId, @Param("snatchActivityId") Integer snatchActivityId);

    Integer getWinCount(@Param("userId") Integer userId, @Param("snatchActivityId") Integer snatchActivityId);

    int updateAllNotWin(Integer snatchActivityId);

    int updateWin(@Param("snatchActivityId") Integer snatchActivityId, @Param("winHashMapList") List<Map<String, Object>> winHashMapList, @Param("payoutQty") BigDecimal payoutQty);

    List<SnatchDetail> winSnatchDetails(Integer activityId);

    List<SnatchDetail> listByActivityIdAndNumber(@Param("snatchActivityId") Integer snatchActivityId, @Param("winNumbers") List<Integer> winNumbers);

    int updateWinOne(@Param("snatchActivityId") Integer snatchActivityId, @Param("number") Integer number,
                     @Param("hash") String hash, @Param("payoutQty") BigDecimal payoutQty, @Param("chargesQty")  BigDecimal chargesQty);

    List<SnatchDetailVo> listVoByUserIdAndActivityId(@Param("userId") Integer userId, @Param("activityId")  Integer activityId);

    List<SnatchDetailAggregateVo> listAggregateVoByActivityIds(@Param("activityIds") List<Integer> activityIds);

    List<SnatchDetailVo> listWinVo(@Param("userId") Integer userId, @Param("activityId")  Integer activityId);

    List<Integer> listBetActivityId(@Param("userId") Integer userId, @Param("configId") Integer configId);

    String getFirstNumberStr(@Param("userId") Integer userId, @Param("activityId") Integer activityId);

    List<Integer> listRobotNumber(Integer activityId);

    BigDecimal sumBetQty(Integer coinId);

    BigDecimal sumChargesQty(Integer coinId);

    BigDecimal sumRobotPayout(@Param("userId") Integer userId,@Param("coinId")  Integer coinId);
}
