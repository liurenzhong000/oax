package com.oax.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

/**
 *锚定货币
 */
@Data
public class Ticker {

    /**
     * id
     */
    private int id;
    /**
     *  example: d8bea4e8c73c5476dc311f24209b844c
     *     货币唯一主键 BTC 或者平台唯一ID
     */
    private String symbol_key;


    /**
     *  example: BitCoin
     *             货币全称
     */
    private String symbol_name;


    /**
     *     example: 62de4c4f2d82641a44172daf0af6af8c
     *     锚定货币唯一主键 BTC 或者平台唯一ID
     */
    private String anchor_key;


    /**
     *     example: Ethereum
     *             锚定货币全称
     */
    private String anchor_name;


    /**
     *   example: 10.232
     *     最新成交价格
     */
    private double price;


    /**
     *   example: 2018-03-13 13:14:00 +8000
     *     价格最后更新时间 注意时区
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date price_updated_at;

    /**
     *     example: 10.2
     *             24成交量
     */
    private double volume_24h;


    /**
     *    example: 100.223
     *             24小时基于锚定货币的成交额
     */
    private double volume_anchor_24h;

    public Ticker(String symbol_key, String anchor_key) {
        this.symbol_key = symbol_key;
        this.anchor_key = anchor_key;
    }

    public Ticker() {
    }
}
