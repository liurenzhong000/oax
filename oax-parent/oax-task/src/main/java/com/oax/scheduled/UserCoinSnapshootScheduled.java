package com.oax.scheduled;

import com.oax.entity.front.UserCoin;
import com.oax.entity.front.UserCoinSnapshoot;
import com.oax.mapper.front.UserCoinMapper;
import com.oax.mapper.front.UserCoinSnapshootMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * 用户资金快照
 */
@Component
@Slf4j
public class UserCoinSnapshootScheduled {

    @Autowired
    private UserCoinSnapshootMapper userCoinSnapshootMapper;

    @Autowired
    private UserCoinMapper userCoinMapper;

    /**每小时一单*/
//    @Scheduled(cron="0 0 0/1 * * ?")
//    @Async
    public void createUserCoinSnapshoot(){
        log.info("===================用户BHB资金快照=====================");
        List<UserCoin> hasBHBUserCoins = userCoinMapper.selectByCoinId(54);//54是BHB，查询拥有BHB的用户记录
        hasBHBUserCoins.forEach(item->{
            UserCoinSnapshoot snapshoot = new UserCoinSnapshoot();
            snapshoot.setUserId(item.getUserId());
            snapshoot.setBalance(item.getBanlance());
            snapshoot.setFreezingBalance(item.getFreezingBanlance());
            snapshoot.setCoinId(item.getCoinId());
            snapshoot.setCreateTime(new Date());
            userCoinSnapshootMapper.insert(snapshoot);
        });
    }
}
