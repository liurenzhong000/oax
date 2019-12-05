package com.oax.vo;

import javax.validation.constraints.NotNull;

public class ArticleTitleVO {
	@NotNull
	private Integer type;
	private Integer pageIndex=1;
    private Integer pageSize=10;
    private String lang;

	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
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
	public String getLang() {
		return lang;
	}
	public void setLang(String lang) {
		this.lang = lang;
	}
}
