package com.oax.admin.service.impl.activity;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.oax.admin.service.activity.PanicBuyActivityService;
import com.oax.common.AssertHelper;
import com.oax.entity.activity.PanicBuyActivity;
import com.oax.entity.activity.PanicBuyDetail;
import com.oax.entity.activity.PanicBuyHelp;
import com.oax.entity.admin.param.PanicBuyActivityParam;
import com.oax.entity.admin.param.PanicBuyDetailParam;
import com.oax.entity.admin.param.PanicBuyHelpParam;
import com.oax.mapper.activity.PanicBuyActivityMapper;
import com.oax.mapper.activity.PanicBuyDetailMapper;
import com.oax.mapper.activity.PanicBuyHelpMapper;
import com.oax.mapper.activity.PanicBuyOrderMapper;
import jdk.nashorn.internal.runtime.logging.Logger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.util.Date;
import java.util.List;

@Service
@Transactional
@Slf4j
public class PanicBuyActivityServiceImpl implements PanicBuyActivityService {

    @Autowired
    private PanicBuyActivityMapper panicBuyActivityMapper;
    @Autowired
    private PanicBuyDetailMapper panicBuyDetailMapper;
    @Autowired
    private PanicBuyHelpMapper panicBuyHelpMapper;
    @Autowired
    private PanicBuyOrderMapper panicBuyOrderMapper;

    @Override
    public void saveOne(PanicBuyActivity entity) {
        entity.setParticipate(0);
        entity.setCreateTime(new Date());
        entity.setReach(0);
        entity.setBuyCount(0);
        entity.setStatus(PanicBuyActivity.Status.CLOSE.ordinal());
        panicBuyActivityMapper.insert(entity);
    }

    @Override
    public void updateOne(Integer id, Integer participateBase, Integer reachBase) {
        PanicBuyActivity entity = panicBuyActivityMapper.selectById(id);
        AssertHelper.notEmpty(entity, "活动不存在");
        entity.setReachBase(reachBase);
        entity.setParticipateBase(participateBase);
        panicBuyActivityMapper.updateById(entity);
    }

    @Override
    public PageInfo<PanicBuyActivity> pageForAdmin(PanicBuyActivityParam param) {
        PageHelper.startPage(param.getPageNum(), param.getPageSize());
        List<PanicBuyActivity> panicBuyActivities = panicBuyActivityMapper.pageForAdmin(param);
        return new PageInfo<>(panicBuyActivities);
    }

    //参与详情
    @Override
    public PageInfo<PanicBuyDetail> pageDetailForAdmin(PanicBuyDetailParam param) {
        PageHelper.startPage(param.getPageNum(), param.getPageSize());
        List<PanicBuyDetail> panicBuyDetails = panicBuyDetailMapper.pageForAdmin(param);
        return new PageInfo<>(panicBuyDetails);
    }

    //助力详情
    @Override
    public PageInfo<PanicBuyHelp> pageHelpForAdmin(PanicBuyHelpParam param) {
        PageHelper.startPage(param.getPageNum(), param.getPageSize());
        List<PanicBuyHelp> panicBuyHelps = panicBuyHelpMapper.pageForAdmin(param);
        return new PageInfo<>(panicBuyHelps);
    }

    @Override
    public void open(Integer id) {
        PanicBuyActivity entity = panicBuyActivityMapper.selectById(id);
        AssertHelper.notEmpty(entity, "活动不存在");
        entity.setStatus(PanicBuyActivity.Status.OPEN.ordinal());
        panicBuyActivityMapper.updateById(entity);
        log.info("BHB活动，开启活动 id={}", id);
    }

    @Override
    public void finish(Integer id) {
        PanicBuyActivity entity = panicBuyActivityMapper.selectById(id);
        AssertHelper.notEmpty(entity, "活动不存在");
        entity.setFinishTime(new Date());
        entity.setStatus(PanicBuyActivity.Status.FINISH.ordinal());
        panicBuyActivityMapper.updateById(entity);

        //更新当前活动达标用户为可购买
        int result = panicBuyDetailMapper.updateReachUser(id, getValidTimeTime());
        log.info("BHB活动，结束活动，发放购买资格 result={}, id={}", result, id);
    }

    @Override
    public void orderClose(Integer id) {
        PanicBuyActivity entity = panicBuyActivityMapper.selectById(id);
        AssertHelper.notEmpty(entity, "活动不存在");
        entity.setStatus(PanicBuyActivity.Status.ORDER_CLOSE.ordinal());

        int result = panicBuyOrderMapper.updateCloseByActivityId(id);
        log.info("BHB活动，用户订单失效 result={}, id={}", result, id);
    }

    //第二天的17:00
    private Date getValidTimeTime(){
        ZoneId zoneId = ZoneId.systemDefault();
        LocalTime midnight = LocalTime.MIDNIGHT;
        LocalDate today = LocalDate.now();
        LocalDateTime todayMidnight = LocalDateTime.of(today, midnight);
        LocalDateTime yesterdayMidnight = todayMidnight.plusDays(1).plusHours(17);
        ZonedDateTime zdt = yesterdayMidnight.atZone(zoneId);
        return Date.from(zdt.toInstant());
    }

}
