package com.oax.vo;

import javax.validation.constraints.NotNull;

public class ArticleDetailVO {
	@NotNull
	private Integer id;
    private String lang;

	public String getLang() {
		return lang;
	}
	public void setLang(String lang) {
		this.lang = lang;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
}
