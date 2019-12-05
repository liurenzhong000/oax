/**
 *
 */
package com.oax.admin.service.impl;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.oax.admin.service.UserCoinDetailService;
import com.oax.admin.service.UserCoinService;
import com.oax.common.EmptyHelper;
import com.oax.entity.admin.param.*;
import com.oax.entity.enums.UserCoinDetailType;
import com.oax.entity.front.BonusInfo;
import com.oax.entity.front.UserCoin;
import com.oax.entity.front.UserCoinFreezingDetail;
import com.oax.mapper.front.BonusInfoMapper;
import com.oax.mapper.front.UserCoinFreezingDetailMapper;
import com.oax.mapper.front.UserCoinMapper;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.oax.admin.enums.OperationLogEnum;
import com.oax.admin.exception.MyException;
import com.oax.admin.service.OperationLogService;
import com.oax.admin.service.UserInfoService;
import com.oax.admin.util.UserUtils;
import com.oax.common.enums.UserStatusEnum;
import com.oax.entity.admin.Lv2CheckLog;
import com.oax.entity.admin.OperationLog;
import com.oax.entity.admin.User;
import com.oax.entity.admin.UserAudit;
import com.oax.entity.admin.UserInfo;
import com.oax.entity.admin.vo.UserAuditLogVo;
import com.oax.entity.admin.vo.UserDetailsUpVo;
import com.oax.entity.admin.vo.UserDetailsVo;
import com.oax.entity.admin.vo.UserInfoAuditVo;
import com.oax.entity.admin.vo.UserInviteVo;
import com.oax.entity.front.Member;
import com.oax.mapper.admin.Lv2CheckLogMapper;
import com.oax.mapper.admin.UserInfoMapper;
import com.oax.mapper.front.MemberMapper;

/**
 * @author ：xiangwh
 * @ClassName:：UserInfoServiceImpl
 * @Description：
 * @date ：2018年6月2日 下午6:45:47
 */
@Service
public class UserInfoServiceImpl implements UserInfoService {

    @Autowired
    private UserInfoMapper userInfoMapper;
    @Autowired
    private Lv2CheckLogMapper lv2CheckLogMapper;

    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private UserCoinMapper userCoinMapper;

    @Autowired
    private UserCoinService userCoinService;

    @Autowired
    private UserCoinDetailService userCoinDetailService;

    @Autowired
    private OperationLogService operationLogService;

    @Autowired
    private BonusInfoMapper bonusInfoMapper;
    @Autowired
    private UserCoinFreezingDetailMapper userCoinFreezingDetailMapper;

    //分页查询用户列表
    @Override
    public PageInfo<com.oax.entity.admin.vo.UserInfoVo> selectByPage(MemberParam memberParam) {
        PageHelper.startPage(memberParam.getPageNum(), memberParam.getPageSize());
        List<com.oax.entity.admin.vo.UserInfoVo> userList = userInfoMapper.findList(memberParam);
        return new PageInfo<>(userList);
    }

    //锁定用户账号 //解锁用户账号
    @Override
    public Integer updateLockStatus(UserLockStatusParam userLockStatus) {
        Integer count = userInfoMapper.getByUserIdQueryLockStatus(userLockStatus.getUserId());
        UserLockStatusParam lockStatus = new UserLockStatusParam();
        if (count == UserInfo.LOCK_STATUS_ZERO) {
            lockStatus.setUserId(userLockStatus.getUserId());
            lockStatus.setLockStatus(userLockStatus.getLockStatus());
            count = userInfoMapper.updateLockStatusZero(lockStatus);
        } else if (count == UserInfo.LOCK_STATUS_ONE) {
            lockStatus.setUserId(userLockStatus.getUserId());
            lockStatus.setLockStatus(userLockStatus.getLockStatus());
            count = userInfoMapper.updateLockStatusOne(lockStatus);
        }
        return count;
    }

    @Override
    public PageInfo<UserDetailsUpVo> get(Integer userId, PageParam pageParam) {
        PageHelper.startPage(pageParam.getPageNum(), pageParam.getPageSize());
        List<UserDetailsUpVo> list = userInfoMapper.get(userId);
        return new PageInfo<>(list);
    }

    @Override
    public PageInfo<UserDetailsVo> getByUserIdQueryUserDetails(UserDetailsParam userDetailsParam) {
        PageHelper.startPage(userDetailsParam.getPageNum(), userDetailsParam.getPageSize());
        List<UserDetailsVo> list = userInfoMapper.getByUserIdQueryUserDetails(userDetailsParam);

        OperationLog operationLog = new OperationLog();

        operationLog.setAdminId(UserUtils.getShiroUser().getId());
        operationLog.setUserId(userDetailsParam.getUserId());
        operationLog.setType(OperationLogEnum.USER_DETAIL_ASSET.getType());
        operationLogService.operationLog(operationLog);
        return new PageInfo<>(list);
    }

    @Override
    public PageInfo<UserInviteVo> getByUserIdQueryUserInvite(UserInviteParam userInviteParam) {
        //查询出3级的用户id
        Map<Integer, Object> inviteIdMap = inviteIdMap(userInviteParam.getUserId());
        if (EmptyHelper.isEmpty(inviteIdMap)) {
            return new PageInfo<>(new ArrayList<>());
        }
        PageHelper.startPage(userInviteParam.getPageNum(), userInviteParam.getPageSize());
//        List<UserInviteVo> inviteUserList = userInfoMapper.getByUserIdQueryUserInvite(userInviteParam);
        List<UserInviteVo> inviteUserList = userInfoMapper.getByUserIdQueryUserInviteInUserIds(userInviteParam, inviteIdMap.keySet());
        inviteUserList.forEach(item -> item.setTier(inviteIdMap.get(item.getUserId()).toString()));
        return new PageInfo<>(inviteUserList);
    }

    @Override
    public PageInfo<UserInfoAuditVo> queryUserList(UserInfoAuditParam userInfo) {
        PageHelper.startPage(userInfo.getPageNum(), userInfo.getPageSize());
        userInfo.setLevel(UserInfoAuditParam.LEVEL_TYPE_ONE);
        userInfo.setCheckStatus(UserInfoAuditParam.CHECK_STATUS_CHECK_PENDING);
        userInfo.setLockStatus(UserInfoAuditParam.LOCK_STATUS_ZERO);
        List<UserInfoAuditVo> list = userInfoMapper.queryUserList(userInfo);
        return new PageInfo<>(list);
    }

    public Map<Integer, Object> inviteIdMap(Integer userId) {
        Map<Integer, Object> idMaps = new LinkedHashMap<>();
        List<Integer> oneLevelUserIds = new ArrayList<>();
        List<Integer> twoLevelUserIds = new ArrayList<>();
        List<Integer> threeLevelUserIds = new ArrayList<>();
        oneLevelUserIds = memberMapper.getIdsByFromUserId(userId);
        oneLevelUserIds.forEach(item -> idMaps.put(item, "一"));
        if (!EmptyHelper.isEmpty(oneLevelUserIds)) {
            twoLevelUserIds = memberMapper.getIdsByFromUserIds(oneLevelUserIds);
            twoLevelUserIds.forEach(item -> idMaps.put(item, "二"));
        }
        if (!EmptyHelper.isEmpty(twoLevelUserIds)) {
            threeLevelUserIds = memberMapper.getIdsByFromUserIds(twoLevelUserIds);
            threeLevelUserIds.forEach(item -> idMaps.put(item, "三"));
        }
        return idMaps;
    }

    @Override
    public List<UserAuditLogVo> getUserAuditPage(Integer userId) {
        List<UserAuditLogVo> list = userInfoMapper.getUserAuditPage(userId);
        return list;
    }

    @Override
    @Transactional
    public Integer passTheAudit(Integer userId) {
        UserInfoAuditParam user = new UserInfoAuditParam();
        Integer level = userInfoMapper.queryUserLevel(userId);
        Integer count = 0;
        if (level == UserInfoAuditParam.LEVEL_TYPE_ONE) {
            user.setId(userId);
            user.setLevel(UserInfoAuditParam.LEVEL_TYPE_TWO);
            user.setCheckStatus(UserStatusEnum.PASS_VERIFY.getStatus());
            user.setCheckTime(new Date());
            count = userInfoMapper.updateLevel(user);
        }
        if (count > 0) {
            Lv2CheckLog lv2CheckLog = new Lv2CheckLog();
            User shiroUser = UserUtils.getShiroUser();
            lv2CheckLog.setUserId(userId);
            lv2CheckLog.setStatus(Lv2CheckLog.STATUS_PASS_THE_AUDIT);
            lv2CheckLog.setAdminId(shiroUser.getId());
            Date data = new Date();
            lv2CheckLog.setCreateTime(data);
            lv2CheckLog.setUpdateTime(data);
            lv2CheckLog.setRemark("审核通过");
            count = lv2CheckLogMapper.insert(lv2CheckLog);

            //在某个时间段之后注册的用户通过验证，上下级都获得一个BHB
//            giveCoinWhenCheck(userId);
        }
        return count;
    }

    private void giveCoinWhenCheck(Integer userId){
        Member user = memberMapper.selectByPrimaryKey(userId);
        if (user.getCheckStatus() == UserStatusEnum.PASS_VERIFY.getStatus() && user.getRegisterTime().getTime() >= 1545840000000L){//2018-12-27 00:00:00
            //赠送一个BHB
            addBalanceWithType(userId, BigDecimal.ONE,"", UserCoinDetailType.ID_CHECK);
            if (user.getFromUserId() != null) {
                Member fromUser = memberMapper.selectByPrimaryKey(user.getFromUserId());
                if (fromUser != null) {
                    //赠送BHB
                    addBalanceWithType(fromUser.getId(), BigDecimal.ONE,"", UserCoinDetailType.ID_CHECK);
                }
            }
        }
    }

    private void addBalanceWithType(Integer userId, BigDecimal qty, String targetId, UserCoinDetailType type) {
        UserCoin betUserCoin = userCoinService.selectAndInsert(userId, 54);
        userCoinMapper.addBanlance(qty, 54, userId, betUserCoin.getVersion());
        userCoinDetailService.addUserCoinDetail(betUserCoin, targetId, type);
    }

    @Override
    @Transactional
    public Integer notPassAudit(UserAuditParam userAudit) {
        Integer count = 0;
        Member member = userInfoMapper.selectById(userAudit.getUserId());
        if (!member.getCheckStatus().equals(1)){
            throw new MyException("状态必须为待审核状态,才能审核");
        }
        member.setCheckStatus(UserStatusEnum.NOT_PASS.getStatus());
        memberMapper.updateByPrimaryKeySelective(member);
        if (userAudit.getUserId() != null) {
            User shiroUser = UserUtils.getShiroUser();
            Lv2CheckLog lv2CheckLog = new Lv2CheckLog();
            lv2CheckLog.setUserId(userAudit.getUserId());
            lv2CheckLog.setStatus(Lv2CheckLog.STATUS_NOT_PASS);
            lv2CheckLog.setAdminId(shiroUser.getId());
            lv2CheckLog.setRemark("审核未通过:" + userAudit.getRemark());
            count = lv2CheckLogMapper.insert(lv2CheckLog);
        }

        return count;
    }

    @Override
    public Integer openLV3(Integer userId) {
        UserInfoAuditParam user = new UserInfoAuditParam();
        Integer level = userInfoMapper.queryUserLevel(userId);
        Integer count = 0;
        //判断该用户lv是否是等级2
        if (level == UserAudit.LEVEL_TYPE_TWO) {
            user.setId(userId);
            user.setLevel(UserAudit.LEVEL_TYPE_THREE);
            user.setCheckTime(new Date());
            count = userInfoMapper.updateLevel(user);
        }
        return count;
    }

    @Override
    public UserAudit getAuditUserByUserId(int userId) {
        return userInfoMapper.getAuditUserByUserId(userId);
    }

    @Override
    public Member selectById(Integer userId) {
        return userInfoMapper.selectById(userId);
    }

    @Override
    public Integer updateUser(Member member) {
        return memberMapper.updateByPrimaryKeySelective(member);
    }

    @Override
    public Integer updateUserForLv3(Member member) {

        memberMapper.updateByPrimaryKeySelective(member);

        User shiroUser = UserUtils.getShiroUser();
        Lv2CheckLog lv2CheckLog = new Lv2CheckLog();
        lv2CheckLog.setAdminId(shiroUser.getId());
        lv2CheckLog.setUserId(member.getId());
        lv2CheckLog.setStatus(1);
        if (member.getLevel().equals(UserInfo.LEVEL_TYPE_THREE)) {

            lv2CheckLog.setRemark("审核LV3通过");
        } else {
            lv2CheckLog.setRemark("审核LV3降级");
        }
        return lv2CheckLogMapper.insert(lv2CheckLog);
    }

    @Override
    public void freezing(Integer userId, boolean freezing) {
        Member user = memberMapper.selectByPrimaryKey(userId);
        user.setFreezing(freezing);
        memberMapper.updateByPrimaryKeySelective(user);
    }

    @Override
    public boolean partFreezing(PartFrezzingParam partFreezing) {
        UserCoin userCoin =  userCoinMapper.selectByUserIdAndCoinId(partFreezing.getUserId(),partFreezing.getCoinId());
        UserCoinFreezingDetail userCoinFreezingDetail = new UserCoinFreezingDetail();
        if(partFreezing.getStatus()==0){
            if(partFreezing.getQty() != null){
                if(userCoin.getBanlance().compareTo(partFreezing.getQty())>=0){
                    userCoinMapper.freezingByUserIdAndCoinId(partFreezing.getQty(),partFreezing.getCoinId(),partFreezing.getUserId(),userCoin.getVersion());
                    userCoinFreezingDetail.setQty(partFreezing.getQty());
                }else {
                    return false;
                }
            }
            userCoinFreezingDetail.setUserName(partFreezing.getUserName());
            userCoinFreezingDetail.setCoinId(partFreezing.getCoinId());
            userCoinFreezingDetail.setCreateTime(new Date());
            userCoinFreezingDetail.setFreezingTime(partFreezing.getFreezingTime());
            userCoinFreezingDetail.setUnfreezeTime(partFreezing.getUnfreezeTime());
            userCoinFreezingDetail.setStatus(partFreezing.getStatus());
            userCoinFreezingDetail.setUserId(partFreezing.getUserId());
            userCoinFreezingDetailMapper.insert(userCoinFreezingDetail);
            return true;
        }else {
            if(partFreezing.getQty() != null){
                if(userCoin.getBanlance().compareTo(partFreezing.getQty())>=0){
                    BonusInfo bonusInfo = bonusInfoMapper.selectOne(new QueryWrapper<BonusInfo>().lambda().eq(BonusInfo::getUserId,partFreezing.getUserId()));
                    if(bonusInfo != null){
                        if(bonusInfo.getBanlace().compareTo(partFreezing.getQty())>=0){
                            userCoinMapper.cancelFreezingByUserIdAndCoinId(partFreezing.getQty(),partFreezing.getCoinId(),partFreezing.getUserId(),userCoin.getVersion());
                            userCoinFreezingDetail.setQty(partFreezing.getQty());
                        }else {
                            return false;
                        }
                    }
                }else {
                    return false;
                }
            }
            userCoinFreezingDetail.setCoinId(partFreezing.getCoinId());
            userCoinFreezingDetail.setCreateTime(new Date());
            userCoinFreezingDetail.setUserName(partFreezing.getUserName());
            userCoinFreezingDetail.setFreezingTime(partFreezing.getFreezingTime());
            userCoinFreezingDetail.setUnfreezeTime(partFreezing.getUnfreezeTime());
            userCoinFreezingDetail.setStatus(partFreezing.getStatus());
            userCoinFreezingDetail.setUserId(partFreezing.getUserId());
            userCoinFreezingDetailMapper.insert(userCoinFreezingDetail);
            return true;
        }
    }

}