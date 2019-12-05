package com.oax.service.impl.activity;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.oax.common.AESHelper;
import com.oax.common.AssertHelper;
import com.oax.common.BeanHepler;
import com.oax.common.CodecEncoder;
import com.oax.druid.DataSource;
import com.oax.druid.DataSourceType;
import com.oax.entity.activity.PanicBuyActivity;
import com.oax.entity.activity.PanicBuyDetail;
import com.oax.entity.activity.PanicBuyHelp;
import com.oax.entity.front.vo.PanicBuyHelpVo;
import com.oax.exception.VoException;
import com.oax.form.PanicBuyHelpForm;
import com.oax.mapper.activity.PanicBuyActivityMapper;
import com.oax.mapper.activity.PanicBuyDetailMapper;
import com.oax.mapper.activity.PanicBuyHelpMapper;
import com.oax.service.SmsCaptchaService;
import com.oax.service.activity.PanicBuyHelpService;
import com.oax.vo.PanicBuyShareVo;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class PanicBuyHelpServiceImpl implements PanicBuyHelpService {

    private static final String encryptKey = "!@#eW*$%!~Der#d1";

    private static final Integer fullHelpValue = 100;

    private static final Integer helpValueStart = 2;
    private static final Integer helpValueEnd = 5;
    @Autowired
    private PanicBuyHelpMapper panicBuyHelpMapper;

    @Autowired
    private PanicBuyDetailMapper panicBuyDetailMapper;

    @Autowired
    private PanicBuyActivityMapper panicBuyActivityMapper;

    @Autowired
    private SmsCaptchaService smsCaptchaService;

    //生成用户的活动邀请码
    public static String generateShareCode(Integer userId, Integer activityId){
        String shadeCode = AESHelper.encrypt(userId+"-"+activityId, encryptKey, CodecEncoder.WEB_SAVE_BASE64);
        return shadeCode;
    }

    //根据分享码获取用户id
    public static Integer getUserIdByUserCode(String shareCode) {
        String encryptStr = AESHelper.decrypt(shareCode, encryptKey, CodecEncoder.WEB_SAVE_BASE64);
        Integer userId = Integer.parseInt(StringUtils.substringBefore(encryptStr, "-"));
        return userId;
    }

    //根据分享码获取活动id
    public static Integer getActivityIdByUserCode(String shareCode) {
        String encryptStr = AESHelper.decrypt(shareCode, encryptKey, CodecEncoder.WEB_SAVE_BASE64);
        Integer activityId = Integer.parseInt(StringUtils.substringAfter(encryptStr, "-"));
        return activityId;
    }

    /**
     * 保存助力记录
     */
    @Override
    @DataSource(DataSourceType.MASTER)
    public PanicBuyShareVo saveHelp(HttpServletRequest request,PanicBuyHelpForm form) throws VoException {
        Integer helpValue = RandomUtils.nextInt(helpValueStart, helpValueEnd);

        try {
            getUserIdByUserCode(form.getShareCode());
            getActivityIdByUserCode(form.getShareCode());
        } catch (Exception e) {
            throw new RuntimeException("分享码错误，助力失败");
        }
        Integer userId = getUserIdByUserCode(form.getShareCode());
        Integer activityId = getActivityIdByUserCode(form.getShareCode());
        //activity数据更新
        PanicBuyActivity panicBuyActivity = panicBuyActivityMapper.selectById(activityId);
        AssertHelper.isTrue(panicBuyActivity.getEndTime().getTime() >= System.currentTimeMillis(), "本期活动已结束");

        //判断手机号是否助力过当前期数
        Integer count = panicBuyHelpMapper.selectCount(new QueryWrapper<PanicBuyHelp>().lambda()
                .eq(PanicBuyHelp::getPhone, form.getPhone())
                .eq(PanicBuyHelp::getUserId, userId)
                .eq(PanicBuyHelp::getActivityId, activityId));
        AssertHelper.isTrue(count < 1, "该手机号已经助力过，请勿重新助力");

        AssertHelper.isTrue(smsCaptchaService.checkSms(request,form.getPhone(), form.getHelpCode(),"cn"), "助力码错误");

        PanicBuyHelp panicBuyHelp = new PanicBuyHelp();
        panicBuyHelp.setCreateTime(new Date());
        panicBuyHelp.setUserId(userId);
        panicBuyHelp.setPhone(form.getPhone());
        panicBuyHelp.setActivityId(activityId);
        panicBuyHelp.setHelpValue(helpValue);
        panicBuyHelpMapper.insert(panicBuyHelp);

        //detail数据,更新
        PanicBuyDetail panicBuyDetail = panicBuyDetailMapper.selectByUserIdAndActivityId(userId, activityId);
        AssertHelper.notEmpty(panicBuyDetail, "获取活动参与数据异常");
        panicBuyDetail.setHelpCount(panicBuyDetail.getHelpCount() + 1);
        panicBuyDetail.setHelpValue(panicBuyDetail.getHelpValue() + helpValue);
        if (panicBuyDetail.getHelpValue() >= fullHelpValue && panicBuyDetail.getStatus() == PanicBuyDetail.Status.PARTICIPATE.ordinal()) {
            panicBuyDetail.setStatus(PanicBuyDetail.Status.SUCCESS.ordinal());
            panicBuyDetail.setFinishTime(new Date());
            panicBuyActivity.setReach(panicBuyActivity.getReach() + 1);
            panicBuyActivityMapper.updateById(panicBuyActivity);
        }
        panicBuyDetailMapper.updateById(panicBuyDetail);

        //返回助力值和当前情况
        PanicBuyShareVo data = getIndexData(panicBuyDetail.getShareCode());
        data.setHelpValue(helpValue);
        return data;
    }

    public static void main(String[] args) {
        System.out.println(generateShareCode(345267, 20181213));
        System.out.println(getUserIdByUserCode("jUSY5X2Q5S3lbKsrIjJDbQ=="));
        System.out.println(getActivityIdByUserCode("jUSY5X2Q5S3lbKsrIjJDbQ=="));

        System.out.println("008618296799192".replaceAll("(\\d{4})(\\d{3})\\d{4}(\\d{4})","$2****$3"));
    }


    /**
     * 获取活动分享页数据
     */
    @Override
    @DataSource(DataSourceType.SLAVE)
    public PanicBuyShareVo getIndexData(String shareCode) {
        //判断活动是否结束
        try {
            getUserIdByUserCode(shareCode);
            getActivityIdByUserCode(shareCode);
        } catch (Exception e) {
            throw new RuntimeException("推广链接不完整或有错误，请重新扫描页面二维码或复制推广链接");
        }

        PanicBuyShareVo panicBuyShareVo = new PanicBuyShareVo();
        Integer userId = getUserIdByUserCode(shareCode);
        Integer activityId = getActivityIdByUserCode(shareCode);
        PanicBuyActivity panicBuyActivity = panicBuyActivityMapper.selectById(activityId);
        //本期活动是否结束
        panicBuyShareVo.setValid(panicBuyActivity.getEndTime().getTime() >= System.currentTimeMillis());
        AssertHelper.isTrue(panicBuyActivity.getReach() + panicBuyActivity.getReachBase() < 777, "本期活动已结束，"+(panicBuyActivity.getId()+1)+"期马上开始！");
        AssertHelper.isTrue(panicBuyActivity.getEndTime().getTime() > System.currentTimeMillis(), "本期活动已结束，"+(panicBuyActivity.getId()+1)+"期马上开始！");
//        AssertHelper.isTrue(panicBuyActivity.getEndTime().getTime() >= System.currentTimeMillis(), "本期活动已结束");

        List<PanicBuyHelp> helpList = panicBuyHelpMapper.selectList(new QueryWrapper<PanicBuyHelp>().lambda()
                .eq(PanicBuyHelp::getUserId, userId).eq(PanicBuyHelp::getActivityId, activityId).orderByDesc(PanicBuyHelp::getId));
        List<PanicBuyHelpVo> helpVoList = BeanHepler.copyListByProperties(helpList, PanicBuyHelpVo.class);
        helpVoList.forEach(item->item.setPhone(item.getPhone().replaceAll("(\\d{4})(\\d{3})\\d{4}(\\d{4})","$2****$3")));

        PanicBuyDetail panicBuyDetail = panicBuyDetailMapper.selectByUserIdAndActivityId(userId, activityId);
        if (panicBuyShareVo.isValid()) {
            AssertHelper.notEmpty(panicBuyDetail, "获取活动参与数据异常");
        }

        panicBuyShareVo.setPanicBuyHelpVos(helpVoList);
        BigDecimal finishRatio = BigDecimal.valueOf(panicBuyDetail.getHelpValue()).divide(BigDecimal.valueOf(fullHelpValue), 2, BigDecimal.ROUND_HALF_UP)
                .multiply(BigDecimal.valueOf(100).setScale(0));
        if (finishRatio.compareTo(BigDecimal.valueOf(100)) > 0) {
            finishRatio = BigDecimal.valueOf(100);
        }
        panicBuyShareVo.setFinishRatio(finishRatio);
        return panicBuyShareVo;
    }

}