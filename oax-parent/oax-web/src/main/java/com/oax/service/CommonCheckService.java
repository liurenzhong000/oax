package com.oax.service;

/**
 * 比较通用的判断
 */
public interface CommonCheckService {

    /**用户是否身份证认证*/
    boolean hasAuthentication(Integer userId);

    /**判断用户是否身份证认证，否则抛出异常*/
    void checkHasAuthentication(Integer userId);

    /**用户是否绑定银行卡*/
    boolean hasBankCard(Integer userId);

    /**判断用户是否绑定银行卡，否则抛出异常*/
    void checkHasBankCard(Integer userId);

    void checkHasPhone(Integer userId);

    void checkHasEmail(Integer userId);

    void checkHasTransactionPassword(Integer userId);

    /**判断用户资金是否冻结*/
    void checkFreezing(Integer userId);

    boolean hasFreezing(Integer userId);

    /**判断用户是否有谷歌验证*/
    void checkGoogleCode(Integer userId);

    boolean hasGoogleCode(Integer userId);

}
