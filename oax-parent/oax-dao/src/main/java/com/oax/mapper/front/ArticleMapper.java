package com.oax.mapper.front;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.oax.entity.admin.param.ArticleParam;
import com.oax.entity.admin.param.ArticleSaveParam;
import com.oax.entity.admin.vo.ArticleListVo;
import com.oax.entity.admin.vo.ArticleVo;
import com.oax.entity.front.Article;
import com.oax.entity.front.ArticleDetail;
import com.oax.entity.front.ArticleTypeDetail;

@Mapper
public interface ArticleMapper {

    /*
     * 运营管理->公告管理
     */
    List<ArticleListVo> noticeManageList(ArticleParam article);

    /*
     * 运营管理->公告管理->修改公告
     */
    Integer updateArticle(Article alter);

    /*
     * 运营管理->公告管理->删除公告
     */
    Integer deleteNotice(Integer id);

    List<ArticleTypeDetail> selectTypeList(Map<String, Object> map);

    ArticleDetail selectArticleDetail(Map<String, Object> parmMap);

    /*
     * 运营管理->公告管理->添加公告
     */
    Integer saveArticle(ArticleSaveParam alter);

    /*
     * 公告管理->根据id->详情
     *
     */
    ArticleVo getArticle(Integer id);

    Article selectById(Integer id);

	Integer updateReadCountById(Integer id);
}