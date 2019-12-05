package com.oax.scheduled;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.oax.entity.front.KlineInfo;
import com.oax.service.KlineService;

/** 
* @ClassName:：MaintainKlineTask 
* @Description： 维护k线数据
* @author ：xiangwh  
* @date ：2018年7月4日 下午9:47:13 
*  
*/
@Component
public class MaintainKlineTask {
	@Autowired
	private KlineService klineService;
	@Scheduled(cron="0 0 2 * * ?")
    @Async
	public void  maintainKline() {
		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		List<KlineInfo> klineInfoList = klineService.selectList();
		klineService.removeKline(klineInfoList);
	}
}
