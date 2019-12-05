package com.oax.admin.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.oax.admin.service.ResourcesService;
import com.oax.entity.admin.Resources;
import com.oax.entity.admin.vo.MenuVo;
import com.oax.mapper.admin.ResourcesMapper;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/5/29
 * Time: 17:54
 */
@Service
public class ResourcesServiceImpl implements ResourcesService {

    @Autowired
    private ResourcesMapper resourcesMapper;


    @Override
    public List<Resources> selectAll() {

        return resourcesMapper.selectAll();
    }

    @Override
    public List<Resources> selectUserResources(Integer userId, Integer resourcestype) {
        return resourcesMapper.selectUserResources(userId, resourcestype);
    }

    @Override
    public PageInfo<Resources> selectByPage(int pageNo, int pageSize) {

        PageHelper.startPage(pageNo, pageSize);
        List<Resources> resources = resourcesMapper.selectAll();
        return new PageInfo<>(resources);
    }

    @Override
    public List<MenuVo> selectResourcesListWithSelected(Integer roleId) {

        return resourcesMapper.selectResourcesListWithSelected(roleId);
    }

    @Override
    public int save(Resources resources) {
        int insert = resourcesMapper.insert(resources);
        return insert;
    }

    @Override
    public int delete(int resourcesId) {
        int i = resourcesMapper.deleteByPrimaryKey(resourcesId);
        return i;
    }

    @Override
    public List<Resources> selectAllByType(int resourcesType) {
        return resourcesMapper.selectAllByType(resourcesType);
    }

    @Override
    public List<MenuVo> selectUserMenu(Integer userid) {
        return resourcesMapper.selectUserMenu(userid);
    }
}
