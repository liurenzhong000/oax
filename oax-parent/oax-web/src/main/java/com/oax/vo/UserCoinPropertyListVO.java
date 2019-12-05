package com.oax.vo;

import com.oax.entity.front.UserCoinInfo;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 用户资金页返回数据
 */
@Data
public class UserCoinPropertyListVO {

    private BigDecimal withdrawalAmount;

    private BigDecimal useredWithdrawal;

    private List<UserCoinInfo> coinList;

    //用户资金总额
    private UserCoinTotalVo total;
}
