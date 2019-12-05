package com.oax.admin.controller;

import com.github.pagehelper.PageInfo;
import com.oax.admin.form.UserRemarkFrom;
import com.oax.admin.service.UserRemarkService;
import com.oax.common.AssertHelper;
import com.oax.common.ResultResponse;
import com.oax.entity.admin.param.PageParam;
import com.oax.entity.front.UserRemark;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Auther: hyp
 * @Date: 2019/2/26 16:49
 * @Description:
 */
@RestController
@RequestMapping("/userRemark")
public class UserRemarkController {

    @Autowired
    private UserRemarkService userRemarkService;

    @GetMapping("/list")
    public ResultResponse list(PageParam param, Integer userId) {
        PageInfo<UserRemark> page = userRemarkService.page(param, userId);
        return new ResultResponse(true, page);
    }

    @PostMapping
    public ResultResponse saveOne(@RequestBody UserRemarkFrom from){
        AssertHelper.notEmpty(from.getUserId(), "用户id不能为空");
        AssertHelper.notEmpty(from.getRemark(), "标注不能为空");
        userRemarkService.saveOne(from.getUserId(), from.getRemark());
        return new ResultResponse(true, "操作成功");
    }

}
