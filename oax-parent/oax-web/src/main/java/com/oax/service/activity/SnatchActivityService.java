package com.oax.service.activity;

import com.oax.vo.SnatchActivityHomeVo;
import com.oax.vo.SnatchBetNumberVo;
import com.oax.vo.SnatchDetailPageVo;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @Auther: hyp
 * @Date: 2019/1/16 19:18
 * @Description:
 */
public interface SnatchActivityService {

    //一元夺宝首页数据
    SnatchActivityHomeVo index(Integer coinId, Integer userId);

    void robotBet(Integer id);

    //用户下注
    Map<String, Object> bet(Integer id, Integer userId, BigDecimal betQty,Boolean robot);

    //查看某一期的投注记录
    SnatchBetNumberVo listMyDetail(Integer userId, Integer activityId);

    SnatchDetailPageVo detailPage(Integer userId);

    List<String> listNewlyLottery();
}
