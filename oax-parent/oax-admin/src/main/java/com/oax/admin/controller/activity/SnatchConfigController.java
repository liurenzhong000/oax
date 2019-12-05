package com.oax.admin.controller.activity;

import com.github.pagehelper.PageInfo;
import com.oax.admin.form.SnatchConfigForm;
import com.oax.admin.form.UpdateSnatchConfigForm;
import com.oax.admin.service.activity.SnatchConfigService;
import com.oax.common.BeanHepler;
import com.oax.common.ResultResponse;
import com.oax.entity.activity.SnatchConfig;
import com.oax.entity.admin.param.PageParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @Auther: hyp
 * @Date: 2019/1/21 18:07
 * @Description:
 */
@RestController
@RequestMapping("/activity/snatchConfig")
public class SnatchConfigController {

    @Autowired
    private SnatchConfigService snatchConfigService;

    @PostMapping
    public ResultResponse saveOne(@Valid SnatchConfigForm form){
        SnatchConfig entity = BeanHepler.copy(form, SnatchConfig.class);
        snatchConfigService.saveOne(entity);
        return new ResultResponse(true, "新增成功");
    }

    @PutMapping("/{id}")
    public ResultResponse updateOne(@PathVariable Integer id, @Valid UpdateSnatchConfigForm form){
        SnatchConfig entity = BeanHepler.copy(form, SnatchConfig.class);
        entity.setId(id);
        snatchConfigService.updateOne(entity);
        return new ResultResponse(true, "更新成功");
    }

    @GetMapping("/list")
    public ResultResponse page(Integer coinId, PageParam pageParam){
        PageInfo pageInfo = snatchConfigService.list(coinId, pageParam);
        return new ResultResponse(pageInfo);
    }

}
