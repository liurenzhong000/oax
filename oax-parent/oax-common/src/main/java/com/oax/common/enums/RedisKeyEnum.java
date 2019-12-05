package com.oax.common.enums;

import lombok.Getter;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/6/14
 * Time: 18:31
 * rediskey 枚举类
 */
@Getter
public enum RedisKeyEnum {

    //文章详情
    ARTICLE("article."),
    //文章列表
    ARTICLE_LIST("articleList."),
    //banner列表
    BANNER_LIST("bannerList."),
    //币种锁仓配置列表
    COIN_LOCK_CONFIG_LIST("coinLockConfigList"),
    //分红汇总缓存列表
    SHAREBONUS_LIST("shareBonusList"),
    //币种列表
    COIN_LIST("coinList"),
    //国家列表
    COUNTRY_LIST("countryList"),
    //提示信息
    I180N_MSG("i180n_msg"),
    //市场交易对
    MARKET_LIST("marketList"),
    APP_LAST_VERSION("app_last_version_"),
    //市场分区(未下架)
    MARKET_CATEGORY_LIST("marketCategoryList"),
  //市场分区(所有分区) TODO  cms需要删除缓存
    MARKET_CATEGORY_ALLLIST("marketCategoryAllList"),
    REDPACKET_LIMIT("redPacketLimit"),
    //交易对买入卖出托管订单
    MARKET_ORDERS_MAP("marketOrders_"),
    //市场交易记录
    MARKET_TRADE_LIST("marketTrade_"),
    //交易对K线图
    MARKET_KLINE_LIST("kline_"),
    //首页交易对
    INDEX_MARKET_LIST("index_market"),
    //系统配置变量
    SYSCONFIG_LIST("sysConfigList"),
    //apiList
    API_LIST("apiList."),

    //终审 乐观锁
    WITHDRAW_LASTCHECK("WITHDRAW_LASTCHECK_");

    private String key;

    RedisKeyEnum(String key) {
        this.key = key;
    }
}
