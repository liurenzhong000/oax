package com.oax.service.delay;

import com.oax.service.activity.SnatchActivityService;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * @Auther: hyp
 * @Date: 2019/1/24 10:53
 * @Description: 夺宝游戏机器人队列 采用无界阻塞队列延迟添加机器人
 */
@Setter
@Getter
public class SnatchRobotDelay implements Delayed {

    private Date startDate;

    private Integer snatchActivityId;

    private SnatchActivityService snatchActivityService;

    public SnatchRobotDelay(Date startDate, Integer snatchActivityId, SnatchActivityService snatchActivityService) {
        super();
        this.startDate = startDate;
        this.snatchActivityId = snatchActivityId;
        this.snatchActivityService = snatchActivityService;
    }

    @Override
    public long getDelay(TimeUnit unit) {
        Date now = new Date();
        long diff = startDate.getTime() - now.getTime();
        return unit.convert(diff, TimeUnit.MILLISECONDS);
    }

    @Override
    public int compareTo(Delayed o) {
        long result = this.getDelay(TimeUnit.NANOSECONDS) - o.getDelay(TimeUnit.NANOSECONDS);
        if (result < 0) {
            return -1;
        } else if (result > 0) {
            return 1;
        } else {
            return 0;
        }
    }
}
