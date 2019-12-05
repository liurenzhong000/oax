package com.oax.entity.admin.vo;

import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/6/8
 * Time: 15:47
 * <p>
 * 币种对应所有 转入数
 */
@Data
public class CountCoinRechargeQtyVo {

    private int coinId;

    private String totalQty;

}
