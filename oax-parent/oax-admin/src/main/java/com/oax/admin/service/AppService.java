package com.oax.admin.service;

import com.github.pagehelper.PageInfo;
import com.oax.entity.admin.param.AppPageParam;
import com.oax.entity.front.App;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/6/12
 * Time: 14:16
 * app 业务接口
 */
public interface AppService {
    /**
     * app页面 分页查询
     *
     * @param appPageParam appType     设备类型 1 ios 2 android
     *                     version     状态 0未启用 1启用
     *                     pageNo      页码
     *                     pageSize    一页展示数
     *                     startTime   开始时间
     *                     endTime     结束时间
     * @return
     */
    PageInfo<App> selectByAppPageParam(AppPageParam appPageParam);

    App selectById(Integer id);

    int update(App app);

    int insert(App app);
}
