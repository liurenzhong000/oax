package com.oax.service.impl;

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
import com.oax.service.HelpCenterService;
import com.oax.vo.ArticleDetailVO;
import com.oax.vo.ArticleTitleVO;

@Service
public class HelpCenterServiceImpl implements HelpCenterService {
	
	@Autowired
	private ArticleMapper articleMapper;
	
	@Autowired
	private RedisUtil redisUtil;
	

	@Override
	@DataSource(DataSourceType.SLAVE)
	public Map<String, Object> list(String lang) {
		Map<String, Object> parmMap=new HashMap<>();
		Map<String, Object> resultMap=new HashMap<>();
		
		parmMap.put("lang", lang);	
		parmMap.put("type", 3);
		List<ArticleTypeDetail> problemList = redisUtil.getList("articleList.3."+lang, ArticleTypeDetail.class);
		if(problemList==null) {
			problemList=articleMapper.selectTypeList(parmMap);
			redisUtil.setList("articleList.3."+lang, problemList,-1);
		}
		
		parmMap.put("type", 4);
		List<ArticleTypeDetail> informationList = redisUtil.getList("articleList.4."+lang, ArticleTypeDetail.class);
		if(informationList==null) {
			informationList=articleMapper.selectTypeList(parmMap);
			redisUtil.setList("articleList.4."+lang, informationList,-1);
		}
		
		parmMap.put("type", 5);
		List<ArticleTypeDetail> termsList = redisUtil.getList("articleList.5."+lang, ArticleTypeDetail.class);
		if(termsList==null) {
			termsList=articleMapper.selectTypeList(parmMap);
			redisUtil.setList("articleList.5."+lang, termsList,-1);
		}

		parmMap.put("type", 6);
		List<ArticleTypeDetail> aboutUsList = redisUtil.getList("articleList.6."+lang, ArticleTypeDetail.class);
		if(aboutUsList==null) {
			aboutUsList=articleMapper.selectTypeList(parmMap);
			redisUtil.setList("articleList.6."+lang, aboutUsList,-1);
		}
		resultMap.put("problemList", problemList);
		resultMap.put("informationList", informationList);
		resultMap.put("termsList", termsList);
		resultMap.put("aboutUsList", aboutUsList);
		
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
			List<ArticleTypeDetail>  list=articleMapper.selectTypeList(parmMap);
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
