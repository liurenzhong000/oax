package com.oax.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 好友助力
 */

@Data
public class PanicBuyHelpForm {

    @NotNull(message = "shareCode不能为空")
    @ApiModelProperty("用户的分享码")
    private String shareCode;

    @NotNull(message = "手机号不能为空")
    @ApiModelProperty("手机号")
    private String phone;

    @NotNull(message = "助力码不能为空")
    @ApiModelProperty("助力码")
    private String helpCode;

}
