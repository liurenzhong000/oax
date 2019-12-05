package com.oax.mapper.activity;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.oax.entity.activity.PanicBuyHelp;
import com.oax.entity.admin.param.PanicBuyHelpParam;

import java.util.List;

public interface PanicBuyHelpMapper extends BaseMapper<PanicBuyHelp> {

    List<PanicBuyHelp> pageForAdmin(PanicBuyHelpParam param);
}
