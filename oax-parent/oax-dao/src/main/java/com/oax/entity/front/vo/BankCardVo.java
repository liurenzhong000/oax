package com.oax.entity.front.vo;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BankCardVo {

    private Integer id;

    /**开户用户名称*/
    private String realName;

    /**开户银行名称*/
    private String bankName;

    /**开户支行*/
    private String bankBranch;

    /**银行卡号*/
    private String cardNo;

    /**
     * 是否默认收付银行卡
     */
    private boolean defaultCard;

    /**
     * 银行卡icon图片地址
     */
    private String iconUrl;

    /**
     * 银行卡code
     */
    private String cardCode;
}
