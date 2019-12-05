package com.oax.admin.service;

import java.math.BigDecimal;
import java.util.List;

import com.oax.entity.enums.UserCoinDetailType;
import com.oax.entity.front.UserCoin;
import com.oax.entity.front.UserCoinInfo;
import com.oax.entity.front.vo.MemberCoinVo;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/6/27
 * Time: 14:37
 * 用户币种
 */
public interface UserCoinService {
    UserCoin selectByUserIdAndCoinId(Integer userId, Integer coinId);

    int update(UserCoin userCoin);

    BigDecimal countAllBanlanceByCoinId(Integer coinId);

    /**查询用户对应的币种余额记录，如果为空，插入数据*/
    UserCoin selectAndInsert(Integer userId, Integer coinId);

    List<UserCoinInfo> listUserCoinByUserId(Integer userId, String coinName);

    void addBalanceWithType(Integer userId, Integer coinId, BigDecimal qty, String targetId, UserCoinDetailType type);

    List<MemberCoinVo> listBCBMemberCoinVos();

}
