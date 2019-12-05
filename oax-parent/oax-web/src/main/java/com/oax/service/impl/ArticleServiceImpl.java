package com.oax.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.oax.common.RedisUtil;
import com.oax.druid.DataSource;
import com.oax.druid.DataSourceType;
import com.oax.entity.front.ArticleDetail;
import com.oax.entity.front.ArticleTypeDetail;
import com.oax.mapper.front.ArticleMapper;
import com.oax.service.ArticleService;
import com.oax.vo.ArticleDetailVO;
import com.oax.vo.ArticleTitleVO;

@Service
public class ArticleServiceImpl implements ArticleService {
	
	@Autowired
	private ArticleMapper articleMapper;
	
	@Autowired
	private RedisUtil redisUtil;
	
	
	@Override
	@DataSource(DataSourceType.SLAVE)
	public List<ArticleTypeDetail> list(String lang,Integer type) {
		
		List<ArticleTypeDetail> articleList = redisUtil.getList("articleList."+type+"."+lang, ArticleTypeDetail.class);
		
		Map<String,Object> parmMap=new HashMap<>();
		parmMap.put("lang", lang);
		parmMap.put("type", type);
		
		if(articleList==null) {
			articleList=articleMapper.selectTypeList(parmMap);
			redisUtil.setList("articleList."+type+"."+lang, articleList,-1);
		}
		
		return articleList;
		
	}

	@Override
	@DataSource(DataSourceType.SLAVE)
	public Map<String, Object> noticeList(String lang,Integer source) {
		
		List<ArticleTypeDetail> list=new ArrayList<>();
		Map<String, Object> parmMap=new HashMap<>();
		Map<String, Object> resultMap=new HashMap<>();
		
		parmMap.put("lang", lang);
		parmMap.put("type", 1);
		List<ArticleTypeDetail> newCoinList = redisUtil.getList("articleList.1."+lang, ArticleTypeDetail.class);
		if(newCoinList==null) {
			newCoinList=articleMapper.selectTypeList(parmMap);
			redisUtil.setList("articleList.1."+lang, newCoinList,-1);
		}
		
		parmMap.put("type", 2);
		List<ArticleTypeDetail> newNoticeList = redisUtil.getList("articleList.2."+lang, ArticleTypeDetail.class);
		if(newNoticeList==null) {
			newNoticeList=articleMapper.selectTypeList(parmMap);
			redisUtil.setList("articleList.2."+lang, newNoticeList,-1);
		}
		
		if(source==1) {
			resultMap.put("newCoinList", newCoinList);
			resultMap.put("newNoticeList", newNoticeList);
		}else {
			list.addAll(newCoinList);
			list.addAll(newNoticeList);
			resultMap.put("list", list);
		}
		
		return resultMap;
	}

	
	@Override
	@DataSource(DataSourceType.SLAVE)
	public PageInfo<ArticleTypeDetail> titleList(ArticleTitleVO vo) {
		Integer type=vo.getType();
		String key="articleList."+type+"."+vo.getLang()+"."+vo.getPageIndex()+"."+vo.getPageSize();
		
		Map<String, Object> parmMap=new HashMap<>();
		parmMap.put("lang", vo.getLang());
		parmMap.put("type", type);
		
		PageInfo<ArticleTypeDetail> pageInfo=redisUtil.getObject(key, PageInfo.class);
		if(pageInfo==null) {
			PageHelper.startPage(vo.getPageIndex(), vo.getPageSize());
			List<ArticleTypeDetail> list=articleMapper.selectTypeList(parmMap);
			pageInfo=new PageInfo<>(list);
			redisUtil.setObject(key, pageInfo,-1);
		}		
		
		return pageInfo;
	}

	
	@Override
	@DataSource(DataSourceType.MASTER)
	public ArticleDetail articleDetail(ArticleDetailVO vo) {
		Integer id=vo.getId();
		String key="article."+id+"."+vo.getLang();
		
		Map<String, Object> parmMap=new HashMap<>();
		parmMap.put("lang", vo.getLang());
		parmMap.put("id", id);
		
		ArticleDetail detail=redisUtil.getObject(key, ArticleDetail.class);
		if(detail==null) {
			 detail=articleMapper.selectArticleDetail(parmMap);
			 redisUtil.setObject(key, detail,-1);
		}
		
		articleMapper.updateReadCountById(id);
		return detail;
	}
}
