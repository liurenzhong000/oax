package com.oax.service.activity;

import com.oax.vo.PanicBuyActivityDataVo;

public interface PanicBuyActivityService {

    void participate(Integer activityId, Integer userId);

    PanicBuyActivityDataVo activityIndex(Integer userId);

}
