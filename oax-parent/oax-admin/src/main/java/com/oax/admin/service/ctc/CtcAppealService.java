package com.oax.admin.service.ctc;

import com.github.pagehelper.PageInfo;
import com.oax.entity.admin.param.CtcAppealParam;
import com.oax.entity.admin.vo.CtcAppealVo;

public interface CtcAppealService {

    PageInfo<CtcAppealVo> page(CtcAppealParam param);

}
