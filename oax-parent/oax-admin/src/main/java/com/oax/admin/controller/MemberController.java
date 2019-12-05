/**
 *
 */
package com.oax.admin.controller;


import javax.validation.Valid;

import com.oax.admin.service.*;
import com.oax.common.RedisUtil;
import com.oax.entity.admin.param.*;
import com.oax.entity.admin.vo.*;
import com.oax.entity.front.UserCoinDetail;
import com.oax.entity.front.UserCoinInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.github.pagehelper.PageInfo;
import com.oax.common.PageResultResponse;
import com.oax.common.ResultResponse;
import com.oax.entity.front.Member;

import java.util.List;

/**
 * @author ：xiangwh
 * @ClassName:：UserInfoController
 * @Description： 用户管理
 * @date ：2018年6月2日 下午6:52:07
 */
@RequestMapping("member")
@RestController
public class MemberController {
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private IRechargeService rechargeService;
    @Autowired
    private IWithdrawService withdrawService;
    @Autowired
    private ITradeService tradeService;
    @Autowired
    private UserCoinService userCoinService;
    @Autowired
    private UserCoinDetailService userCoinDetailService;
    @Autowired
    private RedisUtil redisUtil;

    //用户列表: 分页 以及模糊查询 统计
    @PostMapping
    public ResultResponse selectByPage(@RequestBody MemberParam param) {
        PageInfo<UserInfoVo> page = userInfoService.selectByPage(param);
        return new ResultResponse(true, page);
    }

    //用户列表: 锁定按钮
    @PutMapping("updateLockStatus")
    public ResultResponse updateLockStatus(@RequestBody @Valid UserLockStatusParam userLockStatus,
                                           BindingResult result) {

        if (result.hasErrors()) {
            return new ResultResponse(false, result.getFieldError().getDefaultMessage());
        }

        Member member = userInfoService.selectById(userLockStatus.getUserId());

        if (member == null) {
            return new ResultResponse(false, "不存的用户");
        }


        Integer lockStatus = userLockStatus.getLockStatus();
        if (lockStatus.equals(UserLockStatusParam.LOCK_STATUS_ZERO)) {
            //解锁
            if (member.getLockStatus().equals(UserLockStatusParam.LOCK_STATUS_ZERO)) {
                return new ResultResponse(false, "此状态不能解锁");
            }
            Integer count = userInfoService.updateLockStatus(userLockStatus);
            if (count > 0) {
                return new ResultResponse(true, "解锁成功");
            }
            return new ResultResponse(true, "解锁失败");
        } else if (lockStatus.equals(UserLockStatusParam.LOCK_STATUS_ONE)) {
            //锁定
            if (member.getLockStatus().equals(UserLockStatusParam.LOCK_STATUS_ONE)) {
                return new ResultResponse(false, "此状态不能锁定");
            }
            Integer count = userInfoService.updateLockStatus(userLockStatus);
            if (count > 0) {
                return new ResultResponse(true, "锁定成功");
            }
            return new ResultResponse(true, "锁定失败");
        } else {
            return new ResultResponse(true, "亲:请稍后再试");
        }
    }

    //详情: 资产审查 用户列表 上半截 分页 统计数据
    @GetMapping("/get/{userId}")
    public ResultResponse get(@PathVariable(name = "userId") Integer userId, PageParam pageParam) {
        PageInfo<UserDetailsUpVo> userList = userInfoService.get(userId, pageParam);
        return new ResultResponse(true, userList);
    }

    //详情: 资产审查 用户列表 下半截 分页 统计数据
    @PostMapping("getCoinList")
    public ResultResponse getCoinList(@RequestBody UserDetailsParam userDetailsParam) {
        PageInfo<UserDetailsVo> userDetails = userInfoService.getByUserIdQueryUserDetails(userDetailsParam);
        return new ResultResponse(true, userDetails);
    }

    //用户管理->详情: 转入记录
    @PostMapping("getRechargeList")
    public ResultResponse getRechargeList(@RequestBody RechargesOrWithdrawParam recharges) {
        PageInfo<RechargesVo> userRecharge = rechargeService.getByUserIdQueryRecharge(recharges);
        PageResultResponse<RechargesVo> pageResultResponse = new PageResultResponse<>();
        BeanUtils.copyProperties(userRecharge, pageResultResponse);
        pageResultResponse.setParam(recharges);
        return new ResultResponse(true, pageResultResponse);
    }

    //用户管理->详情: 转出记录
    @PostMapping("getWithdrawList")
    public ResultResponse getWithdrawList(@RequestBody RechargesOrWithdrawParam withdrawsParam) {
        PageInfo<WithdrawsVo> userWithdraw = withdrawService.getByUserIdQueryWithdraw(withdrawsParam);
        return new ResultResponse(true, userWithdraw);
    }

    //详情: 交易记录
    @PostMapping("getTradeList")
    public ResultResponse getTradeList(@RequestBody TradesParam tradesParam) {
        PageInfo<TradesVo> userTrade = tradeService.getByUserIdQueryTrade(tradesParam);
        return new ResultResponse(true, userTrade);
    }

    //详情: 记录
    @PostMapping("getInviteUserList")
    public ResultResponse getInviteUserList(@RequestBody UserInviteParam userInviteParam) {
        PageInfo<UserInviteVo> userList = userInfoService.getByUserIdQueryUserInvite(userInviteParam);
        return new ResultResponse(true, userList);
    }

    //活动记录，通过user_coin_detail获取相关的记录明细
    @PostMapping("/getActivityUserDetailList")
    public ResultResponse activityUserDetail(@RequestBody ActivityCoinParam param) {
        PageInfo<UserCoinDetailVo> pageInfo = userCoinDetailService.getActivityUserDetailList(param);
        return new ResultResponse(true, pageInfo);
    }

    //获取账户的资产信息（做法币的商户做的）
    @GetMapping("/listUserCoin")
    public ResultResponse listUserCoin(Integer userId, String coinName) {
        List<UserCoinInfo> userCoinInfos = userCoinService.listUserCoinByUserId(userId, coinName);
        return new ResultResponse(userCoinInfos);
    }

    /***
     * 冻结全部资产
     */
    @PutMapping("/{userId}/freezing")
    public ResultResponse freezing(@PathVariable Integer userId, boolean freezing) {
        userInfoService.freezing(userId, freezing);
        return new ResultResponse(true, "操作成功");
    }

    /**
     * 冻结用户部分资产
     */
    @PostMapping("/partFreezing")
    public ResultResponse partFreezing(@RequestBody PartFrezzingParam partFrezzingParam) {
        boolean lock = redisUtil.tryLock("part_freezing:" + partFrezzingParam.getUserId()+"coinId:"+partFrezzingParam.getCoinId());
        if (lock) {
            try {
                partFrezzingParam.setStatus(0);
                boolean result = userInfoService.partFreezing(partFrezzingParam);
                if(result){
                    return new ResultResponse(true, "操作成功");
                }else {
                    return new ResultResponse(false, "操作失败");
                }
            } finally {
                redisUtil.unLock("part_freezing:" + partFrezzingParam.getUserId()+"coinId:"+partFrezzingParam.getCoinId());
            }
        } else {
            throw new IllegalArgumentException("重复冻结资产！");
        }

    }

    /**
     * 解冻用户部分资产
     */
    @PostMapping("/unPartFreezing")
    public ResultResponse unPartFreezing(@RequestBody PartFrezzingParam partFrezzingParam) {
        boolean lock = redisUtil.tryLock("un_part_freezing:" + partFrezzingParam.getUserId()+"coinId:"+partFrezzingParam.getCoinId());
        if (lock) {
            try {
                partFrezzingParam.setStatus(1);
                boolean result = userInfoService.partFreezing(partFrezzingParam);
                if(result){
                    return new ResultResponse(true, "操作成功");
                }else {
                    return new ResultResponse(false, "操作失败");
                }
            } finally {
                redisUtil.unLock("un_part_freezing:" + partFrezzingParam.getUserId()+"coinId:"+partFrezzingParam.getCoinId());
            }
        } else {
            throw new IllegalArgumentException("重复解冻资产！");
        }

    }

}
