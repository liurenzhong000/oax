package com.oax.form;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Setter
@Getter
public class BankCardForm {

    /**开户用户名称*/
    private String realName;

    /**开户银行名称*/
    private String bankName;

    /**开户省市*/
    private String city;

    /**开户支行*/
    private String bankBranch;

    /**银行卡号*/
    private String cardNo;

}
