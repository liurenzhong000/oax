package com.oax.scheduled;

import com.oax.common.ResultResponse;
import com.oax.entity.front.MovesayMoneyActive;
import com.oax.entity.front.MovesayMoneyActiveList;
import com.oax.entity.front.UserCoin;
import com.oax.service.CoinService;
import com.oax.service.MovesayMoneyActiveListService;
import com.oax.service.MovesayMoneyActiveService;
import com.oax.service.UserCoinService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Component
public class lockPositionTaskMin1 {
    private final Logger mLogger = LoggerFactory.getLogger(lockPositionTaskMin1.class);


    @Autowired
    private MovesayMoneyActiveService movesayMoneyActiveService;

    @Autowired
    private MovesayMoneyActiveListService movesayMoneyActiveListService;


    @Autowired
    private UserCoinService userCoinService;


    @Scheduled(cron = "*/5 * * * * ?")
    @Async
    public void unlockPosition() {
        Date currenttime = new Date();
        List<MovesayMoneyActive> movesayMoneyActives = new ArrayList<MovesayMoneyActive>();
        movesayMoneyActives = movesayMoneyActiveService.selectAllRecord();
        for (int i = 0; i < movesayMoneyActives.size(); i++) {
            Date Active_start = movesayMoneyActives.get(i).getStartTime();
            Date Active_end = movesayMoneyActives.get(i).getEndTime();
            Integer status = movesayMoneyActives.get(i).getStatus();
            if (movesayMoneyActives.get(i).getType() == 1) {
                Date Interest_start = movesayMoneyActives.get(i).getInterstartTime();
                Date Interest_end = movesayMoneyActives.get(i).getInterendTime();

                if (currenttime.before(Active_start)) {
                    status = 1;
                }

                if ((Active_start.equals(currenttime) || Active_start.before(currenttime)) && currenttime.before(Active_end)) {
                    status = 2;
                }

                if ((Interest_start.equals(currenttime) || Interest_start.before(currenttime)) && currenttime.before(Interest_end)) {
                    status = 3;
                }

                if (Interest_end.before(currenttime) || Interest_end.equals(currenttime)) {
                    status = 4;
                }
            }
            if (movesayMoneyActives.get(i).getType() == 2) {
                if (currenttime.before(Active_start)) {
                    status = 1;
                }

                if ((Active_start.equals(currenttime) || Active_start.before(currenttime)) && currenttime.before(Active_end)) {
                    status = 2;
                }

                if ((Active_end.equals(currenttime) || Active_end.before(currenttime))) {
                    status = 5;
                }
            }
            int ret = movesayMoneyActiveService.updateMoveMoneyActive(movesayMoneyActives.get(i).getId(), status);
        }
    }


    @Scheduled(cron = "*/5 * * * * ?")
    @Async
    public void updatePositionStatus() {
        Date currenttime = new Date();
        List<MovesayMoneyActiveList> movesayMoneyActiveLists = new ArrayList<MovesayMoneyActiveList>();
        movesayMoneyActiveLists = movesayMoneyActiveListService.SelectAllRecord();
        for (int i = 0; i < movesayMoneyActiveLists.size(); i++) {
            MovesayMoneyActive movesayMoneyActive = movesayMoneyActiveService.selectByPrimaryKey(movesayMoneyActiveLists.get(i).getActiveId());
            Date Interest_start = movesayMoneyActiveLists.get(i).getStartTime();
            Date Interest_end = movesayMoneyActiveLists.get(i).getEndTime();
            Integer status = movesayMoneyActive.getStatus();

            //静态余利宝活动
            if (movesayMoneyActive.getType() == 1) {
                if (movesayMoneyActiveLists.get(i).getStatus() == 3)
                    continue;

                if (currenttime.before(Interest_start)) {
                    status=1;
                    int ret = movesayMoneyActiveListService.updateStatus(movesayMoneyActiveLists.get(i).getId(),status);
                }


                if ((Interest_start.equals(currenttime) || Interest_start.before(currenttime)) && currenttime.before(Interest_end)) {
                    status=2;
                    int ret = movesayMoneyActiveListService.updateStatus(movesayMoneyActiveLists.get(i).getId(),status);
                }

                if (Interest_end.before(currenttime) || Interest_end.equals(currenttime)) {
                    UserCoin userCoin = userCoinService.queryBalanceInfoByUserId(movesayMoneyActiveLists.get(i).getUserId(), movesayMoneyActiveLists.get(i).getMarketId());
                    movesayMoneyActiveListService.updateStaticMoneyActive(userCoin, movesayMoneyActiveLists.get(i));
                }
            }
        }
    }




    /**
     * 每天凌晨执行一次
     */
    @Scheduled(cron = "0 0 0 * * ?")
    @Async
    public void updateActiveStatus() {
        List<MovesayMoneyActiveList> movesayMoneyActiveLists = new ArrayList<MovesayMoneyActiveList>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String currenttime = sdf.format(new Date());
        System.out.println("-----------------------------------------开始释放收益-----------------------------------");
        movesayMoneyActiveLists = movesayMoneyActiveListService.SelectAllRecord();
        for (int i = 0; i < movesayMoneyActiveLists.size(); i++) {
            String Interest_start = sdf.format(movesayMoneyActiveLists.get(i).getStartTime());
            String Interest_end = sdf.format(movesayMoneyActiveLists.get(i).getEndTime());

            //动态余利宝活动
            if (movesayMoneyActiveLists.get(i).getType() == 2) {
                if (movesayMoneyActiveLists.get(i).getStatus() == 3) {
                    continue;
                }
                //每日释放收益
                if ((Interest_start.equals(currenttime) || Interest_start.compareTo(currenttime) < 0) && (currenttime.compareTo(Interest_end) < 0 || currenttime.equals(Interest_end))) {
                    //计算每日应该释放的金额
                    BigDecimal profits = new BigDecimal(movesayMoneyActiveLists.get(i).getProfits()).divide(new BigDecimal(100));
                    BigDecimal money = movesayMoneyActiveLists.get(i).getCumulativeJoin().multiply(profits).divide(new BigDecimal(360), 6, RoundingMode.HALF_UP);
                    movesayMoneyActiveListService.updateDayMoveMoneyActive(movesayMoneyActiveLists.get(i), money);
                    System.out.println("-----------------------------------------每日释放收益-----------------------------------");

                    if (currenttime.equals(Interest_end)) {
                        //释放最后没提现的金额
                        UserCoin userCoin = userCoinService.queryBalanceInfoByUserId(movesayMoneyActiveLists.get(i).getUserId(), movesayMoneyActiveLists.get(i).getMarketId());
                        movesayMoneyActiveListService.updateAllMoveMoneyActive(movesayMoneyActiveLists.get(i), userCoin);
                        movesayMoneyActiveListService.updatePromoteFund(movesayMoneyActiveLists.get(i));
                        System.out.println("-----------------------------------------到期释放收益-----------------------------------");
                    }
                }

            }
        }
    }


}
