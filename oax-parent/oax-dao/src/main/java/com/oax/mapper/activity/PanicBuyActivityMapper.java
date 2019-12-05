package com.oax.mapper.activity;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.oax.entity.activity.PanicBuyActivity;
import com.oax.entity.admin.param.PanicBuyActivityParam;

import java.util.List;

public interface PanicBuyActivityMapper extends BaseMapper<PanicBuyActivity> {

    PanicBuyActivity getTodayNewlyOne();

    PanicBuyActivity getNewlyOne();

    List<PanicBuyActivity> pageForAdmin(PanicBuyActivityParam param);

}
