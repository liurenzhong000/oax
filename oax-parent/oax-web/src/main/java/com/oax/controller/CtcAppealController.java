package com.oax.controller;

import com.oax.common.ResultResponse;
import com.oax.context.HttpContext;
import com.oax.service.ctc.CtcAppealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * CTC 订单申诉相关
 */
@RestController
@RequestMapping("/ctcAppeal")
public class CtcAppealController {

//    @Autowired
//    private CtcAppealService ctcAppealService;

//    @PostMapping
//    public ResultResponse saveOne(Integer ctcOrderId, String appealDesc){
//        Integer userId = HttpContext.getUserId();
//        ctcAppealService.saveOne(userId, ctcOrderId, appealDesc);
//        return new ResultResponse(true, null);
//    }

}
