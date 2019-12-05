package com.oax.vo;

import java.io.Serializable;

/** 
* @ClassName:：AppVO 
* @Description： app入参model
* @author ：xiangwh  
* @date ：2018年6月13日 下午8:00:01 
*  
*/
public class AppVO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String version;
	private Integer type;
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	@Override
	public String toString() {
		return "AppVO [version=" + version + ", type=" + type + "]";
	}
	
}
