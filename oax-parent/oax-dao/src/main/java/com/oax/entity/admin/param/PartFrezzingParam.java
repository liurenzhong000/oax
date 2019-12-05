package com.oax.entity.admin.param;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class PartFrezzingParam extends PageParam {

    /**
     * 用户id
     */
    private Integer userId;
    /**
     * 币种id
     */
    private Integer coinId;

    //冻结开始时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    protected Date freezingTime;

    //冻结结束时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    protected Date unfreezeTime;
    /**
     * 操作用户名称
     */
    private String userName;
    //数量
    private BigDecimal qty;
    /**
     * 状态：0-冻结1-解冻
     */
    private Integer status;

}
