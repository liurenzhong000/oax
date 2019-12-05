package com.oax.service;

import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.oax.entity.front.ArticleDetail;
import com.oax.entity.front.ArticleTypeDetail;
import com.oax.vo.ArticleDetailVO;
import com.oax.vo.ArticleTitleVO;

public interface HelpCenterService {

	Map<String, Object> list(String lang);

	PageInfo<ArticleTypeDetail> titleList(ArticleTitleVO vo);

	ArticleDetail articleDetail(ArticleDetailVO vo);

}