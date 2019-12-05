package com.oax.entity.admin.vo;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/6/11
 * Time: 10:10
 * 虚拟货币转出 vo
 */
@Data
public class WithdrawPageVo {

    private int id;

    private int userId;

    private String phone;

    private String email;

    private String coinName;

    private BigDecimal qty;

    private BigDecimal fee;

    private Integer status;

    /**
     * 实际到账
     */
    private BigDecimal account;

    private String toAddress;

    //    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateTime;

    //    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    /**
     * 交易hash
     */
    private String txId;



    public BigDecimal getAccount() {
        if (qty != null && fee != null)
            return qty.subtract(fee);
        return account;
    }

}
