package com.oax.admin.service;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.oax.entity.admin.Resources;
import com.oax.entity.admin.vo.MenuVo;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/5/29
 * Time: 17:52
 */
public interface ResourcesService {
    List<Resources> selectAll();

    List<Resources> selectUserResources(Integer userId, Integer resourcestype);

    /**
     * 根据 分页条件查询信息
     *
     * @param pageNo   开始页
     * @param pageSize 一页展示数
     * @return
     */
    PageInfo<Resources> selectByPage(int pageNo, int pageSize);

    /**
     * 获取对应的资源 -> 选中角色对应资源
     *
     * @param roleId 角色id
     * @return
     */
    List<MenuVo> selectResourcesListWithSelected(Integer roleId);


    int save(Resources resources);


    int delete(int resourcesId);

    /**
     * 根据资源类型 获取资源
     *
     * @param resourcesType
     * @return
     */
    List<Resources> selectAllByType(int resourcesType);


    /**
     * 获取用户对应 菜单
     *
     * @param userid userId
     * @return
     */
    List<MenuVo> selectUserMenu(Integer userid);
}
