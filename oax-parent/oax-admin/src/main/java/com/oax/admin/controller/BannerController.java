package com.oax.admin.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.oax.admin.service.IBenderService;
import com.oax.common.ResultResponse;
import com.oax.entity.admin.param.BannerParam;
import com.oax.entity.admin.param.BannerUpdateParam;
import com.oax.entity.admin.vo.BannerVo;
import com.oax.entity.front.Banner;

/**
 * 运营管理相关
 */
@RequestMapping("banner")
@RestController
public class BannerController {
    @Autowired
    private IBenderService benderService;

    //bender管理列表
    @PostMapping("manageList")
    public ResultResponse bannerManageList(@RequestBody BannerParam banner) {
        PageInfo<BannerVo> bannerList = benderService.bannerManageList(banner);
        return new ResultResponse(true, bannerList);
    }

    //banner管理->更新
    @PutMapping("update")
    public ResultResponse update(@RequestBody @Valid BannerUpdateParam banner) {
        Integer count = benderService.update(banner);
        if (count > 0) {
            return new ResultResponse(true, "更新bender成功");
        } else {
            return new ResultResponse(false, "更新bender失败");
        }
    }

    //banner管理->删除
    @DeleteMapping("/delete/{id}")
    public ResultResponse deleteBanner(@PathVariable(name = "id") Integer id) {
        Integer count = benderService.deleteBanner(id);
        if (count > 0) {
            return new ResultResponse(true, "删除公告成功");
        } else {
            return new ResultResponse(false, "删除公告失败");
        }
    }

    //banner管理->添加
    @PostMapping("save")
    public ResultResponse save(@RequestBody @Valid Banner banner) {
        Integer count = benderService.saveBanner(banner);
        if (count > 0) {
            return new ResultResponse(true, "添加成功");
        } else {
            return new ResultResponse(false, "添加失败");
        }
    }
}

















