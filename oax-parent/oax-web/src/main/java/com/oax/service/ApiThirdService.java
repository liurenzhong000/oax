package com.oax.service;

import java.math.BigDecimal;
import java.util.Map;

/**
 * @Auther: hyp
 * @Date: 2019/1/23 14:46
 * @Description:
 */
public interface ApiThirdService {
    //通过token验证用户是否登录
    Map<String, Object> checkByToken(Integer userId, String accessToken);

    //获取用户余额
    Map<String, Object> getBalanceByCoinId(Integer userId, Integer coinId, String accessToken);

    Map<String, Object> chargeBalance(Integer userId, Integer coinId, BigDecimal qty, String targetId, String accessToken);
}
