package com.oax.entity.admin.param;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/6/4
 * Time: 15:01
 * <p>
 * 虚拟币管理列表 参数
 */
@Data
public class SimpleCoinParam extends PageParam {


    /**
     * 币种
     */
    private Integer coinId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date beginTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

}
