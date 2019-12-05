/*
 *
 * ResourcesMapper.java
 * Copyright(C) 2018 oax公司
 * @date 2018-05-29
 */
package com.oax.mapper.admin;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.oax.entity.admin.Resources;
import com.oax.entity.admin.vo.MenuVo;

@Mapper
public interface ResourcesMapper {
    /**
     * @mbg.generated 2018-05-29
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * @mbg.generated 2018-05-29
     */
    int insert(Resources record);

    /**
     * @mbg.generated 2018-05-29
     */
    int insertSelective(Resources record);

    /**
     * @mbg.generated 2018-05-29
     */
    Resources selectByPrimaryKey(Integer id);

    /**
     * @mbg.generated 2018-05-29
     */
    int updateByPrimaryKeySelective(Resources record);

    /**
     * @mbg.generated 2018-05-29
     */
    int updateByPrimaryKey(Resources record);

    /**
     * 查询所有
     *
     * @return
     */
    List<Resources> selectAll();

    /**
     * 通过userid查询 对应的 资源
     *
     * @param userId        用户id
     * @param resourcestype 资源类型
     * @return
     */
    List<Resources> selectUserResources(@Param("userId") Integer userId
            , @Param("type") Integer resourcestype);

    /**
     * 根据 角色 获取对应的资源
     *
     * @param roleId 角色id
     * @return
     */
    List<MenuVo> selectResourcesListWithSelected(Integer roleId);

    /**
     * 根据资源类型 获取资源
     *
     * @param resourcesType
     * @return
     */
    List<Resources> selectAllByType(int resourcesType);

    List<MenuVo> selectUserMenu(Integer userid);
}