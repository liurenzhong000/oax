package com.oax.service.impl;

import com.oax.common.AssertHelper;
import com.oax.entity.admin.UserAudit;
import com.oax.entity.front.Member;
import com.oax.exception.NoAuthenticationException;
import com.oax.exception.NoBankCardException;
import com.oax.exception.NoEmailException;
import com.oax.exception.NoPhoneException;
import com.oax.mapper.ctc.BankCardMapper;
import com.oax.mapper.front.MemberMapper;
import com.oax.service.CommonCheckService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommonCheckServiceImpl implements CommonCheckService {

    @Autowired
    private BankCardMapper bankCardMapper;

    @Autowired
    private MemberMapper memberMapper;

    @Override
    public boolean hasAuthentication(Integer userId) {
        Member user = memberMapper.selectByPrimaryKey(userId);
        boolean hasAuthentication = false;
        if (user != null && user.getIdNo() != null && user.getLevel() >= UserAudit.LEVEL_TYPE_TWO){
            hasAuthentication = true;
        }
        return hasAuthentication;
    }

    @Override
    public void checkHasAuthentication(Integer userId) {
        if (!hasAuthentication(userId)) {
            throw new NoAuthenticationException("未进行实名认证，请先进行实名认证");
        }
    }

    @Override
    public boolean hasBankCard(Integer userId) {
        return bankCardMapper.hasBankCard(userId);
    }

    @Override
    public void checkHasBankCard(Integer userId) {
        if (!hasBankCard(userId)) {
            throw new NoBankCardException("未绑定银行卡，请先绑定银行卡");
        }
    }

    @Override
    public void checkHasPhone(Integer userId) {
        Member user = memberMapper.selectByPrimaryKey(userId);
        if (StringUtils.isBlank(user.getPhone())) {
            throw new NoPhoneException("未绑定手机号，请先绑定手机号");
        }
    }

    @Override
    public void checkHasEmail(Integer userId) {
        Member user = memberMapper.selectByPrimaryKey(userId);
        if (StringUtils.isBlank(user.getEmail())) {
            throw new NoEmailException("未绑定邮箱，请先绑定邮箱");
        }
    }

    @Override
    public void checkHasTransactionPassword(Integer userId) {
        Member user = memberMapper.selectByPrimaryKey(userId);
        if (StringUtils.isBlank(user.getTransactionPassword())) {
            throw new NoEmailException("未设置交易密码，请先设置交易密码");
        }
    }

    @Override
    public void checkFreezing(Integer userId) {
        AssertHelper.isTrue(!hasFreezing(userId), "您的账户资金已全部冻结，无法进行任何交易");
    }

    @Override
    public boolean hasFreezing(Integer userId) {
        Member user = memberMapper.selectByPrimaryKey(userId);
        return user.getFreezing();
    }

    @Override
    public void checkGoogleCode(Integer userId) {
        Member user = memberMapper.selectByPrimaryKey(userId);
        AssertHelper.isTrue(Integer.valueOf(1).equals(user.getGoogleStatus()), "未绑定谷歌验证，请先绑定谷歌验证");
    }

    @Override
    public boolean hasGoogleCode(Integer userId) {
        Member user = memberMapper.selectByPrimaryKey(userId);
        return Integer.valueOf(1).equals(user.getGoogleStatus());
    }
}
