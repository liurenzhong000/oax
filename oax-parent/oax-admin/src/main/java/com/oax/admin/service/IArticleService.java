package com.oax.admin.service;

import com.github.pagehelper.PageInfo;
import com.oax.entity.admin.param.ArticleParam;
import com.oax.entity.admin.param.ArticleSaveParam;
import com.oax.entity.admin.vo.ArticleListVo;
import com.oax.entity.admin.vo.ArticleVo;
import com.oax.entity.front.Article;

public interface IArticleService {
    /*
     * 运营管理->公告管理
     */
    PageInfo<ArticleListVo> noticeManageList(ArticleParam article);

    /*
     * 运营管理->公告管理->修改公告
     */
    Integer update(Article article);

    /*
     * 运营管理->公告管理->删除公告
     */
    Integer deleteNotice(Integer id);

    /*
     * 运营管理->公告管理->添加公告
     */
    Integer saveArticle(ArticleSaveParam article);

    /*
     * 公告管理->根据id->详情
     */
    ArticleVo getArticle(Integer id);
}
