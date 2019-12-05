package com.oax.admin.service.impl.activity;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.oax.admin.form.ListSnatchActivityForm;
import com.oax.admin.service.activity.SnatchActivityService;
import com.oax.entity.admin.vo.SnatchActivityAdminVo;
import com.oax.mapper.activity.SnatchActivityMapper;
import com.oax.mapper.activity.SnatchDetailMapper;
import com.oax.mapper.front.MemberMapper;
import com.oax.mapper.front.UserCoinMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: hyp
 * @Date: 2019/1/21 19:05
 * @Description: 每期的相关数据
 */
@Service
@Transactional
public class SnatchActivityServiceImpl implements SnatchActivityService {

    @Autowired
    private SnatchActivityMapper snatchActivityMapper;

    @Autowired
    private SnatchDetailMapper snatchDetailMapper;

    @Autowired
    private UserCoinMapper userCoinMapper;

    @Override
    public PageInfo list(ListSnatchActivityForm form) {
        PageHelper.startPage(form.getPageNum(), form.getPageSize());
        List<SnatchActivityAdminVo> snatchActivityAdminVos = snatchActivityMapper.pageForAdmin(form.getId(), form.getConfigId(), form.getCoinId());
        return new PageInfo<>(snatchActivityAdminVos);
    }

    //投注，手续费，机器人账户
    @Override
    public Map<String, Object> aggregationDetail(Integer coinId){
        Map<String, Object> data = new HashMap<>();
        BigDecimal allBetQty = snatchDetailMapper.sumBetQty(coinId);
        BigDecimal allChargesQty = snatchDetailMapper.sumChargesQty(coinId);
        BigDecimal robotBalance = userCoinMapper.getBanlanceByCoinIdAndUserId(2, coinId);
        BigDecimal robotPayout = snatchDetailMapper.sumRobotPayout(2, coinId);
        data.put("allBetQty", allBetQty);
        data.put("allChargesQty", allChargesQty);
        data.put("robotBalance", robotBalance);
        data.put("robotPayout", robotPayout);
        return data;
    }

}
