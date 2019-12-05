package com.oax.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.oax.admin.service.UserRemarkService;
import com.oax.admin.util.UserUtils;
import com.oax.entity.admin.User;
import com.oax.entity.admin.param.PageParam;
import com.oax.entity.front.UserRemark;
import com.oax.mapper.front.UserRemarkMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @Auther: hyp
 * @Date: 2019/2/26 16:48
 * @Description:
 */
@Service
@Transactional
public class UserRemarkServiceImpl implements UserRemarkService {

    @Autowired
    private UserRemarkMapper userRemarkMapper;

    @Override
    public PageInfo<UserRemark> page(PageParam param, Integer userId) {
        PageHelper.startPage(param.getPageNum(), param.getPageSize());
        List<UserRemark> diceIncomeWins = userRemarkMapper.selectList(new QueryWrapper<UserRemark>().lambda()
                .eq(UserRemark::getUserId, userId).orderByDesc(UserRemark::getId));
        return new PageInfo<>(diceIncomeWins);
    }

    @Override
    public void saveOne(Integer userId, String remark) {
        User shiroUser = UserUtils.getShiroUser();
        UserRemark userRemark = new UserRemark();
        userRemark.setAdminName(shiroUser.getName());
        userRemark.setCreateTime(new Date());
        userRemark.setUserId(userId);
        userRemark.setRemark(remark);
        userRemarkMapper.insert(userRemark);
    }
}
