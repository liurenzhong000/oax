package com.oax.admin.service.impl;

import com.oax.OaxApiApplicationTest;
import com.oax.admin.service.bonus.BCBBonusService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Auther: hyp
 * @Date: 2019/1/27 15:19
 * @Description:
 */
public class BCBBonusServiceImplTest extends OaxApiApplicationTest {

    @Autowired
    private BCBBonusService bcbBonusService;

    @Test
    public void test(){
        bcbBonusService.bonus();
        try {
            Thread.sleep(10000000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
