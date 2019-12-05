package com.oax.admin.controller;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.oax.admin.enums.UserStatusEnum;
import com.oax.admin.service.UserRoleService;
import com.oax.admin.service.UserService;
import com.oax.admin.util.PasswordHelper;
import com.oax.admin.util.UserUtils;
import com.oax.common.ResultResponse;
import com.oax.entity.admin.User;
import com.oax.entity.admin.UserRole;
import com.oax.entity.admin.param.UserPageParam;
import com.oax.entity.admin.param.UserResetPwdParam;

import lombok.extern.slf4j.Slf4j;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/5/31
 * Time: 10:30
 * 用户 Controller
 */
@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRoleService userRoleService;


    @PostMapping("/page")
    public ResultResponse getAll(@RequestBody UserPageParam userPageParam) {
        PageInfo<User> pageInfo = userService.selectByPage(userPageParam);

        return new ResultResponse(true, pageInfo);
    }

    @PostMapping("/userRole")
    public ResultResponse saveUserRoles(@RequestBody UserRole userRole) {

        if (StringUtils.isEmpty(userRole.getRoleidArray()) ||
                userRole.getUserid() == null) {
            return new ResultResponse(false, "用户id或角色id不能为空");
        }

        try {
            userRoleService.insertUserRole(userRole);
            log.info("添加角色成功:::{}", JSON.toJSONString(userRole));
            return new ResultResponse(true, "用户角色添加成功");
        } catch (Exception e) {

            log.error("添加角色失败:", e);
            return new ResultResponse(false, "添加角色失败");
        }

    }

    @PostMapping
    public ResultResponse saveUser(@RequestBody @Valid User user, BindingResult result) {


        if (result.hasErrors()) {
            return new ResultResponse(false, result.getFieldError().getDefaultMessage());
        }

        User u = userService.selectByUsername(user.getUsername());

        if (u != null) {
            return new ResultResponse(false, "用户名已存在");
        }
        try {
            user.setEnable(UserStatusEnum.ENABLE.getStatus());
            PasswordHelper passwordHelper = new PasswordHelper();
            passwordHelper.encryptPassword(user);
            userService.insert(user);
            log.info("添加用户成功 user:::{}", JSON.toJSONString(user));
            return new ResultResponse(true, "添加用户成功");
        } catch (Exception e) {
            log.error("添加用户失败", e);
            return new ResultResponse(false, "添加用户失败");
        }
    }

    @DeleteMapping("/{userId}")
    public ResultResponse delete(@PathVariable(name = "userId") Integer userId) {

        User user = userService.selectById(userId);
        if (user != null && "admin".equals(user.getUsername())) {
            return new ResultResponse(false, "admin帐户不允许删除");
        }

        try {
            userService.delUser(userId);
            log.info("删除用户成功 user::{}", userId);
            return new ResultResponse(true, "删除用户成功");
        } catch (Exception e) {
            log.error("删除用户失败", e);
            return new ResultResponse(false, "删除用户失败");
        }
    }

    @PutMapping("/reset/password")
    public ResultResponse resetPassword(@RequestBody @Valid UserResetPwdParam userResetPwdParam, BindingResult result) {

        if (result.hasErrors()) {
            return new ResultResponse(false, result.getFieldError().getDefaultMessage());
        }


        String oldPwd = userResetPwdParam.getOldPwd();

        String newPwd = userResetPwdParam.getNewPwd();

        if (oldPwd.equals(newPwd)) {
            return new ResultResponse(false, "新密码不能与旧密码相同");
        }

        User shiroUser = UserUtils.getShiroUser();

        User user = new User();
        user.setPassword(oldPwd);
        user.setUsername(shiroUser.getUsername());
        PasswordHelper passwordHelper = new PasswordHelper();

        passwordHelper.encryptPassword(user);

        if (!StringUtils.equals(user.getPassword(), shiroUser.getPassword())) {
            return new ResultResponse(false, "密码输入错误");
        }

        shiroUser.setPassword(newPwd);

        passwordHelper.encryptPassword(shiroUser);

        try {
            userService.update(shiroUser);
            //修改密码后 推出登录
            UserUtils.logout();

            return new ResultResponse(true, "修改成功");
        } catch (Exception e) {
            log.error("修改用户密码失败:::", e);
            return new ResultResponse(false, "修改失败");
        }
    }

    @PostMapping("/{userId}/enable/{enable}")
    public ResultResponse enableUser(@PathVariable(name = "userId") int userId,
                                     @PathVariable(name = "enable") int enable) {

        User user = userService.selectById(userId);

        if (user == null) {
            return new ResultResponse(false, "用户不存在");
        }

        if (UserStatusEnum.ENABLE.getStatus() == user.getEnable()) {
            //用户状态为 启用
            if (enable == UserStatusEnum.UNABLE.getStatus()) {
                //设置为 禁用
                user.setEnable(enable);
            } else {
                return new ResultResponse(false, "用户为启用状态,不能再次设置启用");
            }
        } else {
            //用户状态为 未启用
            if (enable == UserStatusEnum.ENABLE.getStatus()) {
                //设置启用
                user.setEnable(enable);
            } else {
                return new ResultResponse(false, "用户为禁用状态,不能再次设置禁用");
            }
        }

        userService.update(user);
        return new ResultResponse(true, "状态更新成功");
    }


}
