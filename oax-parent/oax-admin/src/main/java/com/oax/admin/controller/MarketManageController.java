package com.oax.admin.controller;

import java.util.List;

import javax.validation.Valid;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.oax.admin.service.IIndexPageMarketService;
import com.oax.common.ResultResponse;
import com.oax.entity.admin.MarketCoin;
import com.oax.entity.admin.vo.DealCoinVo;
import com.oax.entity.front.IndexPageMarket;

@RequestMapping("market")
@RestController
public class MarketManageController {

    @Autowired
    private IIndexPageMarketService indexPageMarketService;

    //交易对管理->列表
    @PostMapping("manageList")
    public ResultResponse manageList() {
        List<IndexPageMarket> dealRightList = indexPageMarketService.dealRightManage();
        return new ResultResponse(true, dealRightList);
    }

    //交易对管理->修改->所有交易币种
    @PostMapping("coinList")
    public ResultResponse coinList() {
        List<DealCoinVo> coinList = indexPageMarketService.coinList();
        return new ResultResponse(true, coinList);
    }

    //交易对管理->修改
    @PutMapping("update")
    public ResultResponse update(@RequestBody @Valid MarketCoin marketCoin) {

        List<IndexPageMarket> indexPageMarketList = indexPageMarketService.selectByMarketId(marketCoin.getMarketId());

        if (CollectionUtils.isNotEmpty(indexPageMarketList)&&
                !indexPageMarketList.get(0).getId().equals(marketCoin.getId())){
            return new ResultResponse(false,"市场已存在");
        }
        Integer count = indexPageMarketService.update(marketCoin);
        if (count > 0) {
            return new ResultResponse(true, "更新交易对成功");
        } else {
            return new ResultResponse(false, "更新交易对失败");
        }
    }
}
