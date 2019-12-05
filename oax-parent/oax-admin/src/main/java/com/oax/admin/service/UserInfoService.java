package com.oax.admin.service;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.oax.entity.admin.UserAudit;
import com.oax.entity.admin.param.*;
import com.oax.entity.admin.vo.UserAuditLogVo;
import com.oax.entity.admin.vo.UserDetailsUpVo;
import com.oax.entity.admin.vo.UserDetailsVo;
import com.oax.entity.admin.vo.UserInfoAuditVo;
import com.oax.entity.admin.vo.UserInviteVo;
import com.oax.entity.front.Member;

public interface UserInfoService {
    /**
     * 所有用户列表信息
     */
    PageInfo<com.oax.entity.admin.vo.UserInfoVo> selectByPage(MemberParam param);

    /**
     * 锁定用户账号
     * 解锁用户账号
     */
    Integer updateLockStatus(UserLockStatusParam userLockStatus);

    /**
     * 根据用户id查询出该用户信息
     */
    PageInfo<UserDetailsUpVo> get(Integer userId, PageParam pageParam);

    /**
     * 根据用户id查询出该用户详细信息列表
     */
    PageInfo<UserDetailsVo> getByUserIdQueryUserDetails(UserDetailsParam userDetailsParam);

    /**
     * 根据用户id查询 详情: 邀请记录
     */
    PageInfo<UserInviteVo> getByUserIdQueryUserInvite(UserInviteParam userInviteParam);

    /**
     * 等级审核->所有用户列表信息
     */
    PageInfo<UserInfoAuditVo> queryUserList(UserInfoAuditParam userInfo);

    /**
     * 审核页面
     * 用户管理->用户列表->详情-> 获取操作员记录
     */
    List<UserAuditLogVo> getUserAuditPage(Integer userId);

    /**
     * 审核页面->审核通过
     */
    Integer passTheAudit(Integer userId);

    /**
     * 审核页面->审核未通过
     */
    Integer notPassAudit(UserAuditParam userAudit);

    /**
     * 用户开通LV3权限
     */
    Integer openLV3(Integer userId);


    /**
     * 通过id 获取 对应用户需审核信息
     *
     * @param userId
     * @return
     */
    UserAudit getAuditUserByUserId(int userId);

    Member selectById(Integer userId);

    Integer updateUser(Member member);

    Integer updateUserForLv3(Member member);

    void freezing(Integer userId, boolean freezing);

    boolean partFreezing(PartFrezzingParam partFrezzingParam);
}

