package com.oax.domain;

import lombok.Data;

/**
 * 交易所信息
 */
@Data
public class Market {
    /**
     * id
     */
    private int id;

    /**
     *   example: 一直赢
     *             交易所名称
     */
    private String name;


    /**
     *   example: http://always-win.com
     *     官网地址
     */
    private String website;


    /**
     *   example: wechat: always-win, telegram: always-win, email: service@always-win.com
     *             联系方式
     */
    private String contact;


    /**
     *     example: 简介
     *             交易所简介
     */
    private String description	;


    /**
     *  example: http://p0h01hbw2.bkt.clouddn.com/e04064b7f151008854f4f0d6c822b608
     *     logo url
     */
    private String logo_url	;

}
