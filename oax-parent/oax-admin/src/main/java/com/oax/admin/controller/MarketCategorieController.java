package com.oax.admin.controller;

import java.util.List;

import javax.validation.Valid;

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
import com.oax.common.RedisUtil;
import com.oax.common.ResultResponse;
import com.oax.common.enums.MarketCategoryTypeEnum;
import com.oax.common.enums.RedisKeyEnum;
import com.oax.entity.admin.vo.SimpleMarketCategoryVo;
import com.oax.entity.front.CoinWithBLOBs;
import com.oax.entity.front.MarketCategory;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/6/12
 * Time: 11:23
 * 市场分区 控制层
 */
@RestController
@RequestMapping("/marketCategories")
public class MarketCategorieController {

    @Autowired
    private MarketCategoryService marketCategoryService;

    @Autowired
    private CoinService coinService;

    @Autowired
    private RedisUtil redisUtil;


    @GetMapping("/simple")
    public ResultResponse findSimpleMarketCategoryById() {
        List<SimpleMarketCategoryVo> simpleMarketCategoryVos = marketCategoryService.findAllSimpleMarketCategory();
        return new ResultResponse(true, simpleMarketCategoryVos);
    }
    @GetMapping
    public ResultResponse getAllMarketCategory() {

        List<MarketCategory> marketCategoryList = marketCategoryService.selectAll();

        return new ResultResponse(true, marketCategoryList);

    }

    @GetMapping("/{categoryId}")
    public ResultResponse getAllMarketCategory(@PathVariable int categoryId) {

        MarketCategory marketCategory = marketCategoryService.findById(categoryId);

        return new ResultResponse(true, marketCategory);
    }

    @PostMapping
    public ResultResponse addMarketCategory(@RequestBody @Valid MarketCategory marketCategory, BindingResult result) {

        if (result.hasErrors()) {
            return new ResultResponse(false, result.getFieldError().getDefaultMessage());
        }

        try {
            CoinWithBLOBs coinWithBLOBs = coinService.selectById(marketCategory.getCoinId());

            MarketCategory dbmarketca = marketCategoryService.selectByCoinId(marketCategory.getCoinId());
            if (dbmarketca!=null){
                return new ResultResponse(false,"分区已存在");
            }
            if (coinWithBLOBs == null) {
                return new ResultResponse(false, "CoinId:" + marketCategory.getCoinId() + ",币种不存在");
            }

            marketCategory.setIsUse(MarketCategoryTypeEnum.CLOSE.getType());
            marketCategoryService.add(marketCategory);

            redisUtil.delete(RedisKeyEnum.MARKET_CATEGORY_LIST.getKey());
            redisUtil.delete(RedisKeyEnum.MARKET_CATEGORY_ALLLIST.getKey());

            return new ResultResponse(true, "添加分区成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultResponse(false, "添加分区失败");
        }
    }

    @GetMapping("/{categoryId}/used")
    public ResultResponse marketCategoriesUsed(@PathVariable(name = "categoryId") int categoryId) {
        MarketCategory marketCategory = marketCategoryService.findById(categoryId);

        if (marketCategory == null) {
            return new ResultResponse(false, "没有对应的市场分区");
        }

        marketCategory.setIsUse(MarketCategoryTypeEnum.USED.getType());
        try {
            marketCategoryService.update(marketCategory);
            return new ResultResponse(true, "设置分区可用成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultResponse(false, "设置分区可用失败");
        }
    }

    @GetMapping("/{categoryId}/close")
    public ResultResponse marketCategoriesClose(@PathVariable(name = "categoryId") int categoryId) {
        MarketCategory marketCategory = marketCategoryService.findById(categoryId);

        if (marketCategory == null) {
            return new ResultResponse(false, "没有对应的市场分区");
        }

        marketCategory.setIsUse(MarketCategoryTypeEnum.CLOSE.getType());
        try {
            marketCategoryService.update(marketCategory);
            return new ResultResponse(true, "设置分区关闭成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultResponse(false, "设置分区关闭失败");
        }
    }


    @PutMapping
    public ResultResponse updateMarketCategory(@RequestBody MarketCategory marketCategory) {

        if (marketCategory.getId() == null || marketCategory.getId() <= 0) {
            return new ResultResponse(false, "市场分区id为空");
        }

        try {
            marketCategoryService.update(marketCategory);

            redisUtil.delete(RedisKeyEnum.MARKET_CATEGORY_LIST.getKey());
            redisUtil.delete(RedisKeyEnum.MARKET_CATEGORY_ALLLIST.getKey());
            return new ResultResponse(true, "更新分区成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultResponse(false, "更新分区失败");
        }
    }


}
