package com.oax.admin.controller;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.oax.admin.service.CoinLockConfigService;
import com.oax.common.ResultResponse;
import com.oax.entity.admin.vo.CoinLockConfigVo;
import com.oax.entity.front.CoinLockConfig;
import com.oax.entity.front.InrestRatio;

@RestController
@RequestMapping("coinLockConfig")
public class CoinLockConfigController {
    @Autowired
    private CoinLockConfigService service;

    @PostMapping("saveOrUpdate")
    public ResultResponse saveOrUpdate(@RequestBody CoinLockConfig coinLockConfig){
        if (coinLockConfig.getStandardLockQty()==null||coinLockConfig.getStandardLockQty().compareTo(BigDecimal.ZERO)<=0){
            return new ResultResponse(false, "基准锁仓数量不能为空且必须大于0");
        }
        BigDecimal arg = new BigDecimal(coinLockConfig.getStandardLockQty().intValue());
        if (arg.compareTo(coinLockConfig.getStandardLockQty())!=0){
            return new ResultResponse( false, "基准锁仓量必须是整数");
        }
        if (coinLockConfig.getInterestRate()==null||coinLockConfig.getInterestRate().compareTo(BigDecimal.ZERO)<=0||coinLockConfig.getInterestRate().compareTo(BigDecimal.ONE)>0){
            return new ResultResponse(false, "手续费利息分配比不能为空且必须大于0小于等于1");
        }
        if (StringUtils.isEmpty(coinLockConfig.getRatio())){
            return new ResultResponse(false, "锁仓权重系数不能为空");
        }
        List<InrestRatio> list = JSONObject.parseArray(coinLockConfig.getRatio(), InrestRatio.class);
        if (list==null||list.size()==0){
            return new ResultResponse(false, "锁仓权重系数不能为空");
        }
        for (InrestRatio inrestRatio : list) {
            if (inrestRatio.getRatio()==null||inrestRatio.getRatio().compareTo(BigDecimal.ZERO)<=0){
                return new ResultResponse(false, "锁仓权重系数不能为空且必须大于0");
            }
        }
        boolean flag = service.saveOrUpdate(coinLockConfig);
        if (flag){
            if (coinLockConfig.getId()==null){
                return new ResultResponse(true, "添加成功");
            }else{
                return new ResultResponse(true, "修改成功");
            }
        }else{
            if (coinLockConfig.getId()==null){
                return new ResultResponse(false, "添加失败");
            }else{
                return new ResultResponse(false, "修改失败");
            }
        }

    }

    @GetMapping("/selectById/{id}")
    public ResultResponse selectById(@PathVariable(name = "id") Integer id){
        CoinLockConfig coinLockConfig = service.selectById(id);
        return new ResultResponse(true, coinLockConfig);
    }

    @PostMapping("index")
    public ResultResponse index(@RequestBody CoinLockConfigVo vo){
        PageInfo<?> page = service.getPage(vo);
        return new ResultResponse(true, page);
    }

    @GetMapping("/isShow/{id}/{isShow}")
    public ResultResponse isShow(@PathVariable(name = "id") Integer id,@PathVariable(name = "isShow") Integer isShow){
        boolean flag = service.isShow(id,isShow);
        return new ResultResponse(flag, flag?"操作成功":"操作失败");
    }


}
