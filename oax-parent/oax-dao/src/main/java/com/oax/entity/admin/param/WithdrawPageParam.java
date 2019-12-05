package com.oax.entity.admin.param;

import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/6/11
 * Time: 9:52
 */
@Data
public class WithdrawPageParam extends PageParam {

    private Integer userId;

    private Integer coinId;

    private String username;

    /**
     * 状态 -3:转出失败 -2:终审不通过 -1：审核不通过 0：待审核 1:待终审 2：已转出 3：已广播 4：已确认 5:已拉黑
     */
    private Byte withdrawStatus;

    /**
     * 0 正序
     * 1 倒序
     * null 自然排序
     */
    private Integer sortQty;

    private Integer startNumber;

    private Integer endNumber;

}
