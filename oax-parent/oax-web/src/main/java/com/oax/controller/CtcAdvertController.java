package com.oax.controller;

import com.github.pagehelper.PageInfo;
import com.oax.common.ResultResponse;
import com.oax.context.HttpContext;
import com.oax.entity.enums.CtcAdvertStatus;
import com.oax.entity.front.param.CtcAdvertParam;
import com.oax.entity.front.vo.CtcAdvertBalanceVo;
import com.oax.entity.front.vo.ListCtcAdvertVo;
import com.oax.form.CtcAdvertForm;
import com.oax.form.UpdateCtcAdvertForm;
import com.oax.service.ctc.CtcAdvertService;
import com.oax.vo.CtcAdvertInfoVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 商户广告相关接口
 */
@RestController
@RequestMapping("/ctcAdvert")
@Api(tags = { "CtcAdvert API" }, description = "商户广告相关的API")
public class CtcAdvertController {

    @Autowired
    private CtcAdvertService ctcAdvertService;

    /**
     * 获取广告的列表
     *
     */
    @GetMapping("/pageForAdvert")
    public ResultResponse pageForAdvert(CtcAdvertParam param) throws Exception {

        PageInfo<ListCtcAdvertVo> pageInfo = ctcAdvertService.pageForAdvert(param);
        return new ResultResponse(true, pageInfo);
    }

    /**
     * 发布广告前用户对应币种的余额
     */
    @GetMapping("/coinInfo")
    @ApiOperation("发布广告前用户对应币种的余额")
    public ResultResponse coinInfo(Integer coinId){
        Integer userId = HttpContext.getUserId();
        CtcAdvertBalanceVo data = ctcAdvertService.coinInfo(userId, coinId);
        return new ResultResponse(true, data);
    }
    /**
     * 发布一个广告
     */
    @PostMapping
    public ResultResponse saveOne(CtcAdvertForm form) {
        try{
            ctcAdvertService.saveOne(form);
        }catch (Exception e){
            return new ResultResponse(false,e.getMessage());
        }

        return new ResultResponse(true, "发布成功");
    }









    //更新广告
    @PutMapping("/{id}")
    public ResultResponse updateOne(@PathVariable Integer id, UpdateCtcAdvertForm form){
        Integer userId = HttpContext.getUserId();
        ctcAdvertService.updateOne(userId, id, form);
        return new ResultResponse(true, "操作成功");
    }

    /**
     * 上架或下架广告
     * (postman 下 PUT使用枚举为空)
     */
    @PutMapping("/{id}/updateStatus")
    public ResultResponse updateStatus(@PathVariable Integer id, @RequestParam(required = true) CtcAdvertStatus status){
        ctcAdvertService.updateStatus(id, status);
        return new ResultResponse(true, "操作成功");
    }

    /**
     * 获取商户的广告的列表，时间倒叙
     * 发布记录
     */
    @GetMapping("/pageForWeb")
    public ResultResponse page(CtcAdvertParam param){
        PageInfo<ListCtcAdvertVo> pageInfo = ctcAdvertService.pageForWeb(param, HttpContext.getUserId());
        return new ResultResponse(true, pageInfo);
    }

    /**
     * 进入ctc页面，获取商户的买卖广告信息
     */
    @GetMapping("/info")
    public ResultResponse info(){
        CtcAdvertInfoVO infoVO = ctcAdvertService.info();
        return new ResultResponse(true, infoVO);
    }






}
