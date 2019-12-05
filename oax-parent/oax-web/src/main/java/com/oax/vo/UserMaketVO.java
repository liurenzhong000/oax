package com.oax.vo;

import java.io.Serializable;

/** 
* @ClassName:：UserMaketVO 
* @Description： 用户搜藏交易对入参
* @author ：xiangwh  
* @date ：2018年6月13日 下午3:13:11 
*  
*/
public class UserMaketVO implements Serializable {

	private static final long serialVersionUID = 7417521822989459465L;
	
	private Integer coinId;

	public Integer getCoinId() {
		return coinId;
	}

	public void setCoinId(Integer coinId) {
		this.coinId = coinId;
	}

	@Override
	public String toString() {
		return "UserMaketVO [coinId=" + coinId + "]";
	}
	
	
}
