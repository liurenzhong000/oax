package com.oax.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.oax.entity.admin.param.PageParam;
import com.oax.entity.front.UserCoinLeader;
import com.oax.mapper.front.UserCoinLeaderMapper;
import com.oax.service.UserCoinLeaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserCoinLeaderServiceImpl implements UserCoinLeaderService {

    @Autowired
    UserCoinLeaderMapper userCoinLeaderMapper;

    @Override
    public Page<UserCoinLeader> pageForWeb(PageParam pageParam, Integer userId) {
        PageHelper.startPage(pageParam.getPageNum(), pageParam.getPageSize());
        Wrapper<UserCoinLeader> wrapper = new QueryWrapper<UserCoinLeader>().lambda().eq(UserCoinLeader::getUserId, userId).orderByDesc(UserCoinLeader::getId);
        IPage<UserCoinLeader> bonuses = userCoinLeaderMapper.selectPage(new Page<>(pageParam.getPageNum(), pageParam.getPageSize()),wrapper);
        return new Page<UserCoinLeader>().setRecords(bonuses.getRecords()).setSize(userCoinLeaderMapper.selectCount(wrapper));
    }
}
