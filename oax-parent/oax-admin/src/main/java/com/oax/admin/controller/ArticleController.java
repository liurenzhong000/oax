package com.oax.admin.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.oax.admin.service.IArticleService;
import com.oax.common.ResultResponse;
import com.oax.entity.admin.param.ArticleParam;
import com.oax.entity.admin.param.ArticleSaveParam;
import com.oax.entity.admin.vo.ArticleListVo;
import com.oax.entity.admin.vo.ArticleVo;
import com.oax.entity.front.Article;

@RequestMapping("article")
@RestController
public class ArticleController {

    @Autowired
    private IArticleService articleService;

    //公告管理列表
    @PostMapping("manageList")
    public ResultResponse manageList(@RequestBody ArticleParam article) {
        PageInfo<ArticleListVo> noticeList = articleService.noticeManageList(article);
        return new ResultResponse(true, noticeList);
    }

    //公告管理->添加公告
    @PostMapping("save")
    public ResultResponse save(@RequestBody @Valid ArticleSaveParam article) {
        Integer count = articleService.saveArticle(article);
        if (count > 0) {
            return new ResultResponse(true, "添加成功");
        } else {
            return new ResultResponse(false, "添加失败");
        }
    }

    //公告管理->修改更新
    @PutMapping("update")
    public ResultResponse update(@RequestBody @Valid Article article) {
        Integer count = articleService.update(article);
        if (count > 0) {
            return new ResultResponse(true, "更新公告成功");
        } else {
            return new ResultResponse(false, "更新公告失败");
        }
    }

    //公告管理->删除公告
    @DeleteMapping("/delete/{id}")
    public ResultResponse deleteArticle(@PathVariable(name = "id") Integer id) {
        Integer count = articleService.deleteNotice(id);
        if (count > 0) {
            return new ResultResponse(true, "删除公告成功");
        } else {
            return new ResultResponse(false, "删除公告失败");
        }
    }

    //公告管理->根据id->详情
    @GetMapping("/get/{id}")
    public ResultResponse getArticle(@PathVariable(name = "id") Integer id) {
        ArticleVo article = articleService.getArticle(id);
        return new ResultResponse(true, article);
    }

}
