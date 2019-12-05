package com.oax.admin.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.oax.admin.service.RoleResourcesService;
import com.oax.admin.service.RoleService;
import com.oax.common.ResultResponse;
import com.oax.entity.admin.Role;
import com.oax.entity.admin.RoleResourcesKey;

import lombok.extern.slf4j.Slf4j;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/5/31
 * Time: 10:30
 * 角色 Controller
 */
@RestController
@RequestMapping("/roles")
@Slf4j
public class RoleController {


    @Autowired
    private RoleService roleService;

    @Autowired
    private RoleResourcesService roleResourcesService;

    @GetMapping
    public ResultResponse getAll(@RequestParam(required = false, defaultValue = "1") int pageNum,
                                 @RequestParam(required = false, defaultValue = "10") int pageSize) {

        PageInfo<Role> pageInfo = roleService.selectByPage(pageNum, pageSize);

        return new ResultResponse(true, pageInfo);
    }


    @GetMapping("/rolesWithSelected/{userId}")
    public ResultResponse queryRoleListWithSelected(@PathVariable(name = "userId") int userId) {
        List<Role> roleList = roleService.selectRoleListWithSelected(userId);
        return new ResultResponse(true, roleList);
    }


    @PostMapping("/roleResources")
    public ResultResponse saveRoleResources(@RequestBody RoleResourcesKey roleResources) {

        if (StringUtils.isEmpty(roleResources.getRoleid()) ||
                StringUtils.isEmpty(roleResources.getResourcesidArray())) {
            return new ResultResponse(false, "角色id或资源id为空");
        }


        try {
            int i = roleResourcesService.addRoleResources(roleResources);
            if (i > 0) {
                log.info("添加角色资源:::{}", JSON.toJSONString(roleResources));
                return new ResultResponse(true, "添加成功");
            } else {
                return new ResultResponse(false, "添加失败");
            }

        } catch (Exception e) {
            log.error("角色资源添加失败", e);
            return new ResultResponse(false, "角色资源添加失败");
        }
    }

    @PostMapping
    public ResultResponse add(@RequestBody @Valid Role role, BindingResult result) {

        if (result.hasErrors()) {
            return new ResultResponse(false,result.getFieldError().getDefaultMessage());
        }

        try {
            int insert = roleService.insert(role);
            if (insert > 0) {
                log.info("添加角色成功:::{}", JSON.toJSONString(role));
                return new ResultResponse(true, "添加角色成功");
            } else {
                return new ResultResponse(false, "添加角色失败");
            }
        } catch (Exception e) {
            log.error("添加角色失败", e);
            return new ResultResponse(false, "添加角色失败");
        }
    }

    @DeleteMapping("/{roleId}")
    public ResultResponse delete(@PathVariable(name = "roleId") Integer roleId) {

        if (roleId == null) {
            return new ResultResponse(false, "roleId参数错误");
        }

        try {
            int del = roleService.delRole(roleId);
            if (del > 0) {
                log.info("删除角色成功:::{}", roleId);
                return new ResultResponse(true, "删除角色成功");
            } else {
                return new ResultResponse(false, "删除角色失败");
            }
        } catch (Exception e) {
            log.error("删除角色失败", e);
            return new ResultResponse(false, "删除角色失败");
        }
    }


}
