package com.oax.service;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.oax.entity.front.ArticleDetail;
import com.oax.entity.front.ArticleTypeDetail;
import com.oax.vo.ArticleDetailVO;
import com.oax.vo.ArticleTitleVO;

public interface ArticleService {

	List<ArticleTypeDetail> list(String lang, Integer type);
	
	Map<String, Object> noticeList(String lang, Integer type);

	PageInfo<ArticleTypeDetail> titleList(ArticleTitleVO vo);

	ArticleDetail articleDetail(ArticleDetailVO vo);

}