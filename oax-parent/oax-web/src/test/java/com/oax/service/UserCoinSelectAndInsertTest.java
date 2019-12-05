package com.oax.service;

import com.oax.OaxApiApplicationTest;
import com.oax.entity.admin.vo.UserDetailsUpVo;
import com.oax.entity.enums.UserCoinDetailType;
import com.oax.entity.front.UserCoin;
import com.oax.entity.front.UserCoinDetail;
import com.oax.exception.VoException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Component
public class UserCoinSelectAndInsertTest extends OaxApiApplicationTest {

    @Autowired
    private UserCoinService userCoinService;

    @Autowired
    private UserCoinDetailService userCoinDetailService;



    @Test
    public void selectAndInsertTest() {

        UserCoin userCoin = userCoinService.selectAndInsert(219698, 100);
        System.out.println(userCoin);
    }

    @Test
    @Transactional
    public void addUserCoinDetail() throws Exception {
        UserCoin userCoin = userCoinService.selectAndInsert(219698, 100);
        userCoin.setBanlance(userCoin.getBanlance().add(BigDecimal.ONE));
        userCoinDetailService.addUserCoinDetail(userCoin, 0+"", UserCoinDetailType.WITHDRAW);
        if (true) {
            throw new Exception("error");
        }
    }
}
