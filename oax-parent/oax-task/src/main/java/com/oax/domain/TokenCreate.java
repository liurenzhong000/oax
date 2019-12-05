package com.oax.domain;

import lombok.Data;

@Data
public class TokenCreate {
    /**
     * string
    example: BTC
            货币
     */
    private String symbol;

    /**
     * string
    example: BitCoin
            全称
     */
    private String name;

    /**
     * string
    example: d8bea4e8c73c5476dc311f24209b844c
            唯一主键, 如果symbol 不变,可以直接使用symbol, 建议使用平台的唯一标识
     */
    private String unique_key;

    /**
     *
    example: https://bitcoin.org
    token的官方地址
     */
    private String website	;

    /**
     *
    example: https://coinmarketcap.com/currencies/bitcoin/
    coinmarketcap上对应的url地址
     */
    private String coinmarketcap_url	;

    /**
     *
    example:
    etherscan/ethplorer 或其他基础链的explorer对应地址
     */
    private String explorer_url	;

    /**
     *
    example:
    合约地址
     */
    private String contact_address	;

    /**
     *
    example:
    官方twitter账号
     */
    private String twitter	;

    /**
     *
    example:
    官方facebook账号
     */
    private String facebook	;

    /**
     *
    example: https://github.com/bitcoin
    官方GitHub 账号或地址
     */
    private String github	;

    /**
     *
    example: https://s2.coinmarketcap.com/static/img/coins/32x32/1.png
    token logo 地址
     */
    private String logo_url	;
}
