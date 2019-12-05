package com.oax.admin.form;

import com.oax.entity.admin.param.PageParam;
import lombok.Data;

/**
 * @Auther: hyp
 * @Date: 2019/1/14 15:03
 * @Description:
 */
@Data
public class ListDiceRollUnderWinForm extends PageParam {
    private Integer coinId;
}
