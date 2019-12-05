package com.oax.vo;

public class WithdrawAddressListVO {
	
	private Integer pageIndex=1;	

	private Integer pageSize=10;
	
	private Integer userId;
	
	private Integer coinId;


	public Integer getPageIndex() {
		return pageIndex;
	}


	public void setPageIndex(Integer pageIndex) {
		this.pageIndex = pageIndex;
	}


	public Integer getPageSize() {
		return pageSize;
	}


	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}


	public Integer getUserId() {
		return userId;
	}


	public void setUserId(Integer userId) {
		this.userId = userId;
	}


	public Integer getCoinId() {
		return coinId;
	}


	public void setCoinId(Integer coinId) {
		this.coinId = coinId;
	}

}
