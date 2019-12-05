package com.oax.form;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

/**
 * @Auther: hyp
 * @Date: 2018/12/26 09:45
 * @Description:
 */
@Setter
@Getter
public class DiceBetForm {

    /**投注个数*/
    private BigDecimal betQty;

    /**小于这个数中奖*/
    private Integer rollUnder;

    /**币种id*/
    private Integer coinId;
}
