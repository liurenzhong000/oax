package com.oax.entity.admin.vo;

import java.math.BigDecimal;

import com.oax.entity.front.RedPacket;

import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/8/6
 * Time: 16:04
 */
@Data
public class RedPacketVo extends RedPacket {

    private String phone;

    private String email;

    /**
     * 已被领取人民币估值
     */
    private BigDecimal grabCny;


}
