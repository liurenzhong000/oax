package com.oax.admin.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.oax.admin.service.AppService;
import com.oax.admin.service.UserService;
import com.oax.common.RedisUtil;
import com.oax.common.enums.RedisKeyEnum;
import com.oax.entity.admin.User;
import com.oax.entity.admin.param.AppPageParam;
import com.oax.entity.front.App;
import com.oax.mapper.front.AppMapper;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/6/12
 * Time: 14:17
 */
@Service
public class AppServiceImpl implements AppService {
    @Autowired
    private AppMapper appMapper;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private UserService userService;

    @Override
    public PageInfo<App> selectByAppPageParam(AppPageParam appPageParam) {

        PageHelper.startPage(appPageParam.getPageNum(), appPageParam.getPageSize());
        List<App> appList = appMapper.selectByAppPageParam(appPageParam);
        appList.forEach(app -> {
            Integer adminId = app.getAdminId();
            if (adminId != null) {
                User user = userService.selectById(adminId);
                app.setAdminName(user.getName());
            }
        });
        return new PageInfo<>(appList);
    }

    @Override
    public App selectById(Integer id) {

        App app = appMapper.selectByPrimaryKey(id);
        Integer adminId = app.getAdminId();
        if (adminId != null) {
            User user = userService.selectById(adminId);
            app.setAdminName(user.getName());
        }
        return app;
    }

    @Override
    public int update(App app) {
        int i = appMapper.updateByPrimaryKeySelective(app);
        redisUtil.delete(RedisKeyEnum.APP_LAST_VERSION.getKey()+app.getType());
        return i;
    }

    @Override
    public int insert(App app) {
        return appMapper.insertSelective(app);
    }
}
