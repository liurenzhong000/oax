package com.oax.scheduled;

import com.oax.common.RedisUtil;
import com.oax.common.constant.RedisKeyConstant;
import com.oax.entity.front.UserCoin;
import com.oax.entity.front.UserCoinSnapshootLight;
import com.oax.mapper.front.UserCoinMapper;
import com.oax.mapper.front.UserCoinSnapshootLightMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.*;
import java.util.Date;
import java.util.List;

/**
 * 用户资金快照
 */
@Component
@Slf4j
public class UserCoinSnapshootLightScheduled {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private UserCoinSnapshootLightMapper userCoinSnapshootLightMapper;

    @Autowired
    private UserCoinMapper userCoinMapper;

    /**每小时一单*/
    @Scheduled(cron="0 0 0/1 * * ?")
    @Async
    public void createUserCoinSnapshoot(){
        log.info("===================用户BHB资金轻量级快照=====================");
        List<UserCoin> hasBHBUserCoins = userCoinMapper.selectHasRechargeOrCtcByCoinId(54);//54是BHB，查询有过充值或法币的拥有BHB的用户记录
        //判断当前时间是否在19:30-20:30之间，如果是，保存余额记录到redis
        boolean saveBalanceToRedis = isSaveBalanceToRedis();
        hasBHBUserCoins.forEach(item->{
            BigDecimal bonusBalance = getBonusBalance(item.getUserId(), item.getBanlance());
            UserCoinSnapshootLight snapshoot = new UserCoinSnapshootLight();
            snapshoot.setUserId(item.getUserId());
            snapshoot.setBalance(item.getBanlance());
            snapshoot.setFreezingBalance(item.getFreezingBanlance());
            snapshoot.setCoinId(item.getCoinId());
            snapshoot.setCreateTime(new Date());
            snapshoot.setBonusBalance(bonusBalance);
            userCoinSnapshootLightMapper.insert(snapshoot);

            if (saveBalanceToRedis) {
                saveBalanceToRedis(item.getUserId(), item.getBanlance());
            }
        });
        log.info("===================用户BHB资金轻量级快照  结束=====================");
    }

    private boolean isSaveBalanceToRedis(){
        long timestamp = System.currentTimeMillis();
        if (timestamp > getStartTime().getTime() && timestamp < getEndTime().getTime()) {
            return true;
        }
        return false;
    }

    private void saveBalanceToRedis(Integer userId, BigDecimal balance){
        redisUtil.setObject(RedisKeyConstant.BONUS_SNAPSHOOT_LOG_KEY + userId, balance, 84600);//23小时30分钟
    }

    private BigDecimal getBonusBalance(Integer userId, BigDecimal balance) {
        BigDecimal bonusBalance = redisUtil.getObject(RedisKeyConstant.BONUS_SNAPSHOOT_LOG_KEY + userId, BigDecimal.class);
        if (bonusBalance != null && bonusBalance.compareTo(balance) < 0){
            return bonusBalance;
        }
        return balance;
    }

    /**
     * 今天晚上19:30
     */
    public static Date getStartTime(){
        ZoneId zoneId = ZoneId.systemDefault();
        LocalTime midnight = LocalTime.MIDNIGHT;
        LocalDate today = LocalDate.now();
        LocalDateTime todayMidnight = LocalDateTime.of(today, midnight);
        LocalDateTime yesterdayMidnight = todayMidnight.plusHours(19).plusMinutes(30);
        ZonedDateTime zdt = yesterdayMidnight.atZone(zoneId);
        return Date.from(zdt.toInstant());
    }

    /**
     * 今天晚上20:30
     */
    public static Date getEndTime(){
        ZoneId zoneId = ZoneId.systemDefault();
        LocalTime midnight = LocalTime.MIDNIGHT;
        LocalDate today = LocalDate.now();
        LocalDateTime todayMidnight = LocalDateTime.of(today, midnight);
        LocalDateTime yesterdayMidnight = todayMidnight.plusHours(20).plusMinutes(30);
        ZonedDateTime zdt = yesterdayMidnight.atZone(zoneId);
        return Date.from(zdt.toInstant());
    }

}
