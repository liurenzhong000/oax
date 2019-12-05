package com.oax.entity.front;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class IndexPageMarket {
    private Integer id;
    //排序
    private Integer sortNo;
    //创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    //更新时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
    //交易对
    private String dealTroops;
    //发布者
    private String adminName;

    /**
     * 交易对id
     */
    private int marketId;


}