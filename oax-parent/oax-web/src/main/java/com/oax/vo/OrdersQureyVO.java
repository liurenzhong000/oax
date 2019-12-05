package com.oax.vo;

import javax.validation.constraints.Min;

/** 
* @ClassName:：OrdersQureyVO 
* @Description： 订单记录入参model
* @author ：xiangwh  
* @date ：2018年6月13日 下午6:53:04 
*  
*/
public class OrdersQureyVO {
	//userId
	private Integer userId;
	//起始时间
	private String beginDate;
	//结束时间
	private String endDate;
	//交易对id
	private Integer marketId;
	//订单类型 1买入 2卖出
	private Integer type;
	//状态 -1已撤销 0撮合中 1 部分撮合 2 已撮合
	private String status;
	//分页参数
	@Min(value = 1, message = "页码不能小于1")
	private Integer pageNo = 1;
	private Integer pageSize = 10;
	
	public Integer getPageNo() {
		return pageNo;
	}
	public void setPageNo(Integer pageNo) {
		if(pageNo==null) pageNo = 1;
		this.pageNo = pageNo;
	}
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		if (pageSize==null) pageSize = 10;
		this.pageSize = pageSize;
	}
	public String getBeginDate() {
		return beginDate;
	}
	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public Integer getMarketId() {
		return marketId;
	}
	public void setMarketId(Integer marketId) {
		this.marketId = marketId;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	@Override
	public String toString() {
		return "OrdersQureyVO [userId=" + userId + ", beginDate=" + beginDate + ", endDate=" + endDate + ", marketId="
				+ marketId + ", type=" + type + ", status=" + status + "]";
	}
	
	
	
}
