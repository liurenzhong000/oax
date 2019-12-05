package com.oax.admin.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.oax.admin.service.IArticleService;
import com.oax.admin.util.UserUtils;
import com.oax.common.RedisUtil;
import com.oax.common.enums.RedisKeyEnum;
import com.oax.entity.admin.User;
import com.oax.entity.admin.param.ArticleParam;
import com.oax.entity.admin.param.ArticleSaveParam;
import com.oax.entity.admin.vo.ArticleListVo;
import com.oax.entity.admin.vo.ArticleVo;
import com.oax.entity.front.Article;
import com.oax.mapper.front.ArticleMapper;

@Service
public class ArticleServiceImpl implements IArticleService {
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private RedisUtil redisUtil;

    @Override
    public PageInfo<ArticleListVo> noticeManageList(ArticleParam article) {
        PageHelper.startPage(article.getPageNum(), article.getPageSize());
        List<ArticleListVo> list = articleMapper.noticeManageList(article);
        return new PageInfo<>(list);
    }

    @Override
    public Integer update(Article article) {

        Article dbArticle = articleMapper.selectById(article.getId());


        if (dbArticle==null){
            return 0;
        }

        BeanUtils.copyProperties(article,dbArticle);


        Integer count = articleMapper.updateArticle(article);

        /*List<Article> cc = new ArrayList<>();
        cc.add(alter);
        redisUtil.setList("xxx", cc);*/
        //redisUtil.deleteKeys("xxx.*");
        if (count > 0) {
            redisUtil.deleteKeys(RedisKeyEnum.ARTICLE_LIST.getKey() + article.getType() + ".*");
            redisUtil.deleteKeys(RedisKeyEnum.ARTICLE.getKey() + article.getId()+".*");
        }
        return count;
    }

    @Override
    public Integer deleteNotice(Integer id) {

        Article article = articleMapper.selectById(id);

        Integer count = articleMapper.deleteNotice(id);
        if (count > 0) {
            redisUtil.deleteKeys(RedisKeyEnum.ARTICLE_LIST.getKey() + article.getType()+ ".*");
            redisUtil.deleteKeys(RedisKeyEnum.ARTICLE.getKey() + article.getId()+".*");
        }
        return count;
    }

    @Override
    public Integer saveArticle(ArticleSaveParam article) {
        ArticleSaveParam alter = new ArticleSaveParam();
        alter.setType(article.getType());
        alter.setCnTitle(article.getCnTitle());
        alter.setEnTitle(article.getEnTitle());
        alter.setCnContent(article.getCnContent());
        alter.setEnContent(article.getEnContent());
        alter.setStatus(article.getStatus());
        //获取后台登录用户id
        User shiroUser = UserUtils.getShiroUser();
        alter.setAdminId(shiroUser.getId());
        alter.setReleaseTime(article.getReleaseTime());
        Date date = new Date();
        alter.setCreateTime(date);
        alter.setUpdateTime(date);
        Integer count = articleMapper.saveArticle(alter);
        if (count > 0) {
            redisUtil.deleteKeys(RedisKeyEnum.ARTICLE_LIST.getKey() + article.getType() + ".*");
        }
        return count;
    }

    @Override
    public ArticleVo getArticle(Integer id) {
        return articleMapper.getArticle(id);
    }
}




























