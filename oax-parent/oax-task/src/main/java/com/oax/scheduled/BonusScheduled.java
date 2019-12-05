package com.oax.scheduled;

import com.oax.entity.front.*;
import com.oax.mapper.front.*;
import com.oax.service.KlineService;
import com.oax.service.RechargeService;
import com.oax.service.TradeService;
import com.oax.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author ：zl
 * @ClassName:：BonusScheduled
 * @Description： BHB分红
 */
@Slf4j
@Controller
public class BonusScheduled {

    @Autowired
    UserCoinMapper userCoinMapper;

    @Autowired
    UserCoinDetailMapper userCoinDetailMapper;

    @Autowired
    MemberMapper memberMapper;

    @Autowired
    RechargeService rechargeService;

    @Autowired
    TradeMapper tradeMapper;

    @Autowired
    BonusMapper bonusMapper;


    /**
     * 分红机器人
     */
//    @Async
//    @Scheduled(cron = "0 0/1 * * * ?")
    public void autoBonus() {
        //人民币阈值
        double thresholdCNY=10000.;
        //币种id
        Integer leftCoinId = 46;
        //交易对id
        Integer rightCoinId = 10;
        //前一天的平均价格
        BigDecimal averagePrice = getAveragePrice(leftCoinId,rightCoinId);
        log.info("前一天的平均价格" + averagePrice);
        //持仓阈值
        BigDecimal thresholdNumber=new BigDecimal(thresholdCNY).divide(averagePrice,4, BigDecimal.ROUND_HALF_UP);
        log.info("持仓阈值" + thresholdNumber);
        //所有持有币的用户
        ArrayList<UserCoin> userCoinArrayList = userCoinMapper.selectByCoinId(leftCoinId);
        //分红
        for (UserCoin userCoin:userCoinArrayList) {
            if(thresholdNumber.compareTo(userCoin.getBanlance())>0){
                hierarchyBonus( userCoin.getUserId(),thresholdNumber, averagePrice, leftCoinId,rightCoinId);
            }
        }
    }

    /**
     * 获取平均价格
     * @param leftCoinId
     * @param rightCoinId
     * @return
     */
    private BigDecimal getAveragePrice(Integer leftCoinId,Integer rightCoinId) {
        BigDecimal price = new BigDecimal(0);
        Date startDate = DateUtil.parseStringToDataHMS(DateUtil.getStartTime(DateUtil.getLast(DateUtil.parseDateToYYYYMMDDHHMMSS(new Date()),1)));
        System.out.println("startDate" + startDate);
        Date endDate = DateUtil.parseStringToDataHMS(DateUtil.getnowEndTime(DateUtil.getLast(DateUtil.parseDateToYYYYMMDDHHMMSS(new Date()),1)));
        System.out.println("endDate" + endDate);
        List<Trade> tradeList = tradeMapper.selectByleftCoinIdAndrightCoinIdAndTime(leftCoinId,rightCoinId,startDate,endDate);
        System.out.println("tradeList" + tradeList.size());
        for (Trade trade:tradeList) {
            price.add(trade.getPrice());
        }
        price = price.divide(new BigDecimal(tradeList.size()),4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(6.93));
        return price;
    }

    /**
     * 查找该用户下的用户并确定层级
     * @param userId
     */
    public void hierarchyBonus(Integer userId,BigDecimal thresholdNumber,BigDecimal averagePrice,Integer leftCoinId,Integer rightCoinId){
        List<Member> memberList = memberMapper.getChildrenUserByUserId(userId);
        List<Member> zeroLevelArrayList = memberList.subList(0,0);
        //一级用户
        ArrayList<Member> oneLevelArrayList = new ArrayList();
        ArrayList<Integer> oneLeverUserId=new ArrayList<>();
        //二级用户
        ArrayList<Member> twoLevelArrayList = new ArrayList();
        ArrayList<Integer> twoLeverUserId=new ArrayList<>();
        //三级用户
        ArrayList<Member> threeLevelArrayList = new ArrayList();
        ArrayList<Integer> threeLeverUserId=new ArrayList<>();
        for (Member member:memberList) {
            if(member.getFromUserId()==userId){
                oneLevelArrayList.add(member);
                oneLeverUserId.add(userId);
            }
        }
        for (Member member:memberList) {
            if(oneLeverUserId.contains(member.getFromUserId())){
                twoLevelArrayList.add(member);
                twoLeverUserId.add(member.getId());
            }
        }
        for (Member member:memberList) {
            if(twoLeverUserId.contains(member.getFromUserId())){
                threeLevelArrayList.add(member);
                threeLeverUserId.add(member.getId());
            }
        }
        if(zeroLevelArrayList.size()>0){
            distributBonus(zeroLevelArrayList,userId,0.07,thresholdNumber,averagePrice,leftCoinId,rightCoinId,0);
        }
        if(oneLevelArrayList.size()>0){
            distributBonus(oneLevelArrayList,userId,0.01,thresholdNumber,averagePrice,leftCoinId,rightCoinId,1);
        }
        if(twoLevelArrayList.size()>0){
            distributBonus(twoLevelArrayList,userId,0.005,thresholdNumber,averagePrice,leftCoinId,rightCoinId,2);
        }
        if(threeLevelArrayList.size()>0){
            distributBonus(threeLevelArrayList,userId,0.002,thresholdNumber,averagePrice,leftCoinId,rightCoinId,3);
        }
    }

    /**
     * 分发分红
     * @param memberArrayList
     * @param toUserId
     * @param point
     */
    public void distributBonus(List<Member> memberArrayList, Integer toUserId, double point,BigDecimal thresholdNumber,BigDecimal averagePrice,Integer leftCoinId,Integer rightCoinId,int hierarchy){
        for (Member member:memberArrayList) {
            List<UserCoinDetail> userCoinDetailList = userCoinDetailMapper.selectListByUserId(member.getId());
            //平均持仓量
            BigDecimal averageThreshold= new BigDecimal(0);
            BigDecimal number = new BigDecimal(0);
            for(UserCoinDetail userCoinDetail:userCoinDetailList){
                averageThreshold.add(userCoinDetail.getAfterBalance());
            }
            averageThreshold = averageThreshold.divide(new BigDecimal(userCoinDetailList.size()),4, BigDecimal.ROUND_HALF_UP);
            if(judgePosition(userCoinDetailList,thresholdNumber)){//分红充值
                //分红所得持仓量
                BigDecimal threshold = averageThreshold.multiply(new BigDecimal(point));
                //分红所得USDT量
                number = threshold.multiply(averagePrice).divide(new BigDecimal(6.93),4, BigDecimal.ROUND_HALF_UP);
                Recharge recharge = new Recharge();
                recharge.setCoinId(rightCoinId);
                recharge.setQty(number);
                recharge.setRemark("BHB分红");
                recharge.setUserId(toUserId);
                recharge.setType(4);
                rechargeService.insertIgnoreAndAddUserBalance(recharge);
            }
            Bonus bonus = new Bonus();
            bonus.setRightCoinId(Long.valueOf(String.valueOf(rightCoinId)));
            bonus.setLeftCoinId(Long.valueOf(String.valueOf(leftCoinId)));
            bonus.setThresholdNumber(userCoinMapper.selectByUserIdAndCoinId(member.getId(),leftCoinId).getBanlance());
            bonus.setAverageThreshold(averageThreshold);
            bonus.setBonus(number);
            bonus.setHierarchy(hierarchy);
            bonusMapper.insert(bonus);
        }

    }

    /**
     * 判断持仓
     * @param userCoinDetailList
     */
    public Boolean  judgePosition(List<UserCoinDetail> userCoinDetailList,BigDecimal thresholdNumber){
        for (UserCoinDetail userCoinDetail:userCoinDetailList) {
            if(thresholdNumber.compareTo(userCoinDetail.getAfterBalance())>0){
                return  false;
            }
        }
        return true;
    }
}