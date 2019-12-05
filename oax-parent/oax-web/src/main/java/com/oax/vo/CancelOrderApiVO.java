package com.oax.vo;

import java.io.Serializable;

/** 
* @ClassName:：CancelOrderVO 
* @Description： 取消订单参数
* @author ：xiangwh  
* @date ：2018年6月26日 上午11:39:15 
*  
*/
public class CancelOrderApiVO implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer id;
	private String apiKey;
	private Integer userId;

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getApiKey() {
		return apiKey;
	}
	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}
	
}
