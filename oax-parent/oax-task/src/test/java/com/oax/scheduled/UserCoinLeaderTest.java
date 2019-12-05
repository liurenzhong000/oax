package com.oax.scheduled;

import com.oax.OaxApiApplicationTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class UserCoinLeaderTest extends OaxApiApplicationTest {

    @Autowired
    private LeaderStatisticsScheduled statisticsScheduled;

    @Test
    public void test() throws InterruptedException {
        statisticsScheduled.leaderSubordinateStatistics();
        System.out.println("||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
        Thread.sleep(1000000);
    }
}
