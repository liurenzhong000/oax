package com.oax.admin.controller;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.oax.admin.service.AppService;
import com.oax.admin.util.UserUtils;
import com.oax.common.PageResultResponse;
import com.oax.common.ResultResponse;
import com.oax.entity.admin.User;
import com.oax.entity.admin.param.AppPageParam;
import com.oax.entity.front.App;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/6/12
 * Time: 14:14
 * app版本控制 控制层
 */
@RestController
@RequestMapping("/apps")
public class AppController {

    @Autowired
    private AppService appService;

    @PostMapping("/page")
    public ResultResponse getAllAppList(@RequestBody AppPageParam appPageParam) {

        PageInfo<App> appPageInfo = appService.selectByAppPageParam(appPageParam);

        PageResultResponse<App> appPageResultResponse = new PageResultResponse<>();

        BeanUtils.copyProperties(appPageInfo, appPageResultResponse);
        appPageResultResponse.setParam(appPageParam);
        return new ResultResponse(true, appPageResultResponse);

    }

    @PutMapping
    public ResultResponse updateApp(@RequestBody App app) {

        App dbAPP = appService.selectById(app.getId());

        if (dbAPP == null) {
            return new ResultResponse(false, "不存在的app");
        }

        User shiroUser = UserUtils.getShiroUser();
        app.setAdminId(shiroUser.getId());
        appService.update(app);

        return new ResultResponse(true, "更新成功");
    }

    @GetMapping("/{appId}")
    public ResultResponse getAppById(@PathVariable(name = "appId") int appId) {
        App app = appService.selectById(appId);

        return new ResultResponse(true, app);
    }

    @PostMapping
    public ResultResponse addApp(@RequestBody @Valid App app, BindingResult result) {

        if (result.hasErrors()) {
            return new ResultResponse(false, result.getFieldError().getDefaultMessage());
        }
        User shiroUser = UserUtils.getShiroUser();
        app.setAdminId(shiroUser.getId());
        appService.insert(app);

        return new ResultResponse(true, "添加app成功");

    }


}
