package com.oax.admin.controller;

import java.util.List;

import javax.validation.Valid;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.oax.admin.service.CoinService;
import com.oax.admin.service.MarketCategoryService;
import com.oax.admin.service.MarketService;
import com.oax.common.ResultResponse;
import com.oax.common.enums.MarketStatusEnum;
import com.oax.entity.admin.dto.MarketWithCoin;
import com.oax.entity.admin.param.MarketParam;
import com.oax.entity.admin.vo.SimpleMarketsVo;
import com.oax.entity.front.CoinWithBLOBs;
import com.oax.entity.front.Market;
import com.oax.entity.front.MarketCategory;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/6/12
 * Time: 11:29
 * 交易对 控制层
 */
@RestController
@RequestMapping("/markets")
public class MarketsController {


    @Autowired
    private CoinService coinService;

    @Autowired
    private MarketService marketService;

    @Autowired
    private MarketCategoryService marketCategoryService;


    @PostMapping("/categorie")
    public ResultResponse getAllMarkets(@RequestBody MarketParam marketParam) {
        List<MarketWithCoin> markets = marketService.selectByMarketParam(marketParam);
        return new ResultResponse(true, markets);
    }

    @GetMapping("/simple")
    public ResultResponse getAllSimpleMarkets() {

        List<SimpleMarketsVo> simpleMarketsVos = marketService.selectSimpleAll();

        return new ResultResponse(true, simpleMarketsVos);
    }


    @GetMapping("/{marketId}/show")
    public ResultResponse showMarkets(@PathVariable(name = "marketId") int marketId) {


        MarketWithCoin marketWithCoin = marketService.findById(marketId);

        if (marketWithCoin == null) {
            return new ResultResponse(false, "不存在此市场");
        }

        marketWithCoin.setIsShow(MarketStatusEnum.SHOW.getStatus());
        try {
            marketService.update(marketWithCoin);
            return new ResultResponse(true, "交易对开启成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultResponse(false, "交易对开启失败");
        }

    }

    @GetMapping("/{marketId}/close")
    public ResultResponse closeMarkets(@PathVariable(name = "marketId") int marketId) {

        MarketWithCoin marketWithCoin = marketService.findById(marketId);

        if (marketWithCoin == null) {
            return new ResultResponse(false, "不存在此市场");
        }

        marketWithCoin.setIsShow(MarketStatusEnum.CLOSE.getStatus());

        try {
            marketService.update(marketWithCoin);
            return new ResultResponse(true, "交易对关闭成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultResponse(false, "交易对关闭失败");
        }
    }

    @GetMapping("/{marketId}/close/auto_add")
    public ResultResponse closeAutoAddMarkets(@PathVariable(name = "marketId") int marketId) {

        MarketWithCoin marketWithCoin = marketService.findById(marketId);

        if (marketWithCoin == null) {
            return new ResultResponse(false, "不存在此市场");
        }

        marketWithCoin.setIsAutoAdd(MarketStatusEnum.NOT_AUTO_ADD.getStatus());


        marketService.update(marketWithCoin);
        return new ResultResponse(true, "关闭自动交易成功");

    }



    @GetMapping("/{marketId}/open/auto_add")
    public ResultResponse openAutoAddMarkets(@PathVariable(name = "marketId") int marketId) {

        MarketWithCoin marketWithCoin = marketService.findById(marketId);

        if (marketWithCoin == null) {
            return new ResultResponse(false, "不存在此市场");
        }

        marketWithCoin.setIsAutoAdd(MarketStatusEnum.AUTO_ADD.getStatus());

        marketService.update(marketWithCoin);
        return new ResultResponse(true, "设置自动交易成功");
    }

    @GetMapping("/{marketId}/open/is_mine")
    public ResultResponse openAutoMineMarkets(@PathVariable(name = "marketId") int marketId) {

        MarketWithCoin marketWithCoin = marketService.findById(marketId);

        if (marketWithCoin == null) {
            return new ResultResponse(false, "不存在此市场");
        }

        marketWithCoin.setIsMine(MarketStatusEnum.AUTO_MINE.getStatus());

        marketService.update(marketWithCoin);
        return new ResultResponse(true, "设置交易挖矿成功");
    }

    @GetMapping("/{marketId}/close/is_mine")
    public ResultResponse closeMineMarkets(@PathVariable(name = "marketId") int marketId) {

        MarketWithCoin marketWithCoin = marketService.findById(marketId);

        if (marketWithCoin == null) {
            return new ResultResponse(false, "不存在此市场");
        }

        marketWithCoin.setIsMine(MarketStatusEnum.NOT_AUTO_MINE.getStatus());


        marketService.update(marketWithCoin);
        return new ResultResponse(true, "关闭交易挖矿成功");

    }


    @GetMapping("/{marketId}")
    public ResultResponse getMarketsById(@PathVariable(name = "marketId") int marketId) {

        MarketWithCoin marketWithCoin = marketService.findById(marketId);

        return new ResultResponse(true, marketWithCoin);

    }

    @PostMapping
    public ResultResponse addMarket(@RequestBody @Valid Market market, BindingResult result) {

        if (result.hasErrors()) {
            return new ResultResponse(false, result.getFieldError().getDefaultMessage());
        }
        MarketCategory marketCategory = marketCategoryService.findById(market.getMarketCategoryId());
        if (marketCategory == null) {
            return new ResultResponse(false, "找不到对应的分区");
        }

        if (marketCategory.getCoinId().equals(market.getCoinId())) {
            return new ResultResponse(false, "分区币种不能与目标币种相同");
        }

        CoinWithBLOBs coinWithBLOBs = coinService.selectById(market.getCoinId());
        if (coinWithBLOBs == null) {
            return new ResultResponse(false, "找不到对应的虚拟币");
        }

        List<Market> marketList = marketService.selectByCategoryCoinIdAndMarketCoinId(marketCategory.getCoinId(), market.getCoinId());

        if (CollectionUtils.isNotEmpty(marketList)) {
            return new ResultResponse(false, "交易对已存在");
        }
        try {
            market.setIsShow(MarketStatusEnum.CLOSE.getStatus());
            marketService.insert(market);
            return new ResultResponse(true, "添加交易对成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultResponse(false, "添加交易对失败");
        }
    }


    @PutMapping("/{marketId}/sortNum/{sortNum}")
    public ResultResponse updateSortNum(@PathVariable("marketId") Integer marketId,
                                        @PathVariable("sortNum") Integer sortNum) {
        Market market = marketService.selectById(marketId);
        if (market == null) {
            return new ResultResponse(false, "不存在的交易对");
        }
        market.setSortNum(sortNum);
        marketService.update(market);
        return new ResultResponse(true, "变更排序成功");
    }

    @PutMapping
    public ResultResponse updateMarket(@RequestBody Market market) {
        if (market.getId() == null || market.getId() <= 0) {
            return new ResultResponse(false, "交易对id不能为空");
        }
        MarketCategory marketCategory = marketCategoryService.findById(market.getMarketCategoryId());
        if (marketCategory == null) {
            return new ResultResponse(false, "找不到对应的分区");
        }

        if (marketCategory.getCoinId().equals(market.getCoinId())) {
            return new ResultResponse(false, "分区币种不能与目标币种相同");
        }

        CoinWithBLOBs coinWithBLOBs = coinService.selectById(market.getCoinId());
        if (coinWithBLOBs == null) {
            return new ResultResponse(false, "找不到对应的虚拟币");
        }

        List<Market> marketList = marketService.selectByCategoryCoinIdAndMarketCoinId(marketCategory.getCoinId(), market.getCoinId());

        Market dbmarket = marketService.selectById(market.getId());
        if (!(market.getCoinId().equals(dbmarket.getCoinId())
                && market.getMarketCategoryId().equals(dbmarket.getMarketCategoryId()))) {
            if (CollectionUtils.isNotEmpty(marketList)) {
                return new ResultResponse(false, "交易对已存在");
            }
        }


        try {
            marketService.update(market);
            return new ResultResponse(true, "更新交易对成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultResponse(false, "更新交易对失败");
        }
    }


}
