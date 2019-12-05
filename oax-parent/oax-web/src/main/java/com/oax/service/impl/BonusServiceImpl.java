package com.oax.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.oax.entity.admin.param.PageParam;
import com.oax.entity.front.Bonus;
import com.oax.mapper.front.BonusMapper;
import com.oax.mapper.front.MemberMapper;
import com.oax.service.IBonusService;
import com.oax.vo.BonusVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zl
 * @since 2018-11-26
 */
@Service
@Slf4j
public class BonusServiceImpl extends ServiceImpl<BonusMapper, Bonus> implements IBonusService {

    @Autowired
    BonusMapper bonusMapper;
    @Autowired
    MemberMapper memberMapper;
    @Override
    public Page<BonusVO> pageBonus(PageParam pageParam, Wrapper<Bonus> wrapper) {

        List<BonusVO> bonusVOList = new ArrayList<>();
        PageHelper.startPage(pageParam.getPageNum(), pageParam.getPageSize());
        IPage<Bonus> bonuses = bonusMapper.selectPage(new Page<>(pageParam.getPageNum(), pageParam.getPageSize()),wrapper);

        for (Bonus bonus:bonuses.getRecords()) {
            BonusVO bonusVO = new BonusVO();
            bonusVO.setFromUserId(bonus.getFromUserId());
            bonusVO.setRegisterTime(memberMapper.selectByPrimaryKey(Integer.valueOf(String.valueOf(bonus.getFromUserId()))).getRegisterTime());
            bonusVO.setThresholdNumber(bonus.getThresholdNumber());
            bonusVO.setAverageThreshold(bonus.getAverageThreshold());
            bonusVO.setBonus(bonus.getBonus());
            bonusVO.setCreateTime(bonus.getCreateTime());
            bonusVO.setHierarchy(bonus.getHierarchy());
            bonusVOList.add(bonusVO);
        }
        return new Page<BonusVO>().setRecords(bonusVOList).setSize(bonusMapper.selectCount(wrapper));
    }
}
