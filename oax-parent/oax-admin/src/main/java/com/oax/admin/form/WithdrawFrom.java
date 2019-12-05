package com.oax.admin.form;

import lombok.Data;

/**
 * @Auther: hyp
 * @Date: 2019/2/15 15:26
 * @Description: 提现拉黑
 */
@Data
public class WithdrawFrom {

    private Integer[] withdrawIds;

    private byte passStatus;

    private String remark;
}
