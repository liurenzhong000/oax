package com.oax.admin.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.oax.admin.service.UserInfoService;
import com.oax.common.ResultResponse;
import com.oax.entity.admin.UserAudit;
import com.oax.entity.admin.UserInfo;
import com.oax.entity.admin.param.UserAuditParam;
import com.oax.entity.admin.param.UserInfoAuditParam;
import com.oax.entity.admin.vo.UserAuditLogVo;
import com.oax.entity.admin.vo.UserInfoAuditVo;
import com.oax.entity.front.Member;

/**
 * 等级审核相关
 */
@RequestMapping("audit")
@RestController
public class LevelAuditController {

    @Autowired
    private UserInfoService userInfoService;


    //等级审核: 所有用户列表
    //check_status 为1的用户(表示待审核用户) and level等级为1的用户这里
    // 只查等级为1的用户 and lock_status为0 正常用户
    @PostMapping
    public ResultResponse queryLV1UserList(@RequestBody UserInfoAuditParam userInfo) {
        PageInfo<UserInfoAuditVo> userList = userInfoService.queryUserList(userInfo);
        return new ResultResponse(true, userList);
    }


    //用户管理->用户列表->详情->操作员log
    @GetMapping("/{userId}/auditlog")
    public ResultResponse getUserAuditPage(@PathVariable(name = "userId") Integer userId) {
        List<UserAuditLogVo> userAuditPage = userInfoService.getUserAuditPage(userId);
        return new ResultResponse(true, userAuditPage);
    }

    @GetMapping("/{userId}")
    public ResultResponse getUser(@PathVariable(name = "userId") int userId) {
        UserAudit userAudit = userInfoService.getAuditUserByUserId(userId);
        return new ResultResponse(true, userAudit);
    }

    //审核页面
    //二审:审核通过 LV1 升级为 LV2
    @GetMapping("/passTheAudit/{userId}")
    public ResultResponse passTheAudit(@PathVariable(name = "userId") Integer userId) {
        Integer count = userInfoService.passTheAudit(userId);
        if (count > 0) {
            return new ResultResponse(true, "审核通过");
        } else {
            return new ResultResponse(false, "审核失败");
        }
    }

    //二审:审核未通过
    @PostMapping("notPassAudit")
    public ResultResponse notPassAudit(@RequestBody @Valid UserAuditParam userAudit) {
        Integer count = userInfoService.notPassAudit(userAudit);
        if (count > 0) {
            return new ResultResponse(true, "审核未通过");
        } else {
            return new ResultResponse(false, "审核失败");
        }
    }

    //用户开通LV3权限
    @GetMapping("/userLV3/{isopen}/{userId}")
    public ResultResponse openLV3(@PathVariable(name = "userId") Integer userId,
                                  @PathVariable(name = "isopen") Integer isopen) {
        Member member = userInfoService.selectById(userId);
        if (isopen == 1
                && member.getLevel().equals(UserInfo.LEVEL_TYPE_TWO)) {
            //开启
            member.setLevel(UserInfo.LEVEL_TYPE_THREE);
        } else if (isopen == 0
                && member.getLevel().equals(UserInfo.LEVEL_TYPE_THREE)) {
            //关闭
            member.setLevel(UserInfo.LEVEL_TYPE_TWO);
        }else {
            return new ResultResponse(false,"状态有误");
        }
        Integer count = userInfoService.updateUserForLv3(member);
        if (count > 0) {
            return new ResultResponse(true, "审核通过");
        } else {
            return new ResultResponse(false, "审核失败");
        }
    }
}
