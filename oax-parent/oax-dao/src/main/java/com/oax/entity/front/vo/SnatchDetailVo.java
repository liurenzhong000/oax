package com.oax.entity.front.vo;

import lombok.Data;

/**
 * @Auther: hyp
 * @Date: 2019/1/18 15:15
 * @Description: 投注编号（投注编号展示）
 */
@Data
public class SnatchDetailVo {

    /**编号*/
    private String numberStr;

    /**hash*/
    private String hash;

    /**跳转区块浏览器链接地址*/
    private String hashUrl;

    /**状态，0未开奖 1未中奖 2中奖*/
    private Integer status;

    /**开奖编号是不是我*/
    private Boolean mine;
}
