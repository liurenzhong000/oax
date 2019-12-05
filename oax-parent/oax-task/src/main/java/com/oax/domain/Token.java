package com.oax.domain;

import lombok.Data;

@Data
public class Token {

    /**
     * *	integer($int64)
    id
     */
    private int id;

    /**
     * *	string
    example: BTC
     货币
     */
    private String symbol;


    /**
     * *	string
    example: BitCoin
     全称
     */
    private String name;


    /**
     * *	string
    example: d8bea4e8c73c5476dc311f24209b844c
     唯一主键, 如果symbol不变,可以直接使用symbol, 建议使用平台的唯一标识
     */
    private String unique_key;


    /**
     * string
    example: https://bitcoin.org
     token的官方地址
     */
    private String website	;


    /**
     * string
    example: https://coinmarketcap.com/currencies/bitcoin/
     coinmarketcap上对应的url地址
     */
    private String coinmarketcap_url	;


    /**
     * string
    example:
     etherscan/ethplorer 或其他基础链的explorer对应地址
     */
    private String explorer_url	;


    /**
     * string
    example   :
     合约地址
     */
    private String contact_address	;


    /**
     * string
    example:
     官方twitter账号
     */
    private String twitter	;


    /**
     * string
    example:
     官方facebook账号
     */
    private String facebook	;


    /**
     * string
    example: https://github.com/bitcoin
     官方GitHub 账号或地址
     */
    private String github	;


    /**
     * string
    example: https://s2.coinmarketcap.com/static/img/coins/32x32/1.png
     token logo 地址
     */
    private String logo_url	;


}
