package com.oax.admin.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.oax.admin.service.IBenderService;
import com.oax.admin.util.UserUtils;
import com.oax.common.RedisUtil;
import com.oax.common.enums.RedisKeyEnum;
import com.oax.entity.admin.User;
import com.oax.entity.admin.param.BannerParam;
import com.oax.entity.admin.param.BannerUpdateParam;
import com.oax.entity.admin.vo.BannerVo;
import com.oax.entity.front.Banner;
import com.oax.mapper.front.BannerMapper;

@Service
public class BenderServiceImpl implements IBenderService {
    @Autowired
    private BannerMapper bannerMapper;
    @Autowired
    private RedisUtil redisUtil;

    @Override
    public PageInfo<BannerVo> bannerManageList(BannerParam banner) {
        PageHelper.startPage(banner.getPageNum(), banner.getPageSize());
        List<BannerVo> list = bannerMapper.bannerManageList(banner);
        return new PageInfo<>(list);
    }

    @Override
    public Integer update(BannerUpdateParam banner) {
        BannerUpdateParam bann = new BannerUpdateParam();
        bann.setId(banner.getId());
        bann.setType(banner.getType());
        bann.setTitle(banner.getTitle());
        bann.setCnImage(banner.getCnImage());
        bann.setEnImage(banner.getEnImage());
        bann.setSortNo(banner.getSortNo());
        User shiroUser = UserUtils.getShiroUser();
        bann.setAdminId(shiroUser.getId());
        bann.setStatus(banner.getStatus());
        bann.setUrl(banner.getUrl());
        bann.setUpdateTime(new Date());
        Integer count = bannerMapper.update(bann);
        if (count > 0) {
            redisUtil.deleteKeys(RedisKeyEnum.BANNER_LIST.getKey() + banner.getType()+".*");
        }
        return count;
    }

    @Override
    public Integer deleteBanner(Integer id) {
        Banner banner = bannerMapper.selectById(id);
        Integer integer = bannerMapper.deleteBanner(id);
        if (integer>0){
            redisUtil.deleteKeys(RedisKeyEnum.BANNER_LIST.getKey() + banner.getType()+".*");
        }
        return integer;
    }

    @Override
    public Integer saveBanner(Banner banner) {
        Banner bann = new Banner();
        bann.setType(banner.getType());
        bann.setTitle(banner.getTitle());
        bann.setCnImage(banner.getCnImage());
        bann.setEnImage(banner.getEnImage());
        bann.setUrl(banner.getUrl());
        bann.setStatus(banner.getStatus());
        User shiroUser = UserUtils.getShiroUser();
        bann.setAdminId(shiroUser.getId());
        bann.setSortNo(banner.getSortNo());
        Date date = new Date();
        bann.setCreateTime(date);
        bann.setUpdateTime(date);
        int count = bannerMapper.insert(bann);
        if (count > 0) {
            redisUtil.deleteKeys(RedisKeyEnum.BANNER_LIST.getKey() + banner.getType()+".*");
        }
        return count;
    }
}
