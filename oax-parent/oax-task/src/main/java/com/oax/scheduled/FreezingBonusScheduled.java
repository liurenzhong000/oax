package com.oax.scheduled;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.oax.entity.front.BonusInfo;
import com.oax.entity.front.UserCoinFreezingDetail;
import com.oax.mapper.front.BonusInfoMapper;
import com.oax.mapper.front.UserCoinFreezingDetailMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class FreezingBonusScheduled {

    @Autowired
    private BonusInfoMapper bonusInfoMapper;
    @Autowired
    private UserCoinFreezingDetailMapper userCoinFreezingDetailMapper;

    @Scheduled(cron = "0 0 20 * * ?")
    @Async
    public void  freezingBonusScheduled(){
        List<UserCoinFreezingDetail> userCoinFreezingDetailList = userCoinFreezingDetailMapper.selectYesterdayList();
        userCoinFreezingDetailList.stream().forEach(item->handle(item));
        log.info("冻结操作共:"+userCoinFreezingDetailList.size());
    }

    public void handle(UserCoinFreezingDetail userCoinFreezingDetail){
        BonusInfo bonusInfo = bonusInfoMapper.selectOne(new QueryWrapper<BonusInfo>().lambda().eq(BonusInfo::getUserId,userCoinFreezingDetail.getUserId()));
        if(bonusInfo ==null){
            bonusInfo = new BonusInfo();
            bonusInfo.setUserId(userCoinFreezingDetail.getUserId());
            bonusInfo.setBanlace(userCoinFreezingDetail.getQty());
            bonusInfo.setStartTime(userCoinFreezingDetail.getFreezingTime());
            bonusInfo.setEndTime(userCoinFreezingDetail.getUnfreezeTime());
            bonusInfoMapper.insert(bonusInfo);
        }else {
            if(userCoinFreezingDetail.getStatus()==0){
                if(userCoinFreezingDetail.getQty() != null){
                    bonusInfo.setBanlace(bonusInfo.getBanlace().add(userCoinFreezingDetail.getQty()));
                }
            }else {
                if(userCoinFreezingDetail.getQty() != null){
                    bonusInfo.setBanlace(bonusInfo.getBanlace().subtract(userCoinFreezingDetail.getQty()));
                }
            }
            bonusInfo.setStartTime(userCoinFreezingDetail.getFreezingTime());
            bonusInfo.setEndTime(userCoinFreezingDetail.getUnfreezeTime());
            bonusInfoMapper.updateById(bonusInfo);
        }
        log.info("user_id = {}, banlance={}", bonusInfo.getUserId(), bonusInfo.getBanlace());
    }

}
