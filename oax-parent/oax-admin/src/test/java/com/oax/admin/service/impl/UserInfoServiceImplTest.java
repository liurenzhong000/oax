package com.oax.admin.service.impl;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oax.OaxApiApplicationTest;
import com.oax.admin.service.UserInfoService;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/9/14
 * Time: 16:39
 */
@Component
public class UserInfoServiceImplTest extends OaxApiApplicationTest {

    @Autowired
    private UserInfoService userInfoService;
    @Test
    public void passTheAudit() {

        Integer integer = userInfoService.passTheAudit(195703);

    }
}