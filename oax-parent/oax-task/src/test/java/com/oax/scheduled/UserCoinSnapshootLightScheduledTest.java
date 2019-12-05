package com.oax.scheduled;

import com.oax.OaxApiApplicationTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Auther: hyp
 * @Date: 2019/1/9 15:36
 * @Description:
 */
@Component
public class UserCoinSnapshootLightScheduledTest extends OaxApiApplicationTest {

    @Autowired
    private UserCoinSnapshootLightScheduled snapshootLightScheduled;

    @Test
    public void test(){
        snapshootLightScheduled.createUserCoinSnapshoot();
        try {
            Thread.sleep(100000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
