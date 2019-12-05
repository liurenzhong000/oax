package com.oax.scheduled;

import com.oax.common.EmptyHelper;
import com.oax.entity.front.UserCoinLeader;
import com.oax.mapper.front.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * BHB领导下级每天平均持仓统计
 */
@Component
@Slf4j
public class LeaderStatisticsScheduled {

    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private UserCoinLeaderMapper userCoinLeaderMapper;

    @Autowired
    private UserCoinSnapshootLightMapper userCoinSnapshootLightMapper;

    @Autowired
    private TradeMapper tradeMapper;

    @Autowired
    private UserCoinMapper userCoinMapper;

    /**每天统计一次*/
    @Scheduled(cron="0 0 0 1/1 * ?")
    public void leaderSubordinateStatistics(){
        log.info("====================BHB领导下级每天平均持仓统计====================");
        //所有需要统计的领导用户id
        List<Integer> leaderUserIds = memberMapper.getIdsByType(3);//3表示BHB领导人
        Date dataTime = getDataTime();
        Date startTime = getDataTimeStart();
        Date endTime = getDataTimeEnd();
        BigDecimal usdtPrice = getUsdtPrice(startTime, endTime);
        leaderUserIds.forEach(leaderUserId ->{
            //获取一级用户的id
            List<Integer> oneLevelAllUserIds = getChildUserIds(Collections.singletonList(leaderUserId));
            //获取二级用户id
            List<Integer> twoLevelAllUserIds = getChildUserIds(oneLevelAllUserIds);
            //获取三级用户id
            List<Integer> threeLevelAllUserIds = getChildUserIds(twoLevelAllUserIds);

            List<Integer> otherLevelAllUserIds = getOtherChildUserIds(threeLevelAllUserIds);


            //获取一级用户的平均持仓和
            BigDecimal oneLevelSum = getSum(startTime, endTime, oneLevelAllUserIds);
            //获取二级用户的平均持仓和
            BigDecimal twoLevelSum = getSum(startTime, endTime, twoLevelAllUserIds);
            //获取三级用户的平均持仓和
            BigDecimal threeLevelSum = getSum(startTime, endTime, threeLevelAllUserIds);

            BigDecimal otherLevelSum = getSum(startTime, endTime, otherLevelAllUserIds);

            BigDecimal allSum = oneLevelSum.add(twoLevelSum).add(threeLevelSum).add(otherLevelSum);
            saveUserCoinLeader(leaderUserId, dataTime, oneLevelSum, twoLevelSum, threeLevelSum, usdtPrice, allSum);
            log.info("业绩统计：user_id:{}, allLevelSUm:{}", leaderUserId, allSum);
        });

    }

    private BigDecimal getUsdtPrice(Date startTime, Date endTime){
        BigDecimal usdtPrice = tradeMapper.getAvgByMarketIdAndTime(77, startTime, endTime);//77是BHB/USDT交易对
        if (usdtPrice == null) {
            usdtPrice = BigDecimal.ONE;
        }
        return usdtPrice;
    }

    //保存统计记录
    private void saveUserCoinLeader(Integer userId, Date dataTime, BigDecimal oneLevelSum, BigDecimal twoLevelSum, BigDecimal threeLevelSum, BigDecimal usdtPrice, BigDecimal allSum){
        UserCoinLeader entity = new UserCoinLeader();
        entity.setCreateTime(new Date());
        entity.setDataTime(dataTime);
        entity.setUserId(userId);
        entity.setOneLevelSum(oneLevelSum);
        entity.setTwoLevelSum(twoLevelSum);
        entity.setThreeLevelSum(threeLevelSum);
        entity.setAllLevelSum(allSum);
        entity.setPrice(usdtPrice);
        userCoinLeaderMapper.insert(entity);
    }

    private BigDecimal getSum(Date startTime, Date endTime, List<Integer> userIds){
        if (EmptyHelper.isEmpty(userIds)) {
            return BigDecimal.ZERO;
        }
        return userCoinSnapshootLightMapper.subordinateSum(startTime, endTime, userIds);
//        if (EmptyHelper.isEmpty(userIds)) {
//            return BigDecimal.ZERO;
//        }
//        return userCoinMapper.sumByUserIdsAndCoinId(userIds, 54);
    }

    private List<Integer> getChildUserIds(List<Integer> userIds) {
        if (EmptyHelper.isEmpty(userIds)) {
            return new ArrayList<>();
        }
        return memberMapper.getIdsByFromUserIds(userIds);
    }

    private List<Integer> getOtherChildUserIds(List<Integer> userIds) {
        List<Integer> otherList = new ArrayList<>();
        do {
            userIds = getChildUserIds(userIds);
            otherList.addAll(userIds);
        }while (!EmptyHelper.isEmpty(userIds));
        return otherList;
    }

    //获取是统计的哪天的数据
    private Date getDataTime(){
        return getDataTimeStart();
    }

    private static Date getDataTimeStart(){
        ZoneId zoneId = ZoneId.systemDefault();
        LocalTime midnight = LocalTime.MIDNIGHT;
        LocalDate today = LocalDate.now();
        LocalDateTime todayMidnight = LocalDateTime.of(today, midnight);
        LocalDateTime yesterdayMidnight = todayMidnight.plusDays(-1);
        ZonedDateTime zdt = yesterdayMidnight.atZone(zoneId);
        return Date.from(zdt.toInstant());
    }

    private static Date getDataTimeEnd(){
        ZoneId zoneId = ZoneId.systemDefault();
        LocalTime midnight = LocalTime.MIDNIGHT;
        LocalDate today = LocalDate.now();
        LocalDateTime todayMidnight = LocalDateTime.of(today, midnight);
        ZonedDateTime zdt = todayMidnight.atZone(zoneId);
        return Date.from(zdt.toInstant());
    }

}