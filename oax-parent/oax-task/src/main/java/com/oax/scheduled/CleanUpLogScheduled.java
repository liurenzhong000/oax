package com.oax.scheduled;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.oax.service.CleanUpLogService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CleanUpLogScheduled {
	@Autowired
	private CleanUpLogService cleanUpLogService;
	
	//每月15号的凌晨2点执行
	@Scheduled(cron = "0 0 2 15 * ?")
	public void cleanUpLog() {   
        try {
        	Calendar c=Calendar.getInstance();
    		c.add(Calendar.MONTH, -1);
    		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-01 00:00:00");
    		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    		
    		int lastMonthMaxDay=c.getActualMaximum(Calendar.DAY_OF_MONTH);
    		c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), lastMonthMaxDay, 23, 59, 59);
    		
    		//按格式输出    		
    		String beginTime = sdf1.format(c.getTime()); //上月第一天
    		String endTime = sdf2.format(c.getTime()); //上月最后一天
        	cleanUpLogService.cleanUpLog(beginTime,endTime);
            log.info("---------定时任务CleanUpLogScheduled结束---------");
        } catch (Exception e) {
            log.error("--------定时任务CleanUpLogScheduled请求失败--------");
            e.printStackTrace();
        }
		
	}
}
