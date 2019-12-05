package com.oax.mapper.admin;

import com.oax.entity.admin.UserAudit;
import com.oax.entity.admin.param.*;
import com.oax.entity.admin.vo.*;
import com.oax.entity.front.Member;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

@Mapper
public interface UserInfoMapper {
    /**
     * 查询所有注册的用户的综合信息
     */
    public List<com.oax.entity.admin.vo.UserInfoVo> findList(MemberParam memberParam);

    /**
     * 锁定用户账号
     */
    public Integer updateLockStatusZero(UserLockStatusParam userLockStatus);

    /**
     * 解锁用户账号
     */
    public Integer updateLockStatusOne(UserLockStatusParam userLockStatus);

    /**
     * 根据用户id查询出该用户信息
     */
    public List<UserDetailsUpVo> get(Integer userId);

    /**
     * 根据用户id查询出该用户详细信息列表
     */
    List<UserDetailsVo> getByUserIdQueryUserDetails(UserDetailsParam userDetailsParam);

    /**
     * 根据用户id查询 详情: 邀请记录
     */
    List<UserInviteVo> getByUserIdQueryUserInvite(UserInviteParam userInviteParam);

    List<UserInviteVo> getByUserIdQueryUserInviteInUserIds(@Param("userInviteParam") UserInviteParam userInviteParam, @Param("userIds") Set<Integer> userIds);

    /**
     * 等级审核->所有用户列表信息
     */
    List<UserInfoAuditVo> queryUserList(UserInfoAuditParam userInfo);

    /**
     * 审核页面
     * 用户管理->用户列表->详情-> 操作员记录
     */
    List<UserAuditLogVo> getUserAuditPage(Integer userId);

    /**
     * 审核页面->审核通过 改变level 2等级为3
     */
    Integer updateLevel(UserInfoAuditParam user);

    /**
     * 审核页面->审核通过 根据用户id查询出该用户目前的等级
     */
    Integer queryUserLevel(Integer userId);

    /**
     * 查询该用户是否是锁定状态
     */
    Integer getByUserIdQueryLockStatus(Integer id);

    /**
     * 通过id 获取 对应用户需审核信息
     *
     * @param userId
     * @return
     */
    UserAudit getAuditUserByUserId(int userId);

    Member selectById(Integer userId);

}
