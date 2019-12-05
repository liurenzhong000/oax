package com.oax.service.impl.activity;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.oax.common.EmptyHelper;
import com.oax.constant.CommonConstant;
import com.oax.druid.DataSource;
import com.oax.druid.DataSourceType;
import com.oax.entity.activity.SnatchDetail;
import com.oax.entity.front.vo.SnatchDetailAggregateVo;
import com.oax.entity.front.vo.SnatchDetailVo;
import com.oax.form.SnatchAggregateForm;
import com.oax.mapper.activity.SnatchDetailMapper;
import com.oax.service.activity.SnatchDetailService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @Auther: hyp
 * @Date: 2019/1/16 19:20
 * @Description:
 */
@Service
public class SnatchDetailServiceImpl extends ServiceImpl<SnatchDetailMapper, SnatchDetail> implements SnatchDetailService {

    @Autowired
    private SnatchDetailMapper snatchDetailMapper;

    @Override
    @DataSource(DataSourceType.MASTER)
    public BigDecimal getBettedQty(Integer userId, Integer snatchActivityId) {
        if (userId == null) {
            return BigDecimal.ZERO;
        }
        return snatchDetailMapper.getBettedQty(userId, snatchActivityId);
    }

    @Override
    @DataSource(DataSourceType.MASTER)
    public Integer getBettedUnits(Integer userId, Integer snatchActivityId) {
        if (userId == null) {
            return 0;
        }
        return snatchDetailMapper.getBettedUnits(userId, snatchActivityId);
    }

    @Override
    @DataSource(DataSourceType.SLAVE)
    public BigDecimal getPayoutQty(Integer userId, Integer snatchActivityId){
        if (userId == null) {
            return BigDecimal.ZERO;
        }
        return snatchDetailMapper.getPayoutQty(userId, snatchActivityId);
    }

    @Override
    @DataSource(DataSourceType.SLAVE)
    public Integer getWinCount(Integer userId, Integer snatchActivityId){
        if (userId == null) {
            return 0;
        }
        return snatchDetailMapper.getWinCount(userId, snatchActivityId);
    }

    @Override
    @DataSource(DataSourceType.MASTER)
    public int updateAllNotWin(Integer snatchActivityId) {
        return snatchDetailMapper.updateAllNotWin(snatchActivityId);
    }

    @Override
    @DataSource(DataSourceType.MASTER)
    public int updateWin(Integer snatchActivityId, List<Map<String, Object>> winHashMapList, BigDecimal payoutQty) {
        return snatchDetailMapper.updateWin(snatchActivityId, winHashMapList, payoutQty);
    }

    @Override
    @DataSource(DataSourceType.MASTER)
    public int updateWinOne(Integer snatchActivityId, Integer number, String hash, BigDecimal payoutQty, BigDecimal chargesQty) {
        return snatchDetailMapper.updateWinOne(snatchActivityId, number, hash, payoutQty, chargesQty);
    }

    @Override
    @DataSource(DataSourceType.MASTER)
    public List<SnatchDetail> winSnatchDetails(Integer activityId) {
        return snatchDetailMapper.winSnatchDetails(activityId);
    }

    @Override
    @DataSource(DataSourceType.MASTER)
    public List<SnatchDetail> listByActivityIdAndNumber(Integer snatchActivityId, List<Integer> winNumbers) {
        return snatchDetailMapper.listByActivityIdAndNumber(snatchActivityId, winNumbers);
    }

    @Override
    @DataSource(DataSourceType.SLAVE)
    public List<SnatchDetailVo> listVoByUserIdAndActivityId(Integer userId, Integer activityId) {
        List<SnatchDetailVo> snatchDetailVos = snatchDetailMapper.listVoByUserIdAndActivityId(userId, activityId);
        fillSnatchDetailVoa(snatchDetailVos);
        return snatchDetailVos;
    }

    @Override
    @DataSource(DataSourceType.SLAVE)
    public Page<SnatchDetailAggregateVo> listAggregateVoByUserIdAndConfigId(Integer userId, SnatchAggregateForm form) {
        PageHelper.startPage(form.getPageNum(), form.getPageSize());
        List<Integer> activityIds = snatchDetailMapper.listBetActivityId(userId, form.getConfigId());
        if (EmptyHelper.isEmpty(activityIds)) {
            return new Page<>();
        }
        List<SnatchDetailAggregateVo> aggregateVos = snatchDetailMapper.listAggregateVoByActivityIds(activityIds);
        aggregateVos.forEach(item -> {
            item.setBetUnits(getBettedUnits(userId, item.getActivityId()));
            item.setBetQty(getBettedQty(userId, item.getActivityId()));
            item.setWinCount(getWinCount(userId, item.getActivityId()));
            item.setPayoutQty(getPayoutQty(userId, item.getActivityId()));
            item.setNumberStr(getFirstNumberStr(userId, item.getActivityId()));
        });
        PageInfo pageInfo = new PageInfo(activityIds);
        return new Page<SnatchDetailAggregateVo>().setRecords(aggregateVos).setTotal(pageInfo.getTotal()).setSize(pageInfo.getSize());
    }

    @Override
    @DataSource(DataSourceType.SLAVE)
    public String getFirstNumberStr(Integer userId, Integer activityId) {
        return snatchDetailMapper.getFirstNumberStr(userId, activityId);
    }

    //获取开奖编号
    @Override
    @DataSource(DataSourceType.SLAVE)
    public List<SnatchDetailVo> listWinVo(Integer userId, Integer activityId){
        List<SnatchDetailVo> snatchDetailVos = snatchDetailMapper.listWinVo(userId, activityId);
        fillSnatchDetailVoa(snatchDetailVos);
        return snatchDetailVos;
    }

    @Override
    public List<Integer> listRobotNumber(Integer activityId) {
        return snatchDetailMapper.listRobotNumber(activityId);
    }

    private void fillSnatchDetailVoa(List<SnatchDetailVo> snatchDetailVos){
        snatchDetailVos.forEach(item-> {
            if (StringUtils.isNotBlank(item.getHash())) {
                item.setHashUrl(CommonConstant.EOS_HASHURL + item.getHash());
            }
        });
    }

}
