package com.oax.mapper.front;

import com.oax.OaxApiApplicationTest;
import com.oax.admin.service.bonus.BHBBonusService;
import com.oax.common.RedisUtil;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class BHBBonusTest extends OaxApiApplicationTest {

    @Autowired
    private BHBBonusService bonusService;

    @Autowired
    private RedisUtil redisUtil;

//    @Test
//    public void test(){
//        bonusService.BHBBonus("", 1);//0跑的是明天要分红的量，1判断是今天要分红的量
//        System.out.println("================");
//    }

//    @Test
//    public void testRedis(){
//        redisUtil.setString("my_test", "1111111",10);
//        String str = redisUtil.getString("my_test");
//        System.out.println(str);
//    }
}
