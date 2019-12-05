package com.oax.mapper.front;

import java.util.List;

import com.oax.entity.admin.param.AppPageParam;
import com.oax.entity.front.App;

public interface AppMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(App record);

    int insertSelective(App record);

    App selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(App record);

    int updateByPrimaryKeyWithBLOBs(App record);

    int updateByPrimaryKey(App record);

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
    List<App> selectByAppPageParam(AppPageParam appPageParam);

    /**
     * @param ：@return
     * @return ：App
     * @throws
     * @Title：checkVersion
     * @Description：检测更新版本号
     */
    App checkVersion(Integer type);

	/** 
	* @Title：getLastVersionUrl 
	* @Description：获取最新下载地址
	* @throws 
	*/
	String getLastVersionUrl(Integer type);
}