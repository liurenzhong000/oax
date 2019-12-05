package com.oax.entity.front.param;

import com.oax.entity.admin.param.PageParam;
import com.oax.entity.enums.CtcAdvertStatus;
import com.oax.entity.enums.CtcAdvertType;
import lombok.Getter;
import lombok.Setter;

/**
 * 商户发布的广告  历史列表参数
 */
@Setter
@Getter
public class CtcAdvertParam extends PageParam {

    /**
     * 广告类别
     */
    private CtcAdvertType type;

    /**
     * 广告状态（下架、上架）
     */
    private CtcAdvertStatus status;
}
