package com.oax.admin.vo;

import com.oax.admin.util.UserUtils;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Auther: hyp
 * @Date: 2019/1/11 14:54
 * @Description: 分红数据
 */
@Data
public class BonusDataVo {

    /**达标人数*/
    private Integer reachCount;

    /**bhb / usdt 比例*/
    private BigDecimal bhbUsdtRatio;

    /**总分红量*/
    private BigDecimal allBonus;

    /**是否为测试跑量*/
    private Boolean test;

    /**分红操作开始时间*/
    private Date startTime;

    /**分红操作结束时间*/
    private Date endTime;

    /**快照时间*/
    private String dataTimeStr;

    /**导出的url*/
    private String excelUrl;

    /**操作后台用户*/
    private String adminName;

}
