package com.oax.entity.admin.vo;

import java.util.List;

import com.oax.entity.admin.dto.MarketWithCoin;

import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/6/6
 * Time: 18:52
 */
@Data
public class MarketVo {


    /**
     * 市场信息
     */
    private String categoryName;

    /**
     * 市场信息对应 交易对集合
     */
    private List<MarketWithCoin> marketList;


}
