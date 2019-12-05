package com.oax.entity.admin.vo;

import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/6/4
 * Time: 20:16
 * <p>
 * 编辑交易对中 币种名称展示
 */
@Data
public class MarketCoinVo {

    private int coinId;
    private int type;
    private String coinName;
}
