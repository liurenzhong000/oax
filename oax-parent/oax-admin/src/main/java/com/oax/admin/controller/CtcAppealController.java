package com.oax.admin.controller;

import com.github.pagehelper.PageInfo;
import com.oax.admin.service.ctc.CtcAppealService;
import com.oax.common.ResultResponse;
import com.oax.entity.admin.param.CtcAppealParam;
import com.oax.entity.admin.vo.CtcAppealVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * CTC 订单申诉相关
 */
@RestController
@RequestMapping("/ctcAppeal")
public class CtcAppealController {

    @Autowired
    private CtcAppealService ctcAppealService;

    @GetMapping("/page")
    public ResultResponse page(CtcAppealParam param) {
        PageInfo<CtcAppealVo> pageInfo = ctcAppealService.page(param);
        return new ResultResponse(true, pageInfo);
    }
}
