package com.oax.service.impl;

import com.oax.common.AccessTokenManager;
import com.oax.common.AssertHelper;
import com.oax.common.EmptyHelper;
import com.oax.common.RedisUtil;
import com.oax.common.util.string.VerificationHelper;
import com.oax.druid.DataSource;
import com.oax.druid.DataSourceType;
import com.oax.entity.enums.UserCoinDetailType;
import com.oax.entity.front.Member;
import com.oax.entity.front.UserCoin;
import com.oax.exception.NoLoginException;
import com.oax.mapper.front.MemberMapper;
import com.oax.service.ApiThirdService;
import com.oax.service.UserCoinService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: hyp
 * @Date: 2019/1/23 13:53
 * @Description: 提供给合作方的接口，爆点游戏
 */
@Service
@Transactional(isolation= Isolation.REPEATABLE_READ)
@Slf4j
public class ThirdServiceImpl implements ApiThirdService {

    @Autowired
    private UserCoinService userCoinService;

    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private AccessTokenManager tokenManager;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private ApplicationContext context;

    public String getActiveProfile() {
        return context.getEnvironment().getActiveProfiles()[0];
    }


    //通过token验证用户是否登录
    @Override
    @DataSource(DataSourceType.MASTER)
    public Map<String, Object> checkByToken(Integer userId, String accessToken){
        if (getActiveProfile().equals("prod")){
            AssertHelper.isTrue(false, "不支持");
        }
        validAccessToken(userId, accessToken);
        Member user = memberMapper.selectByPrimaryKey(userId);
        AssertHelper.notEmpty(user, "用户不存在");
        log.info("apiThird - checkByToken - userId={}", userId);
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("phoneOrEmail", VerificationHelper.hidePhoneAndEmail(memberMapper.getPhoneOrEmailById(userId)));
        return map;
    }

    //获取用户余额
    @Override
    @DataSource(DataSourceType.MASTER)
    public Map<String, Object> getBalanceByCoinId(Integer userId, Integer coinId, String accessToken) {
        if (getActiveProfile().equals("prod")){
            AssertHelper.isTrue(false, "不支持");
        }
        hasLogin(userId);
        Member user = memberMapper.selectByPrimaryKey(userId);
        AssertHelper.notEmpty(user, "用户不存在");
        UserCoin userCoin = userCoinService.selectAndInsert(userId, coinId);
        log.info("apiThird - getBalanceByCoinId - userId={} coinId={}", userId, coinId);
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userCoin.getUserId());
        map.put("balance", userCoin.getBanlance());
        return map;
    }

    //余额变更
    @Override
    @DataSource(DataSourceType.MASTER)
    public Map<String, Object> chargeBalance(Integer userId, Integer coinId, BigDecimal qty, String targetId, String accessToken) {
        if (getActiveProfile().equals("prod")){
            AssertHelper.isTrue(false, "不支持");
        }
        hasLogin(userId);
        UserCoin userCoin = userCoinService.selectAndInsert(userId, coinId);
        if (qty.compareTo(BigDecimal.ZERO)<0){
            AssertHelper.isTrue(userCoin.getBanlance().compareTo(qty.negate()) >=0, "账户余额不足");
        }
        userCoinService.addBalanceWithType(userId, coinId, qty, targetId, UserCoinDetailType.BLAST_POINT);
        log.info("apiThird - chargeBalance - userId={} coinId={} qty={}", userId, coinId, qty);
        return getBalanceByCoinId(userId, coinId, accessToken);
    }

    private void validAccessToken(Integer userId, String accessToken){
        boolean valid = tokenManager.check(userId, accessToken, 1);
        if (!valid) {
            throw new NoLoginException();
        }
    }

    //用户是登录状态
    private void hasLogin(Integer userId) {
        String key = "accessToken." + userId;
        String rightAccessToken = redisUtil.getString(key);
        if (EmptyHelper.isEmpty(rightAccessToken)) {
            throw new NoLoginException();
        }
    }

}
