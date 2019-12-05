package com.oax.mapper.front;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.oax.entity.admin.param.ShareBonusParam;
import com.oax.entity.admin.vo.FeedBackVo;
import com.oax.entity.front.BenchMarkShareBonus;
import com.oax.entity.front.ShareBonusInfo;
import com.oax.entity.front.UserCoin;

@Mapper
public interface BenchMarkShareBonusMapper {

    List<BenchMarkShareBonus> getBenchMarkShareBonusList(@Param("beginTime") String beginTime, @Param("endTime")String endTime);

    List<UserCoin> getUserCoinXThreshold(Integer banlance);

    List<UserCoin> getUserCoinXNonThreshold();

    Integer insert(BenchMarkShareBonus model);

    List<BenchMarkShareBonus> getTradeListXThreshold(@Param("beginTime") String beginTime, @Param("endTime")String endTime,@Param("banlance") Integer banlance);

    List<BenchMarkShareBonus> getTradeListXNonThreshold(@Param("beginTime") String beginTime, @Param("endTime")String endTime);

    List<ShareBonusInfo> getShareBonusInfoList(@Param("toDay") String toDay, @Param("nextDay")String nextDay,@Param("lastFriday") String lastFriday,@Param("currentFriday") String currentFriday, @Param("currentMonday")String currentMonday, @Param("nextMonday")String nextMonday);

    List<Map<String,Object>> getShareBonus(ShareBonusParam param);

    List<Map<String,Object>> getMyShareBonus(@Param("userId") Integer userId, @Param("beginTime")String beginTime, @Param("endTime")String endTime);

    List<Map<String,Object>> ShareBonusIndex(FeedBackVo vo);

    List<Map<String,Object>> getPage(ShareBonusParam param);

    Integer batchSaveTradeRedman(List<BenchMarkShareBonus> benchMarkShareBonusList);

    List<Map<String,Object>> getMarKetShareBonusList(@Param("beginTime")String beginTime, @Param("endTime")String endTime);
}
