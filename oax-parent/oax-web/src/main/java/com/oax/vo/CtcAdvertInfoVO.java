package com.oax.vo;

import com.oax.entity.front.vo.CtcAdvertVo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CtcAdvertInfoVO {

    /**
     * 商户卖出广告
     */
    private CtcAdvertVo saleAdvert;

    /**
     * 商户买入广告
     */
    private CtcAdvertVo buyAdvert;

    /**
     * 当前用户是否为商户
     */
    private boolean merchant;

    /**
     * 商户新的订单个数
     */
    private int newOrderSize;

}
