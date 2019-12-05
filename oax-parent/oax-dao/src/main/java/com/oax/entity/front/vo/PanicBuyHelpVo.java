package com.oax.entity.front.vo;

import lombok.Data;

import java.util.Date;

@Data
public class PanicBuyHelpVo {

    /**助力好友手机号*/
    private String phone;

    /**助力值*/
    private Integer helpValue;

    /**助力时间*/
    private Date createTime;

}
