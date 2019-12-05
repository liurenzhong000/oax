package com.oax.common.enums;

import lombok.Getter;

/** 
* @ClassName:：KlineTypeEnum 
* @Description： k线图时间类型枚举
* @author ：xiangwh  
* @date ：2018年7月3日 下午6:05:35 
*  
*/
@Getter
public enum KlineTypeEnum {
	//1分钟
	KLINE_1_MIN(1),
	//5分钟
	KLINE_5_MIN(5),
	//10分钟
	KLINE_10_MIN(10),
	//30分钟
	KLINE_30_MIN(30),
	//60分钟
	KLINE_1_HOUR(60),
	//4小时
	KLINE_4_HOUR(60*4),
	//一天
	KLINE_1_DAY(60*24),
	//一个星期
	KLINE_1_WEEK(60*24*7),
	//1一个月
	KLINE_1_MONTH(60*24*30);
	private int minType;
	private KlineTypeEnum(int minType) {
		this.minType = minType;
	}
	
	
}
